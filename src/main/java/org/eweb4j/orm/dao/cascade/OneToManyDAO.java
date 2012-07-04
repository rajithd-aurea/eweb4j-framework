package org.eweb4j.orm.dao.cascade;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.eweb4j.orm.config.ORMConfigBeanUtil;
import org.eweb4j.orm.dao.DAOException;
import org.eweb4j.orm.dao.DAOFactory;
import org.eweb4j.orm.jdbc.transaction.Trans;
import org.eweb4j.orm.jdbc.transaction.Transaction;
import org.eweb4j.util.ClassUtil;
import org.eweb4j.util.ReflectUtil;

/**
 * 
 * @author weiwei
 * 
 */
public class OneToManyDAO {
	private String dsName;
	private Object t;
	private List<Field> fields;
	private ReflectUtil ru;
	private String idField;
	private String idColumn;
	private String idVal;
	private Method idGetter;
	private String table;

	public OneToManyDAO(String dsName) {
		this.dsName = dsName;
	}

	/**
	 * 初始化
	 * 
	 * @param t
	 * @param fields
	 * @throws DAOException
	 */
	public void init(Object t, List<Field> fields) throws DAOException {
		this.t = t;
		this.fields = fields;
		this.ru = new ReflectUtil(this.t);
		this.table = ORMConfigBeanUtil.getTable(this.t.getClass());
		// 主类的ID属性名
		this.idField = ORMConfigBeanUtil.getIdField(this.t.getClass());
		this.idGetter = ru.getGetter(idField);
		if (idGetter == null)
			throw new DAOException("can not find idGetter.", null);

		this.idColumn = ORMConfigBeanUtil.getIdColumn(this.t.getClass());

		Object idVal = null;
		try {
			idVal = idGetter.invoke(this.t);
			this.idVal = idVal == null ? null : String.valueOf(idVal);
		} catch (Exception e) {
			throw new DAOException(idGetter + " invoke exception ", e);
		}

	}

