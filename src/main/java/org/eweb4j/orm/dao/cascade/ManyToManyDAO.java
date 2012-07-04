package org.eweb4j.orm.dao.cascade;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.eweb4j.orm.config.ORMConfigBeanUtil;
import org.eweb4j.orm.dao.DAOException;
import org.eweb4j.orm.dao.DAOFactory;
import org.eweb4j.orm.jdbc.transaction.Trans;
import org.eweb4j.orm.jdbc.transaction.Transaction;
import org.eweb4j.util.ClassUtil;
import org.eweb4j.util.ReflectUtil;

public class ManyToManyDAO {
	private String dsName;
	private Object t;
	private List<Field> fields;
	private ReflectUtil ru;
	private String idField;
	private Method idSetter;
	private Method idGetter;
	private String table;
	private String idVal;
	private String idColumn;

	public ManyToManyDAO(String dsName) {
		this.dsName = dsName;
	}

	public void init(Object t, List<Field> fields) throws DAOException {
		this.t = t;
		this.fields = fields;
		this.ru = new ReflectUtil(this.t);
		this.table = ORMConfigBeanUtil.getTable(this.t.getClass());
		// 主类的ID属性名
		this.idField = ORMConfigBeanUtil.getIdField(this.t.getClass());
		this.idSetter = ru.getSetter(idField);
		if (this.idSetter == null)
			throw new DAOException("can not get idSetter.", null);

		this.idGetter = ru.getGetter(idField);
		if (this.idGetter == null)
			throw new DAOException("can not get idGetter.", null);

		this.idColumn = ORMConfigBeanUtil.getIdColumn(this.t.getClass());
		
		try {
			Object _idVal = idGetter.invoke(this.t);
			this.idVal = _idVal == null ? null : String.valueOf(_idVal);
		} catch (Exception e) {
			throw new DAOException(idGetter + " invoke exception ", e);
		}
	}

