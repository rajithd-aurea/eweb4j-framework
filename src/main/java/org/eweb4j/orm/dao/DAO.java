package org.eweb4j.orm.dao;

import java.util.List;
import java.util.Map;

/**
 * 这是一个通用的非常灵活的数据库操作接口。 使用方法： <code>
 *     <pre>
 * 	       1.先实例化DAO接口，默认实现类是@see org.eweb4j.orm.dao.DAOImpl
 *         2.DAO dao = DAOFactory.getDAOImpl({TargetEntityClass},{dsName});
 *         3.dao.clear()
 *         4.dao.xxx().toSql()这种是输出拼凑好的sql语句。
 *         5.dao.xxx().execute()这种是执行sql语句，一般用于添加、修改、删除语句
 *         6.dao.xxx().query()这种是执行sql语句，不过是查询语句，返回多个结果
 *         7.dao.xxx().queryOne()这种是查询单个结果
 *         8.dao.xxx().count()这种是计算表的行数，返回long类型
 *         8.dao.xxx()可以多种多样的操作，每个方法返回都是DAO自己，可以执行链式的编程。
 *         9.dao.selectAll().field("id").equal(3).query() = select * from table where id = '3'
 *     </pre>
 * <code>
 * 
 * @version 1.8
 * @since JDK 1.5
 * @author weiwei
 * 
 */
public interface DAO {
	/**
	 * <code>
	 * <pre>
	 * Entity
	 * Table(name="t_pet")
	 * class Pet{
	 *     Id
	 *     Column("id")
	 *     petId;
	 *     name;
	 * }
	 *  DAO dao = DAOFactory.getDAO(Pet.class);
	 * 	dao.clear();
	 * 	String sql = dao.selectStr("petId, name").toSql();
	 * 	Assert.assertEquals(" SELECT petId, name FROM t_pet ", sql);
	 * </pre>
	 * </code>
	 * 
	 * @param str
	 *            查询语句后面的字符，select {这段}
	 * @return
	 */
	public DAO selectStr(String str);
	
	/**
	 * <code>
	 * <pre>
	 * Entity
	 * Table(name="t_pet")
	 * class Pet{
	 *     Id
	 *     Column("id")
	 *     petId;
	 *     name;
	 * }
	 *  DAO dao = DAOFactory.getDAO(Pet.class);
	 * 	dao.clear();
	 * 	String sql = dao.selectAll().toSql();
	 * 	Assert.assertEquals(" SELECT * FROM t_pet ", sql);
	 * </pre>
	 * </code>
	 * 
	 * @return
	 */
	public DAO selectAll();


	/**
	 * <code>
	 * <pre>
	 * Entity
	 * Table(name="t_pet")
	 * class Pet{
	 *     Id
	 *     Column("id")
	 *     petId;
	 *     name;
	 * }
	 *  DAO dao = DAOFactory.getDAO(Pet.class);
	 * 	dao.clear();
	 *  String[] fields = {"petId", "name"};
	 * 	String sql = dao.select(fields).toSql();
	 * 	Assert.assertEquals(" SELECT id, name FROM t_pet ", sql);
	 *  //这种使用field的方式最终生成的sql中的的字段名是根据@Column中的配置来的。
	 * 
	 * </pre>
	 * </code>
	 * 
	 * @param fields
	 * @return
	 */
	public DAO select(String... fields);

	/**
	 * 这个方法需要搭配values(Object[] values)方法一起使用。
	 * dao.insert(String[]).values(Object[]).execute() <code>
	 * <pre>
	 * Entity
	 * Table(name="t_pet")
	 * class Pet{
	 *     Id
	 *     Column("id")
	 *     petId;
	 *     name;
	 * }
	 *  DAO dao = DAOFactory.getDAO(Pet.class);
	 * 	dao.clear();
	 *  String[] fields = {"petId", "name"};
	 * 	String sql = dao.insert(fields).toSql();
	 * 	Assert.assertEquals(" INSERT INTO t_pet(id, name) ", sql);
	 *  //这种使用field的方式最终生成的sql中的的字段名是根据@Column中的配置来的。
	 * 
	 * </pre>
	 * </code>
	 * 
	 * @param fields
	 *            需要插入的字段
	 * @return
	 */
	public DAO insert(String... fields);