	/**
	 * 一对多（主从）级联插入 。 
	 * 1. 如果主对象ID值没有，将主对象插入数据库并获取ID值 
	 * 2. 遍历从对象，找到mappedBy 
	 * 3. 注入主对象，插入关系
	 * 4. 如果找不到mappedBy，则先找@JoinTable，然后插入次对象获取id值，接着拼凑 sql 语句，插入关系 5.
	 * 如果找不到@JoinTable，则根据主对象 class 在从对象属性中找。然后注入主对象，插入关系。
	 */
	public void insert() throws DAOException {
		if (this.fields == null || this.fields.size() == 0)
			return;

		final Class<?> ownClass = ru.getObject().getClass();

		Transaction.execute(new Trans() {

			@Override
			public void run(Object... args) throws Exception {
				if (idVal == null || "0".equals(idVal) || "".equals(idVal)) {
					Object _idVal = DAOFactory.getInsertDAO(dsName).insert(t);
					idVal = String.valueOf(_idVal);
					Method idSetter = ru.getSetter(idField);
					idSetter.invoke(t, Integer.parseInt(idVal));
				} else if (DAOFactory.getSelectDAO(dsName).selectOne(t, idField) == null) {
					throw new Exception("the main object'id val is invalid!");
				}

				for (Field f : fields) {
					Method fGetter = ru.getGetter(f.getName());
					if (fGetter == null)
						continue;

					OneToMany oneToMany = null;
					if (f.isAnnotationPresent(OneToMany.class)) {
						oneToMany = f.getAnnotation(OneToMany.class);
					} else if (fGetter.isAnnotationPresent(OneToMany.class)) {
						oneToMany = fGetter.getAnnotation(OneToMany.class);
					} else {
						continue;
					}

					Class<?> tarClass = oneToMany.targetEntity();
					if (void.class.isAssignableFrom(tarClass)) {
						tarClass = ClassUtil.getGenericType(f);
					}

					List<?> fList = null;

					try {
						fList = (List<?>) fGetter.invoke(t);
					} catch (Exception e) {
						throw new DAOException(fGetter + " invoke exception ",e);
					}

					if (fList == null)
						continue;

					for (int i = 0; i < fList.size(); i++) {
						Object tarObj = fList.get(i);
						if (tarObj == null)
							continue;

						ReflectUtil tarRu = new ReflectUtil(tarObj);
						String mappedBy = oneToMany.mappedBy();
						if (mappedBy != null && mappedBy.trim().length() > 0) {
							Method ownFieldSetter = tarRu.getSetter(mappedBy);
							if (ownFieldSetter == null)
								continue;

							// finished
							ownFieldSetter.invoke(tarObj,ru.getObject());
							DAOFactory.getInsertDAO(dsName).insert(tarObj);
						} else {
							JoinTable joinTable = null;
							if (f.isAnnotationPresent(JoinTable.class)) {
								joinTable = f.getAnnotation(JoinTable.class);
							} else if (fGetter.isAnnotationPresent(JoinTable.class)) {
								joinTable = fGetter.getAnnotation(JoinTable.class);
							} else {
								// find ownclass in tarObj fields
								for (Field tarObjField : tarRu.getFields()) {
									if (tarObjField.getType().getName().equals(ownClass.getName())) {
										Method ownFieldSetter = tarRu.getSetter(tarObjField.getName());
										if (ownFieldSetter == null)
											continue;

										// finished
										ownFieldSetter.invoke(tarObj,ru.getObject());
										DAOFactory.getInsertDAO(dsName).insert(tarObj);
										break;
									}
								}
							}
							if (joinTable != null){
								JoinColumn[] froms = joinTable.joinColumns();
								if (froms == null || froms.length == 0)
									continue;
	
								JoinColumn[] tos = joinTable.inverseJoinColumns();
								if (tos == null || tos.length == 0)
									continue;
	
								String relTable = joinTable.name();
	
								// insert into relTable (from, to) values(?, ?) ;
								String format = "insert into %s(%s, %s) values(?, ?) ;";
								String sql = String.format(format, relTable,froms[0], tos[0]);
								Object tarObjIdVal = DAOFactory.getInsertDAO(dsName).insert(tarObj);
	
								// finished
								DAOFactory.getInsertDAO(dsName).insertBySql(tarClass, sql, idVal, tarObjIdVal);
							}
						}
					}
				}
			}
		});

	}

