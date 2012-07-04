package org.eweb4j.orm.dao.cascade;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.eweb4j.util.ReflectUtil;


/**
 * 级联操作接口
 * @author weiwei
 *
 */
public class CascadeDAO {
	private ToOneDAO oneToOne = null;
	private OneToManyDAO oneToMany = null;
	private ManyToManyDAO manyToMany = null;

	public CascadeDAO(String dsName) {
		this.oneToOne = new ToOneDAO(dsName);
		this.oneToMany = new OneToManyDAO(dsName);
		this.manyToMany = new ManyToManyDAO(dsName);
	}
	
	/**
	 * 对pojo的所有属性进行级联查询操作
	 * 
	 * @param <T>
	 * @param t
	 *            @
	 */
	public <T> void select(T[] t) {
		cascade(t, null, CascadeType.SELECT);
	}

	public <T> void select(T t) {
		cascade(new Object[] { t }, null, CascadeType.SELECT);
	}

	/**
	 * 对pojo的所有属性进行级联删除操作
	 * 
	 * @param <T>
	 * @param t
	 *            @
	 */
	public <T> void delete(T[] t) {
		cascade(t, null, CascadeType.DELETE);
	}

	/**
	 * 对所有的关联属性进行级联插入
	 * 
	 * @param <T>
	 * @param t
	 *            @
	 */
	public <T> void insert(T[] t) {
		cascade(t, null, CascadeType.INSERT);
	}

	public <T> void insert(T t) {
		cascade(new Object[] { t }, null, CascadeType.INSERT);
	}

	/**
	 * 对所有关联的属性进行级联更新
	 * 
	 * @param <T>
	 * @param t
	 *            @
	 */
	public <T> void update(T[] t, long newIdVal) {
		cascade(t, null, CascadeType.UPDATE, newIdVal);
	}

	public <T> void update(T t, long newIdVal) {
		update(new Object[] { t }, newIdVal);
	}

	/**
	 * 对pojo指定的属性进行级联查询操作
	 * 
	 * @param <T>
	 * @param t
	 * @param fieldName
	 *            @
	 */
	public <T> void select(T[] t, String fieldName) {
		cascade(t, new String[] { fieldName }, CascadeType.SELECT);
	}

	public <T> void select(T t, String fieldName) {
		cascade(new Object[] { t }, new String[] { fieldName },
				CascadeType.SELECT);
	}

	/**
	 * 对pojo指定的属性进行级联删除操作
	 * 
	 * @param <T>
	 * @param t
	 * @param fieldName
	 *            @
	 */
	public <T> void delete(T[] t,  String fieldName) {
		delete(t, new String[] { fieldName });
	}

	public <T> void delete(T t,  String fieldName) {
		delete(new Object[] { t }, new String[] { fieldName });
	}

	/**
	 * 对指定的关联属性级联插入
	 * 
	 * @param <T>
	 * @param t
	 * @param fieldName
	 *            @
	 */
	public <T> void insert(T[] t, String fieldName) {
		cascade(t, new String[] { fieldName }, CascadeType.INSERT);
	}

	public <T> void insert(T t, String fieldName) {
		cascade(new Object[] { t }, new String[] { fieldName },
				CascadeType.INSERT);
	}

	/**
	 * 对指定的关联属性级联更新
	 * 
	 * @param <T>
	 * @param t
	 * @param fieldName
	 *            @
	 */
	public <T> void update(T[] t, String fieldName, long newIdVal) {
		cascade(t, new String[] { fieldName }, CascadeType.UPDATE, newIdVal);
	}

	public <T> void update(T t, String fieldName, long newIdVal) {
		cascade(new Object[] { t }, new String[] { fieldName }, CascadeType.UPDATE, newIdVal);
	}

	/**
	 * 对pojo指定的属性进行级联查询操作
	 * 
	 * @param <T>
	 * @param t
	 * @param fieldNames
	 *            @
	 */
	public <T> void select(T[] t, String... fieldNames) {
		cascade(t, fieldNames, CascadeType.SELECT);
	}

	public <T> void select(T t, String... fieldNames) {
		cascade(new Object[] { t }, fieldNames, CascadeType.SELECT);
	}

	/**
	 * 对pojo指定的属性进行级联删除操作
	 * 
	 * @param <T>
	 * @param t
	 * @param fieldName
	 *            @
	 */
	public <T> void delete(T[] t,  String... fieldNames) {
		cascade(t, fieldNames, CascadeType.DELETE);
	}