	/**
	 * 多对多级联插入 
	 * 1.取得主对象idVal（如果没有，则先插入数据库，获取idVal）
	 * 2.取得关联对象tarIdVal（如果没有，先插入数据库，获取）
	 * 3.检查下是否有重复记录 
	 * 4.插入到关系表
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void insert() throws DAOException {
		if (this.fields == null || this.fields.size() == 0)
			return;
		
		Transaction.execute(new Trans() {
			@Override
			public void run(Object... args) throws Exception {
				// "insert into {relTable}({from},{to}) values({idVal},{toVal})"
				// 插入关系表
				String format = "INSERT INTO %s(%s,%s) VALUES(?,?) ";
				if (idVal == null || "0".equals(idVal) || "".equals(idVal)) {
					Object _idVal = DAOFactory.getInsertDAO(dsName).insert(t);
					idVal = String.valueOf(_idVal);
					Method idSetter = ru.getSetter(idField);
					idSetter.invoke(t, Integer.parseInt(idVal));
				} else if (DAOFactory.getSelectDAO(dsName).selectOne(t, idField) == null) {
					throw new Exception("the main object'id val is invalid!");
				}
				
				for (Field f : fields) {
					String name = f.getName();
					Method tarGetter = ru.getGetter(name);
					if (tarGetter == null)
						continue;
		
					ManyToMany ann = tarGetter.getAnnotation(ManyToMany.class);
					if (ann == null) {
						ann = f.getAnnotation(ManyToMany.class);
						if (ann == null)
							continue;
					}
		
					JoinTable join = tarGetter.getAnnotation(JoinTable.class);
					if (join == null) {
						join = f.getAnnotation(JoinTable.class);
						if (join == null)
							continue;
					}
		
					JoinColumn[] froms = join.joinColumns();
					if (froms == null || froms.length == 0)
						continue;
		
					JoinColumn[] tos = join.inverseJoinColumns();
					if (tos == null || tos.length == 0)
						continue;
		
					Class<?> tarClass = ann.targetEntity();
					if (void.class.isAssignableFrom(tarClass)) {
						tarClass = ClassUtil.getGenericType(f);
					}
					String relTable = join.name();
					String tarIdField = ORMConfigBeanUtil.getIdField(tarClass);
		
					Object _tarObj = null;
					try {
						_tarObj = tarGetter.invoke(t);
					} catch (Exception e) {
						throw new DAOException(tarGetter + " invoke exception ", e);
					}
					// 检查一下目标对象是否存在于数据库
					if (_tarObj == null)
						continue;
		
					List<?> tarList = (List<?>) _tarObj;
					for (int i = 0; i < tarList.size(); i++) {
						Object tarObj = tarList.get(i);
						String from = froms[0].name();
						String to = tos[0].name();
		
						ReflectUtil tarRu = new ReflectUtil(tarObj);
						Method tarIdGetter = tarRu.getGetter(tarIdField);
						Object _tarIdVal = null;
		
						try {
							_tarIdVal = tarIdGetter.invoke(tarObj);
						} catch (Exception e) {
							throw new DAOException(tarIdGetter + " invoke exception ",e);
						}
		
						if (_tarIdVal == null)
							continue;
		
						String tarIdVal = String.valueOf(_tarIdVal);
						Object tempObj = DAOFactory.getSelectDAO(dsName).selectOne(tarClass, new String[] { tarIdField },new String[] { tarIdVal });
		
						if (tempObj == null) {
							// 如果目标对象不存在于数据库，则将目标对象插入到数据库
							Object tarIdValObj = DAOFactory.getInsertDAO(dsName).insert(tarObj);
							// 将获取到的id值注入到tarObj中
							Method tarIdSetter = tarRu.getSetter(tarIdField);
							try {
								tarIdSetter.invoke(tarObj, tarIdValObj);
							} catch (Exception e) {
								throw new DAOException(tarIdSetter + " invoke exception ", e);
							}
							tarIdVal = String.valueOf(tarIdValObj);
						}
		
						// 插入到关系表中
						// 先检查下是否有重复记录
						// "select {from},{to} from {relTable} where {from} = {idVal} and {to} = {toVal} "
						String _format = "select %s, %s from %s where %s = ? and %s = ? ";
						String _sql = String.format(_format, from, to, relTable, from, to);
						if (DAOFactory.getSelectDAO(dsName).selectBySQL(Map.class, _sql, idVal, tarIdVal) != null)
							continue;
		
						// "INSERT INTO %s(%s,%s) VALUES(?,?) "
						String sql = String.format(format, relTable, from, to);
						DAOFactory.getUpdateDAO(dsName).updateBySQLWithArgs(sql, idVal, tarIdVal);
					}
				}
			}
		});
	}

	/**
	 * 多对多级联删除 
	 * 1.如果主对象不存在与数据库，不处理 
	 * 2.否则，检查当前主对象中的关联对象，如果关联对象为空，则删除所有与主对象有关的关联关系。
	 * 3.如果当前主对象中含有关联对象，则删除这些关联对象与主对象的关系
	 * 4.不会删除主对象
	 * 
	 */
	public void delete() throws DAOException {
		if (this.fields == null || this.fields.size() == 0)
			return;

		// "delete from {relTable} WHERE {from} = {idVal} ;"
		String format = "delete from %s WHERE %s = ? ";
		if (idVal == null || "0".equals(idVal) || "".equals(idVal))
			return;
		else if (DAOFactory.getSelectDAO(dsName).selectOne(t, this.idField) == null)
			return;

		for (Field f : fields) {
			Method tarGetter = ru.getGetter(f.getName());
			if (tarGetter == null)
				continue;

			ManyToMany ann = tarGetter.getAnnotation(ManyToMany.class);
			if (ann == null) {
				ann = f.getAnnotation(ManyToMany.class);
				if (ann == null)
					continue;
			}

			JoinTable join = tarGetter.getAnnotation(JoinTable.class);
			if (join == null) {
				join = f.getAnnotation(JoinTable.class);
				if (join == null)
					continue;
			}

			JoinColumn[] froms = join.joinColumns();
			if (froms == null || froms.length == 0)
				continue;

			JoinColumn[] tos = join.inverseJoinColumns();
			if (tos == null || tos.length == 0)
				continue;

			String relTable = join.name();
			String from = froms[0].name();

			List<?> tarList = null;

			try {
				tarList = (List<?>) tarGetter.invoke(t);
			} catch (Exception e) {
				throw new DAOException(tarGetter + " invoke exception ", e);
			}

			if (tarList == null || tarList.size() == 0) {
				String sql = String.format(format, relTable, from);
				// 删除所有关联记录
				DAOFactory.getUpdateDAO(dsName).updateBySQLWithArgs(sql,idVal);
			} else {
				// 删除指定关联的记录
				String to = tos[0].name();
				Class<?> tarClass = ann.targetEntity();
				if (void.class.isAssignableFrom(tarClass)) {
					tarClass = ClassUtil.getGenericType(f);
				}
				
				// "delete from {relTable} where {from} = {idVal} and to = {toVal}"
				String _format = "delete from %s where %s = ? and %s = ?";
				for (int i = 0; i < tarList.size(); i++) {
					Object tarObj = tarList.get(i);
					if (tarObj == null)
						continue;
					ReflectUtil tarRu = new ReflectUtil(tarObj);
					String tarIdField = ORMConfigBeanUtil.getIdField(tarClass);
					Method tarIdGetter = tarRu.getGetter(tarIdField);
					Object toValObj = null;

					try {
						toValObj = tarIdGetter.invoke(tarObj);
					} catch (Exception e) {
						throw new DAOException(tarIdGetter+ "invoke exception ", e);
					}

					if (toValObj == null)
						continue;

					String toVal = String.valueOf(toValObj);
					if (DAOFactory.getSelectDAO(dsName).selectOne(tarClass, new String[] { tarIdField },new String[] { toVal }) == null)
						continue;

					String _sql = String.format(_format, relTable, from, to);
					DAOFactory.getUpdateDAO(dsName).updateBySQLWithArgs(_sql, idVal, toVal);
				}
			}
		}
	}