	/**
	 * 
	 * 一对多（主从）级联删除 1.前提条件必须主对象要存在于数据库中
	 * 2.检查当前主对象中的关联对象，如果关联对象为空，则删除所有与主对象有关的关联关系。
	 * 3.如果当前主对象中含有关联对象，则删除这些关联对象与主对象的关系
	 * 4.不会删除主对象
	 */
	public void delete() throws DAOException {
		if (this.fields == null || this.fields.size() == 0)
			return;

		final Class<?> ownClass = ru.getObject().getClass();
		Transaction.execute(new Trans() {

			@Override
			public void run(Object... args) throws Exception {
				String referencedField = idField;
				Object referencedFieldVal = idVal;
				
				for (Field f : fields) {
					Method tarGetter = ru.getGetter(f.getName());
					if (tarGetter == null)
						continue;

					OneToMany ann = tarGetter.getAnnotation(OneToMany.class);
					if (ann == null) {
						ann = f.getAnnotation(OneToMany.class);
						if (ann == null)
							continue;
					}
					String mappedBy = ann.mappedBy();

					Class<?> tarClass = ann.targetEntity();
					if (void.class.isAssignableFrom(tarClass))
						tarClass = ClassUtil.getGenericType(f);

					List<?> tarList = null;

					try {
						tarList = (List<?>) tarGetter.invoke(t);
					} catch (Exception e) {
						throw new DAOException(
								tarGetter + " invoke exception ", e);
					}
					
					if (tarList == null || tarList.size() == 0) {
						// 当关联对象为空的时候，删除所有关联对象
						ReflectUtil tarRu = new ReflectUtil(tarClass);
						if (mappedBy == null || mappedBy.trim().length() == 0) {
							for (Field tarObjField : tarRu.getFields()) {
								if (tarObjField.getType().getName().equals(ownClass.getName())) {
									if (!tarObjField.getType().getName().equals(ownClass.getName()))
										continue;
									
									Method tarObjFieldGetter = tarRu.getGetter(tarObjField.getName());
									if (tarObjFieldGetter == null)
										continue;
									
									ManyToOne manyToOne = tarObjField.getAnnotation(ManyToOne.class);
									if (manyToOne == null)
										manyToOne = tarObjFieldGetter.getAnnotation(ManyToOne.class);
									if (manyToOne == null)
										continue;
									
									JoinColumn joinCol = tarObjField.getAnnotation(JoinColumn.class);
									if (joinCol == null)
										joinCol = tarObjFieldGetter.getAnnotation(JoinColumn.class);
									
									if (joinCol != null){
										String referencedColumn = joinCol.referencedColumnName();
										if (referencedColumn == null || referencedColumn.trim().length() == 0)
											referencedColumn = idColumn;
										
										referencedField = ORMConfigBeanUtil.getField(ownClass, referencedColumn);
										Method referencedFieldGetter = ru.getGetter(referencedField);
										if (referencedFieldGetter != null)
											referencedFieldVal = referencedFieldGetter.invoke(t);
									}
									
									// finished
									mappedBy = tarObjField.getName(); 
									
									DAOFactory.getDeleteDAO(dsName).deleteByFieldIsValue(tarClass, new String[]{tarObjField.getName()}, new String[]{String.valueOf(referencedFieldVal)});
									break;
								}
							}
						}

					} else {
						for (int i = 0; i < tarList.size(); i++) {
							Object tarObj = tarList.get(i);
							if (tarObj == null)
								continue;
							
							Object tarObjIdVal = ORMConfigBeanUtil.getIdVal(tarObj);
							if (tarObjIdVal == null)
								continue;
							
							ReflectUtil tarRu = new ReflectUtil(tarObj);

							if (mappedBy != null && mappedBy.trim().length() > 0) {
								Method ownFieldSetter = tarRu.getSetter(mappedBy);
								if (ownFieldSetter == null)
									continue;

								// finished
								DAOFactory.getDeleteDAO(dsName).deleteById(tarObj);
							} else {
								JoinTable joinTable = null;
								if (f.isAnnotationPresent(JoinTable.class)) {
									joinTable = f.getAnnotation(JoinTable.class);
								} else if (tarGetter.isAnnotationPresent(JoinTable.class)) {
									joinTable = tarGetter.getAnnotation(JoinTable.class);
								} else {
									// find ownclass in tarObj fields
									for (Field tarObjField : tarRu.getFields()) {
										if (!tarObjField.getType().getName().equals(ownClass.getName()))
											continue;
										
										Method tarObjFieldGetter = tarRu.getGetter(tarObjField.getName());
										if (tarObjFieldGetter == null)
											continue;
										
										ManyToOne manyToOne = tarObjField.getAnnotation(ManyToOne.class);
										if (manyToOne == null)
											manyToOne = tarObjFieldGetter.getAnnotation(ManyToOne.class);
										if (manyToOne == null)
											continue;
										
										JoinColumn joinCol = tarObjField.getAnnotation(JoinColumn.class);
										if (joinCol == null)
											joinCol = tarObjFieldGetter.getAnnotation(JoinColumn.class);
										
										if (joinCol != null){
											String referencedColumn = joinCol.referencedColumnName();
											if (referencedColumn == null || referencedColumn.trim().length() == 0)
												referencedColumn = idColumn;
											
											referencedField = ORMConfigBeanUtil.getField(ownClass, referencedColumn);
											Method referencedFieldGetter = ru.getGetter(referencedField);
											if (referencedFieldGetter != null)
												referencedFieldVal = referencedFieldGetter.invoke(t);
										}
										
										// finished
										mappedBy = tarObjField.getName(); 
										
										DAOFactory.getDeleteDAO(dsName).deleteByFieldIsValue(tarClass, new String[]{tarObjField.getName()}, new String[]{String.valueOf(referencedFieldVal)});
										break;
									}
								}
								
								if (joinTable != null){
									JoinColumn[] froms = joinTable.joinColumns();
									if (froms == null || froms.length == 0)
										continue;
	
									JoinColumn[] tos = joinTable.inverseJoinColumns();
									if (tos == null || tos.length == 0)
										continue;
	
									String relTable = joinTable.name();
	
									// delete from relTable where from = ? and to = ? ;
									String format = "delete from %s where %s = ? and %s = ? ;";
									String sql = String.format(format, relTable,froms[0], tos[0]);
	
									// finished
									DAOFactory.getUpdateDAO(dsName).updateBySQLWithArgs(sql, idVal, ORMConfigBeanUtil.getIdVal(tarObj));
								}
							}
						}
					}

				}
			}
		});
	}