	public <T> void delete(T t,  String... fieldNames) {
		cascade(new Object[] { t }, fieldNames, CascadeType.DELETE);
	}

	/**
	 * 对指定的关联属性级联插入
	 * 
	 * @param <T>
	 * @param t
	 * @param fieldNames
	 *            @
	 */
	public <T> void insert(T[] t, String... fieldNames) {
		cascade(t, fieldNames, CascadeType.INSERT);
	}

	public <T> void insert(T t, String... fieldNames) {
		cascade(new Object[] { t }, fieldNames, CascadeType.INSERT);
	}

	/**
	 * 对指定的关联属性级联更新
	 * 
	 * @param <T>
	 * @param ts
	 * @param fieldNames
	 *            @
	 */
	public <T> void update(T[] t, long newIdVal, String... fieldNames) {
		cascade(t, fieldNames, CascadeType.UPDATE, newIdVal);
	}

	public <T> void update(T t, long newIdVal, String... fieldNames) {
		cascade(new Object[] { t }, fieldNames, CascadeType.UPDATE, newIdVal);
	}

	public <T> void cascade(T[] ts, String[] fieldNames, int type) {
		cascade(ts, fieldNames, type, -1);
	}
	
	/**
	 * 级联
	 * 
	 * @param <T>
	 * @param t
	 * @param fieldNames
	 *            对指定属性名进行级联操作，当为null时，对所有属性进行级联操作
	 * @param type
	 *            1表示级联查询，2表示级联删除，3表示级联插入，4表示级联更新
	 * @param delType
	 *            @
	 */
	public <T> void cascade(T[] ts, String[] fieldNames, int type, long newIdVal) {
		// 首先判断给定的pojo是否是一个持久化对象
		if (ts == null || ts.length == 0) {
			return;
		}
		for (T t : ts) {
			if (t == null)
				continue;
			Class<?> clazz = t.getClass();
			String clsName = clazz.getSimpleName();
			Entity entity = clazz.getAnnotation(Entity.class);
			if (entity == null && !clsName.endsWith("PO")
					&& !clsName.endsWith("POJO") && !clsName.endsWith("Entity")
					&& !clsName.endsWith("Model")) {
				
				return ;
			}
			
			ReflectUtil ru = new ReflectUtil(t);
			Field[] fields = null;
			if (fieldNames == null || fieldNames.length == 0) {
				fields = ru.getFields();
			} else {
				List<Field> fieldList = new ArrayList<Field>();
				for (String n : fieldNames) {
					if (n != null && !"".equals(n.trim())) {
						Field f = ru.getField(n);
						if (f != null) {
							fieldList.add(f);
						}
					}
				}
				if (fieldList.size() > 0) {
					fields = fieldList.toArray(new Field[] {});
				}
			}
			if (fields != null) {
				List<Field> oneList = new ArrayList<Field>();
				List<Field> manyList = new ArrayList<Field>();
				List<Field> manyManyList = new ArrayList<Field>();
				for (Field f : fields) {
					Method getter = ru.getGetter(f.getName());

					if (f.isAnnotationPresent(OneToMany.class)
							|| (getter != null && getter.isAnnotationPresent(OneToMany.class))){
						manyList.add(f);
					} else if (f.isAnnotationPresent(ManyToOne.class)
							|| (getter != null && getter.isAnnotationPresent(ManyToOne.class))){
						oneList.add(f);
					}else if (f.isAnnotationPresent(OneToOne.class)
							|| (getter != null && getter.isAnnotationPresent(OneToOne.class))){
						oneList.add(f);
					}else if (f.isAnnotationPresent(ManyToMany.class)
							|| (getter != null && getter.isAnnotationPresent(ManyToMany.class))){
						manyManyList.add(f);
					}
					
				}

				this.manyToMany.init(t, manyManyList);
				this.oneToMany.init(t, manyList);
				this.oneToOne.init(t, oneList);
				switch (type) {
				case CascadeType.SELECT:
					this.manyToMany.select();
					this.oneToMany.select();
					this.oneToOne.select();
					break;
				case CascadeType.DELETE:
					this.manyToMany.delete();
					this.oneToMany.delete();
					break;
				case CascadeType.INSERT:
					this.manyToMany.insert();
					this.oneToMany.insert();
					break;
				case CascadeType.UPDATE:
					this.oneToMany.update(newIdVal);
					this.manyToMany.update(newIdVal);
					break;
				}
			}
		}

	}
}