	/**
	 * 多对多级联查询 
	 * 1.当主对象没有包含任何一个关联对象时，默认查询所有与之关联的对象
	 * 2.当主对象中包含了关联对象时（含有其id值），则只查询这些关联的对象
	 * 
	 * @param <T>
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void select() throws DAOException {
		if (this.fields == null || this.fields.size() == 0)
			return;
		// select %s from {tarTable} where {referencedColumn} in (select {to} from {relTable} where {from} = {idVal})
		String format = "SELECT %s FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s = ?) ";
		if (idVal == null || "0".equals(idVal) || "".equals(idVal))
			return;

		for (Field f : fields) {
			Method tarGetter = ru.getGetter(f.getName());
			if (tarGetter == null)
				continue;

			ManyToMany ann = tarGetter.getAnnotation(ManyToMany.class);
			if (ann == null) {
				ann = f.getAnnotation(ManyToMany.class);
				if (ann == null)
					continue;
			}

			JoinTable join = tarGetter.getAnnotation(JoinTable.class);
			if (join == null) {
				join = f.getAnnotation(JoinTable.class);
				if (join == null)
					continue;
			}

			JoinColumn[] froms = join.joinColumns();
			if (froms == null || froms.length == 0)
				continue;

			JoinColumn[] tos = join.inverseJoinColumns();
			if (tos == null || tos.length == 0)
				continue;

			// 多对多关系目标Class
			Class<?> tarClass = ann.targetEntity();
			if (void.class.isAssignableFrom(tarClass)) {
				tarClass = ClassUtil.getGenericType(f);
			}
			
			String tarTable = ORMConfigBeanUtil.getTable(tarClass);
			// 目标类对应的数据库表Id字段
			String referencedColumn = tos[0].referencedColumnName();
			if (referencedColumn == null || referencedColumn.trim().length() == 0)
				referencedColumn = ORMConfigBeanUtil.getIdColumn(tarClass);
			
			// 目标类在第三方关系表中的字段名
			String to = tos[0].name();
			
			// 第三方关系表
			String relTable = join.name();
			
			// 主类在第三方关系表中的字段名
			String from = froms[0].name();

			try {
				List<?> tarList = null;
				tarList = (List<?>) tarGetter.invoke(t);

				if (tarList != null && tarList.size() > 0) {
					for (int i = 0; i < tarList.size(); i++) {
						Object tarObj = tarList.get(i);
						ReflectUtil tarRu = new ReflectUtil(tarObj);
						String tarIdField = ORMConfigBeanUtil.getIdField(tarClass);
						Method tarIdGetter = tarRu.getGetter(tarIdField);
						Object tarIdValObj = tarIdGetter.invoke(tarObj);
						if (tarIdValObj == null)
							continue;
						String tarIdVal = String.valueOf(tarIdValObj);
						// 查询 select %s from {tarTable} where {tarIdColumn} = {tarIdVal}
						tarObj = DAOFactory.getSelectDAO(dsName).selectOne(tarClass, new String[] { tarIdField },new String[] { tarIdVal });
					}
				} else {
					String sql = String.format(format,ORMConfigBeanUtil.getSelectAllColumn(tarClass),tarTable, referencedColumn, to, relTable, from);
					// 从数据库中取出与当前主对象id关联的所有目标对象，
					tarList = DAOFactory.getSelectDAO(dsName).selectBySQL(tarClass, sql, idVal);
				}

				// 并注入到当前主对象的属性中
				Method tarSetter = ru.getSetter(f.getName());

				tarSetter.invoke(t, tarList);
			} catch (Exception e) {
				e.printStackTrace();
				throw new DAOException("", e);
			}
		}
	}

	/**
	 * 一对多级联更新
	 */
	public void update(long newIdVal) {
		if (newIdVal <= 0 || this.fields == null || this.fields.size() == 0)
			return;
		if (this.idVal == null || "0".equals(this.idVal) || "".equals(this.idVal)) {
			return;
		} else if (DAOFactory.getSelectDAO(dsName).selectOne(t, this.idField) == null) {
			// 检查一下当前对象的ID是否存在于数据库
			return;
		}
		// "update {table} set {idCol} = {newIdVal} where {idCol} = {idVal}
		// ; update {relTable} set {fromCol} = {newIdVal} where {fromCol} = {idVal}"
		String format = "update %s set %s = %s where %s = %s ;";
		for (Field f : fields) {
			Method tarGetter = ru.getGetter(f.getName());
			if (tarGetter == null)
				continue;

			ManyToMany ann = tarGetter.getAnnotation(ManyToMany.class);
			if (ann == null) {
				ann = f.getAnnotation(ManyToMany.class);
				if (ann == null)
					continue;
			}

			JoinTable join = tarGetter.getAnnotation(JoinTable.class);
			if (join == null) {
				join = f.getAnnotation(JoinTable.class);
				if (join == null)
					continue;
			}

			JoinColumn[] froms = join.joinColumns();
			if (froms == null || froms.length == 0)
				continue;

			// 第三方关系表
			String relTable = join.name();
			// 主类在第三方关系表中的字段名
			String from = froms[0].name();

			try {
				// "update {table} set {idCol} = {newIdVal} where {idCol} = {idVal}
				// ; update {relTable} set {fromCol} = {newIdVal} where {fromCol} = {idVal}"
				final String sql1 = String.format(format, table, idColumn, newIdVal, idColumn, idVal);
				final String sql2 = String.format(format, relTable, from, newIdVal, from, idVal);
				Transaction.execute(new Trans() {
					
					@Override
					public void run(Object... args) throws Exception {
						DAOFactory.getUpdateDAO(dsName).updateBySQL(sql1);
						DAOFactory.getUpdateDAO(dsName).updateBySQL(sql2);						
					}
				});
				

			} catch (Exception e) {
				throw new DAOException("", e);
			}
		}
	}
}