	/**
	 * 一对多（主从）级联查询
	 */
	public void select() throws DAOException {
		if (this.fields == null || this.fields.size() == 0)
			return;
		Object referencedFieldVal = idVal;
		
		Class<?> ownClass = ru.getObject().getClass();
		for (Field f : fields) {
			Method tarGetter = ru.getGetter(f.getName());
			if (tarGetter == null)
				continue;

			OneToMany ann = tarGetter.getAnnotation(OneToMany.class);
			if (ann == null) {
				ann = f.getAnnotation(OneToMany.class);
				if (ann == null)
					continue;
			}
			
			Class<?> tarClass = ann.targetEntity();
			if (void.class.isAssignableFrom(tarClass))
				tarClass = ClassUtil.getGenericType(f);
			try {
				ReflectUtil tarRu = new ReflectUtil(tarClass);
				
				List<?> tarList = null;
				
				String mappedBy = ann.mappedBy();
				if (mappedBy != null && mappedBy.trim().length() > 0) {
					Method ownFieldSetter = tarRu.getSetter(mappedBy);
					if (ownFieldSetter == null)
						continue;
				} else {
					JoinTable joinTable = null;
					if (f.isAnnotationPresent(JoinTable.class)) {
						joinTable = f.getAnnotation(JoinTable.class);
					} else if (tarGetter.isAnnotationPresent(JoinTable.class)) {
						joinTable = tarGetter.getAnnotation(JoinTable.class);
					} else {
						// find ownclass in tarObj fields
						for (Field tarObjField : tarRu.getFields()) {
							if (!tarObjField.getType().getName().equals(ownClass.getName()))
								continue;
							
							Method tarObjFieldGetter = tarRu.getGetter(tarObjField.getName());
							if (tarObjFieldGetter == null)
								continue;
							
							ManyToOne manyToOne = tarObjField.getAnnotation(ManyToOne.class);
							if (manyToOne == null)
								manyToOne = tarObjFieldGetter.getAnnotation(ManyToOne.class);
							if (manyToOne == null)
								continue;
							
							JoinColumn joinCol = tarObjField.getAnnotation(JoinColumn.class);
							if (joinCol == null)
								joinCol = tarObjFieldGetter.getAnnotation(JoinColumn.class);
							
							if (joinCol != null){
								String referencedColumn = joinCol.referencedColumnName();
								if (referencedColumn == null || referencedColumn.trim().length() == 0)
									referencedColumn = idColumn;
								
								String referencedField = ORMConfigBeanUtil.getField(ownClass, referencedColumn);
								Method referencedFieldGetter = ru.getGetter(referencedField);
								if (referencedFieldGetter != null)
									referencedFieldVal = referencedFieldGetter.invoke(t);
							}
							
							// finished
							mappedBy = tarObjField.getName(); 
							break;
						}
					}
	
					if (joinTable != null){
						JoinColumn[] froms = joinTable.joinColumns();
						if (froms == null || froms.length == 0)
							continue;
		
						JoinColumn[] tos = joinTable.inverseJoinColumns();
						if (tos == null || tos.length == 0)
							continue;
						
						String tarTable = joinTable.name();
		
						String format = "select %s from %s where %s = ?  ;";
						String sql = String.format(format, ORMConfigBeanUtil.getSelectAllColumn(tarClass), tarTable, froms[0]);
		
						// finished
						tarList = DAOFactory.getSelectDAO(dsName).selectBySQL(tarClass, sql, idVal);
					}
				}
				if (tarList == null)
					tarList = DAOFactory.getSearchDAO(dsName).searchByExactAndOrderByIdFieldDESC(tarClass, new String[]{mappedBy}, new String[]{String.valueOf(referencedFieldVal)}, false);
				
				if (tarList == null)
					continue;
	
				Method tarSetter = ru.getSetter(f.getName());
				if (tarSetter == null)
					continue;
				
				tarSetter.invoke(t, tarList);
			} catch (Exception e) {

				throw new DAOException("", e);
			}
		}
	}

