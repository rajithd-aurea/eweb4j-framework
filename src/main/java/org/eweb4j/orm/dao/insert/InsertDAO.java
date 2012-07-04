package org.eweb4j.orm.dao.insert;

import org.eweb4j.orm.dao.DAOException;

/**
 * InsertDAO 接口
 * 
 * @author cfuture.aw
 * @since v1.a.432
 */
public interface InsertDAO {
	/**
	 * 将若干个POJO的所有属性值插入数据库 例如： <code>
	 *  class Pet{
	 *  	private Integer id;
	 *  	private String name;
	 *  	private int age;
	 *      //此处省略setter和getter方法
	 *  }
	 * 	Pet pet = new Pet();
	 *  pet.setName("小黑");
	 *  pet.setAge(3);
	 *  insert(pet);
	 * </code> 会执行sql:INSERT INTO $table values('小黑','3');
	 * 
	 */
	public <T> Number[] batchInsert(T... ts) throws DAOException;

	/**
	 * 
	 * @param <T>
	 * @param t
	 * @return
	 * @throws DAOException
	 */
	public <T> Number insert(T t) throws DAOException;

	public <T> Number insertBySql(Class<T> clazz, String condition,
			Object... args) throws DAOException;

	/**
	 * 带Where条件子句,将若干个POJO所有属性值插入数据库 <code>
	 * 	class Pet{
	 * 		private Integer id;
	 * 		private String name;
	 * 		private int age;
	 * 		//此处省略setter和getter方法
	 * 	}
	 *  Pet pet = new Pet();
	 *  pet.setName("小白");
	 *  pet.setAge(4);
	 *  insertByCondition("xxx", pet);
	 * </code> 会执行sql:INSERT INTO $table values('小黑','3') WHERE xxx ;
	 * 
	 * @param <T>
	 *            POJO的类型
	 * @param condition
	 *            Where条件
	 * @param ts
	 *            带有数据的POJO,可多个不同类型或同类型
	 * @return 如果插入成功,返回true,否则返回false
	 */
	public <T> Number[] insertByCondition(T[] ts, String condition)
			throws DAOException;

	/**
	 * 给定POJO属性值,插入数据库 <code>
	 * 	class Pet{
	 * 		private Integer id;
	 * 		private String name;
	 * 		private int age;
	 * 		//此处省略setter和getter方法
	 * 	}
	 *  class Master{
	 *      private Integer id;
	 *      private String name;
	 *      private String gender;
	 *  }
	 *  
	 * 	Pet pet = new Pet();
	 *  pet.setName("小黄");
	 *  
	 *  Master master = new Master();
	 *  master.setGender("女");
	 *  
	 *  Object[] objs = new Object[]{master, pet};
	 *  String[] masterFields = new String[]{"gender"};
	 *  String[] petFields = new String[]{"name"};
	 *  String[][] fields = new String[][]{masterFields, petFields};
	 *  insertByFields(objs,masterFields, petFields);
	 * </code>会执行sql:INSERT INTO $masterTable(gender) values('女');INSERT INTO
	 * $petTable(name) values('小黄') ;
	 * 
	 * @param <T>
	 *            POJO的类型
	 * @param ts
	 *            带有数据的POJO,多个不同类型或同类型
	 * @param fields
	 *            按数组下标对应POJO的属性名
	 * @return 如果插入成功,返回true,否则返回false
	 */
	public <T> Number[] insertByFields(T[] ts, String[] fields)
			throws DAOException;

	/**
	 * 给定POJO属性值,插入数据库(重载insertByFields(T[] ts, String[][] fields))
	 * 
	 * @param <T>
	 *            POJO的类型
	 * @param t
	 *            带有数据的POJO
	 * @param field
	 *            POJO的属性名
	 * @return 如果插入成功,返回true,否则返回false
	 * @see com.cfuture08.eweb4j.orm.dao.InsertDAO.insertByFields(T[] ts,
	 *      String[][] fields)
	 */
	public <T> Number insertByField(T t, String... field) throws DAOException;

}
