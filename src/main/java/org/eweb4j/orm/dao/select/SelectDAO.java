package org.eweb4j.orm.dao.select;

import java.util.List;

import org.eweb4j.orm.dao.DAOException;


/**
 * 简单的select接口
 * @author weiwei
 *
 */
public interface SelectDAO {
	
	/**
	 * <b>查询表中所有记录</b> <code>
	 * List<Pet> result = selectAll(Pet.class,"age",OrderType.ASC_ORDER);
	 * 执行sql：SELECT * FROM Pet ORDER BY age ASC ;
	 * </code>
	 * 
	 * @param <T>
	 *            POJO的类型
	 * @param clazz
	 *            POJO的class对象
	 * @param orderField
	 *            排序字段名
	 * @param orderType
	 *            排序类型
	 * @return 查询成功返回类集否则返回null
	 */
	public <T> List<T> selectAll(Class<T> clazz, String orderField,
			int orderType) throws DAOException;

	/**
	 * <b>查询所有，默认id排序</b> <code>
	 * 	selectAll(Pet.class,OrderType.ASC_ORDER);
	 * 也可以
	 * List<Pet> result = selectAll(Pet.class,1);
	 * 执行sql：SELECT * FROM Pet ORDER BY id ASC ;
	 * </code>
	 * 
	 * @param <T>POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param orderType
	 *            排序类型 1升序，-1降序
	 * @return
	 */
	public <T> List<T> selectAll(Class<T> clazz, int orderType)
			throws DAOException;

	/**
	 * <b>查询所有，默认id排序且降序</b> <code>
	 * List<Pet> result = selectAll(Pet.class);
	 * 执行sql：SELECT * FROM Pet ORDER BY id DESC ;
	 * </code>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @return
	 */
	public <T> List<T> selectAll(Class<T> clazz) throws DAOException;

	/**
	 * <b>查询单个记录，给定属性名作为条件.</b> <code>
	 * pet.setName("小黑");
	 * pet.setAge(5);
	 * pet = selectOne(pet,"name","age");
	 * 执行sql：SELECT * FROM Pet WHERE name = '小黑' AND age = '5' ;
	 * </code>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @param fields
	 *            给定的属性名
	 * @return
	 */
	public <T> T selectOne(T t, String... fields) throws DAOException;
	
	/**
	 * 通过ID获取记录
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public <T> T selectOneById(Class<T> clazz, Number id) throws DAOException;
	
	/**
	 * 通过ID获取记录
	 * @param <T>
	 * @param t
	 * @return
	 * @throws DAOException
	 */
	public <T> T selectOneById(T t) throws DAOException;

	/**
	 * <b>查询单个记录，当确认给定条件是只能够查询一条记录时使用</b> <code>
	 * String[] fields = new String[]{"name","age"};
	 * String[] values = new String[]{"小白","8"};
	 * Pet pet = selectOne(Pet.class,fields,values);
	 * 执行sql：SELECT * FROM Pet WHERE name = '小白' AND age = '8' ;
	 * </code>
	 * 
	 * @param <T>
	 *            POJO的类型
	 * @param clazz
	 *            POJO的class对象
	 * @param fields
	 *            属性名字
	 * @param values
	 *            属性值
	 * @return
	 */
	public <T> T selectOne(Class<T> clazz, String[] fields, String[] values)
			throws DAOException;

	/**
	 * <b>自定义条件子句查询单个记录</b> <code>String condition = "xxx = 'ooo'";
	 * Pet pet = selectOneByWhere(Pet.class, condition);
	 * 执行sql：SELECT * FROM Pet WHERE xxx = 'ooo' ;</code>
	 * 
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @return
	 */
	public <T> T selectOneByWhere(Class<T> clazz, String condition)
			throws DAOException;

	/**
	 * <b>查询某表的记录数</b> <code>
	 * long count = selectCount(Pet.class);
	 * 执行sql：SELECT COUNT(*) FROM Pet ;
	 * </code>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @return 查询出来的记录数
	 */
	public <T> long selectCount(Class<T> clazz) throws DAOException;

	/**
	 * <b>查询某表的记录数(带WHERE条件)</b> <code>
	 * String condition = "xxx = 'ooo'";
	 * long count = selectCount(Pet.class, condition);
	 * 执行sql：SELECT COUNT(*) FROM Pet WHERE xxx = 'ooo' ;
	 * </code>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @return 查询出来的记录数
	 */
	public <T> long selectCount(Class<T> clazz, String condition)
			throws DAOException;

	/**
	 * 支持？占位符。
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @param args
	 * @return
	 * @throws DAOException
	 */
	public <T> long selectCount(Class<T> clazz, String condition,
			Object... args) throws DAOException;

	/**
	 * <b>自定义条件查询</b> <code>
	 * String condition = "xxx = 'ooo'";
	 * 或者
	 * condition = "xxx like '%ooo%'";
	 * 或者
	 * 任何有效的WHERE条件子句
	 * List<Pet> result = selectWhere(Pet.class, condition);
	 * 执行:SELECT * FROM Pet WHERE xxx = 'ooo';
	 * 
	 * 新版支持占位符？，要记得在方法后面添加上参数值
	 * </code>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param condition
	 *            条件 支持占位符？
	 * @param args
	 *            占位符参数值
	 * @return
	 */
	public <T> List<T> selectWhere(Class<T> clazz, String condition,
			Object... args) throws DAOException;

	/**
	 * <b>给定SQL语句查询，支持？占位符</b> <code>
	 * String sql = "select * from pet where name = ? and age > ? ;";
	 * List<Pet> result = selectBySQL(Pet.class,sql,"小黑",5);
	 * 执行sql：select * from pet where name = '小黑' and age > '5' ;
	 * </code>
	 * 
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> List<T> selectBySQL(Class<T> clazz, String sql, Object... args)
			throws DAOException;

}