	public DAO insert(Map<String, Object> map);

	/**
	 * 这个方法需要搭配insert(String[] fields)方法一起使用。
	 * dao.insert(String[]).values(Object[]).execute() <code>
	 * <pre>
	 * Entity
	 * Table(name="t_pet")
	 * class Pet{
	 *     Id
	 *     Column("id")
	 *     petId;
	 *     name;
	 * }
	 *  DAO dao = DAOFactory.getDAO(Pet.class);
	 * 	dao.clear();
	 *  Object[] values = { 3, "baby" };
	 * 	String sql = dao.values(values).toSql();
	 * 	Assert.assertEquals(" VALUES('3', 'baby') ", sql);
	 * 
	 * </pre>
	 * </code>
	 * 
	 * @param values
	 * @return
	 */
	public DAO values(Object... values);

	/**
	 * 这个方法需要搭配set(String[] fields, Object[] values)方法一起使用。
	 * dao.update().set(String[] fields, Object[]).execute() <code>
	 * <pre>
	 * Entity
	 * Table(name="t_pet")
	 * class Pet{
	 *     Id
	 *     Column("id")
	 *     petId;
	 *     name;
	 * }
	 *  DAO dao = DAOFactory.getDAO(Pet.class);
	 * 	dao.clear();
	 *  String sql = dao.update().toSql();
	 * 	Assert.assertEquals(" UPDATE t_pet ", sql);
	 * 
	 * </pre>
	 * </code>
	 * 
	 * @return
	 */
	public DAO update();

	/**
	 * 这个方法需要搭配set(String[] fields, Object[] values)方法一起使用。
	 * dao.update().set(String[] fields, Object[]).execute() <code>
	 * <pre>
	 * Entity
	 * Table(name="t_pet")
	 * class Pet{
	 *     Id
	 *     Column("id")
	 *     petId;
	 *     name;
	 * }
	 *  DAO dao = DAOFactory.getDAO(Pet.class);
	 * 	dao.clear();
	 *  String[] fields = {"petId", "name"};
	 *  Object[] values = {3, "baby"};
	 *  String sql = dao.set(fields, values).toSql();
	 * 	Assert.assertEquals(" UPDATE t_pet ", sql);
	 * 
	 * </pre>
	 * </code>
	 * 
	 * @param fields
	 * @param values
	 * @return
	 */
	public DAO set(String[] fields, Object... values);

	public DAO set(Map<String, Object> map);

	/**
	 * 
	 * <code>
	 * <pre>
	 * Entity
	 * Table(name="t_pet")
	 * class Pet{
	 *     Id
	 *     Column("id")
	 *     petId;
	 *     name;
	 * }
	 *  DAO dao = DAOFactory.getDAO(Pet.class);
	 * 	dao.clear();
	 *  String sql = dao.delete().toSql();
	 * 	Assert.assertEquals(" DELETE from t_pet ", sql);
	 * 
	 * </pre>
	 * </code>
	 * 
	 * @return
	 */
	public DAO delete();

	/**
	 * 经常跟field(String)和equal(Object)等连用。
	 * dao.selectAll().where().field("xxx").equal("ooo")... <code>
	 * <pre>
	 * Entity
	 * Table(name="t_pet")
	 * class Pet{
	 *     Id
	 *     Column("id")
	 *     petId;
	 *     name;
	 * }
	 *  DAO dao = DAOFactory.getDAO(Pet.class);
	 * 	dao.clear();
	 *  String sql = dao.where().toSql();
	 * 	Assert.assertEquals(" WHERE ", sql);
	 * 
	 * </pre>
	 * </code>
	 * 
	 * @return
	 */
	public DAO where();
	
	public DAO isNull();
	
	public DAO isNotNull();

	/**
	 * 就单纯的一个append字符串的操作
	 * 
	 * @param str
	 * @return
	 */
	public DAO append(String str);

	/**
	 * 经常给where还有equal，in，notIn，等等一起用。作为条件子句要用到字段的时候。
	 * where().field("xxx").equal("ooo")...
	 * 
	 * @param fieldName
	 * @return
	 */
	public DAO field(String fieldName);

	/**
	 * 跟equal一样是一个连接操作符 "not like"
	 * 
	 * @param value
	 * @return
	 */
	public DAO notLike(Object value);