	/**
	 * 一对多级联更新
	 */
	public void update(final long newIdVal) {

		if (newIdVal <= 0 || this.fields == null || this.fields.size() == 0)
			return;

		if (idVal == null || "0".equals(idVal) || "".equals(idVal)) {
			return;
		} else if (DAOFactory.getSelectDAO(dsName).selectOne(t, this.idField) == null) {
			// 检查一下当前对象的ID是否存在于数据库
			return;
		}
		
		try{
			Transaction.execute(new Trans() {
				@Override
				public void run(Object... args) throws Exception {
					Class<?> ownClass = ru.getObject().getClass();
		
					// "update {table} set {idCol} = {newIdVal} where {idCol} = {idVal}
					// ; update {tarTable} set {fkCol} = {newIdVal} where {fkCol} = {idVal}"
					String format = "update %s set %s = %s where %s = %s ;";
					for (Field f : fields) {
						Method tarGetter = ru.getGetter(f.getName());
						if (tarGetter == null)
							continue;

						OneToMany ann = tarGetter.getAnnotation(OneToMany.class);
						if (ann == null)
							ann = f.getAnnotation(OneToMany.class);

						if (ann == null)
							continue;
			
						Class<?> tarClass = ann.targetEntity();
						if (void.class.isAssignableFrom(tarClass))
							tarClass = ClassUtil.getGenericType(f);
			
						String mappedBy = ann.mappedBy();
			
						ReflectUtil tarRu = new ReflectUtil(tarClass);
						if (mappedBy != null && mappedBy.trim().length() > 0) {
							Method ownFieldSetter = tarRu.getSetter(mappedBy);
							if (ownFieldSetter == null)
								continue;
						} else {
							JoinTable joinTable = null;
							if (f.isAnnotationPresent(JoinTable.class)) {
								joinTable = f.getAnnotation(JoinTable.class);
							} else if (tarGetter.isAnnotationPresent(JoinTable.class)) {
								joinTable = tarGetter.getAnnotation(JoinTable.class);
							} else {
								// find ownclass in tarObj fields
								for (Field tarObjField : tarRu.getFields()) {
									if (tarObjField.getType().getName().equals(ownClass.getName())) {
										// finished
										mappedBy = tarObjField.getName(); 
										break;
									}
								}
							}
							if (joinTable != null){
								JoinColumn[] froms = joinTable.joinColumns();
								if (froms == null || froms.length == 0)
									continue;
	
								JoinColumn[] tos = joinTable.inverseJoinColumns();
								if (tos == null || tos.length == 0)
									continue;
	
								String relTable = joinTable.name();
	
								// update relTable set from = ? where from = ? ;
								String _format = "update %s set %s = ? where %s = ? ;" ;
								String sql = String.format(_format, relTable, froms[0], froms[0]);
								DAOFactory.getUpdateDAO(dsName).updateBySQLWithArgs(sql, newIdVal, idVal);
								
								continue;
							}
						}
			
						// "update {table} set {idCol} = {newIdVal} where {idCol} = {idVal} ;
						//  update {tarTable} set {fkCol} = {newIdVal} where {fkCol} = {idVal} ;"
						final String sql = String.format(format, table, idColumn,newIdVal, idColumn, idVal);
						
						DAOFactory.getUpdateDAO(dsName).updateBySQL(sql);
						DAOFactory.getDAO(tarClass, dsName).update().set(new String[]{mappedBy}, newIdVal).where().field(mappedBy).equal(idVal).execute();
					}
				}
			});
		} catch (Exception e) {
			throw new DAOException("", e);
		}
	}
}