	/**
	 * 就是sql中的 "<>"
	 * 
	 * @param value
	 * @return
	 */
	public DAO notEqual(Object value);

	/**
	 * 就是sql中的 like 'xxx%'
	 * 
	 * @param value
	 * @return
	 */
	public DAO likeLeft(Object value);

	/**
	 * 就是sql中的 like '%xxx'
	 * 
	 * @param value
	 * @return
	 */
	public DAO likeRight(Object value);

	/**
	 * 就是sql中的 like '%xxx%'
	 * 
	 * @param value
	 * @return
	 */
	public DAO like(Object value);

	/**
	 * 就是sql中的 = 'xxx'
	 * 
	 * @param value
	 * @return
	 */
	public DAO equal(Object value);

	/**
	 * 就是sql中的大于 > 'xxx'
	 * 
	 * @param value
	 * @return
	 */
	public DAO moreThan(Object value);

/**
	 * 就是sql中的小于   <  'xxx'
	 * @param value
	 * @return
	 */
	public DAO lessThan(Object value);

	/**
	 * 就是sql中的 or xxx
	 * 
	 * @param field
	 * @return
	 */
	public DAO or(String field);

	/**
	 * 就是sql中的 and xxx
	 * 
	 * @param field
	 * @return
	 */
	public DAO and(String field);

	/**
	 * 就是sql中的降序 order by xxx desc
	 * 
	 * @param field
	 * @return
	 */
	public DAO desc(String field);

	/**
	 * 就是sql中的升序 order by xxx asc
	 * 
	 * @param field
	 * @return
	 */
	public DAO asc(String field);

	/**
	 * 就是sql中的 in(xxx,xxx,xxx)
	 * 
	 * @param values
	 * @return
	 */
	public DAO in(Object... values);

	/**
	 * 就是sql中的 not in(xxx,xxx,xxx)
	 * 
	 * @param values
	 * @return
	 */
	public DAO notIn(Object... values);

	/**
	 * 就是sql中的 in(sql语句)
	 * 
	 * @param sql
	 * @return
	 */
	public DAO inSql(String sql);

	/**
	 * 就是sql中的 not in(sql语句)
	 * 
	 * @param sql
	 * @return
	 */
	public DAO notInSql(String sql);

	/**
	 * 若在此之前执行dao其他操作的时候使用了占位符 "?" 的话， 使用此方法来填充这些 占位符 参数。
	 * selectAll().where().field("name").equal("?").fillArgs({"baby"});
	 * 
	 * @param args
	 * @return
	 */
	public DAO fillArgs(Object... args);

	/**
	 * 清除sql语句缓存，一般在每次需要重新构建sql语句的时候使用
	 * 
	 * @return
	 */
	public DAO clear();

	/**
	 * 将当前sql缓冲区的sql语句打印输出
	 * 
	 * @return
	 */
	public String toSql();

	/**
	 * 执行查询操作使用，返回多个结果
	 * 
	 * @param <T>
	 * @return
	 */
	public <T> List<T> query();
	
	public <T> List<T> query(int max);
	
	public <T> List<T> query(int page, int length);

	/**
	 * 执行查询操作使用，返回第一个结果
	 * 
	 * @param <T>
	 * @return
	 */
	public <T> T queryOne();
	
	/**
	 * 计算记录数
	 * @return
	 */
	public long count();

	/**
	 * 执行sql语句，用于查询之外的语句执行，若是插入操作，返回最新的ID，若不是返回影响行数
	 * 
	 * @return
	 */
	public int execute();

	/**
	 * 设置一个表名，这种一般用在没有映射对象的时候。 例如没有Pet对象，但是想查询数据库， 这时候可以使用Map.class对象来查询。
	 * 这时候需要设置一个表名。 dao = DAOFactory.getDAO(Map.class); dao.setTable("t_pet");
	 * List<Map<String,String>> list = dao.selectAll().query();
	 * 
	 * @param table
	 * @return
	 */
	public DAO setTable(String table);
	
	/**
	 * 设置Class
	 * @param clazz
	 * @return
	 */
	public DAO setClass(Class<?> clazz);
	
	/**
	 * 设置Map实例
	 * @param map
	 * @return
	 */
	public DAO setMap(Map<String,Object> map);
	
	public String getDsName();

}
