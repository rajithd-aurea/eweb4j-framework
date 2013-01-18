package test.sql;

import java.util.HashMap;
import java.util.Map;

import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.sql.Sql;
import org.eweb4j.orm.sql.SqlFactory;
import org.eweb4j.orm.sql.UpdateSqlCreator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import test.po.Master;
import test.po.Pet;

public class TestUpdateSql {
	private static Pet pet;
	private static UpdateSqlCreator<Pet> update;

	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null) {
			System.out.println(">>>EWeb4J Start Error --> " + err);
			System.exit(-1);
		}
		pet = new Pet();
		update = SqlFactory.getUpdateSql(pet);
	}

	@Test
	public void testMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table", "t_pet");
		map.put("idColumn", "id");
		map.put("idValue", 5);
		map.put("columns", new String[] { "num", "name", "age", "type" });
		map.put("values", new Object[] { 123, "weiwei", 3, "dog" });
		Pet pet = new Pet();
		pet.setName("xxx");
		pet.setNumber("4444");
		pet.setAge(3);
		pet.setType("cat");
		UpdateSqlCreator<?> update = SqlFactory.getUpdateSql(map, pet);
		Sql sql = update.update()[0];
		Assert.assertEquals("UPDATE t_pet SET num = ? ,name = ? ,age = ? ,type = ?  WHERE id = '5' ;", sql.sql);
		Assert.assertEquals(123, sql.args.get(0));
		Assert.assertEquals("weiwei", sql.args.get(1));
		Assert.assertEquals(3, sql.args.get(2));
		Assert.assertEquals("dog", sql.args.get(3));
	}

	/**
	 * 修改表记录所有不为null值的字段，通过主键值作为条件
	 * 
	 * @param ts
	 * @return
	 */
	@Test
	public void testUpdate() {
		pet.setPetId(5L);
		pet.setName("小黑");
		pet.setType("dog");
		pet.setAge(1111);
		Master master = new Master();
		master.setId(9L);
		pet.setMaster(master);
		Sql sql = update.update()[0];
		Assert.assertEquals("UPDATE t_pet SET name = ? ,age = ? ,cate = ? ,master_id = ?  WHERE id = '5' ;", sql.sql);
		Assert.assertEquals("小黑", sql.args.get(0));
		Assert.assertEquals(1111, sql.args.get(1));
		Assert.assertEquals("dog", sql.args.get(2));
		Assert.assertEquals(9l, sql.args.get(3));
	}

	/**
	 * 修改表记录所有字段，通过给定condition条件
	 * 
	 * @param condition
	 * @param ts
	 * @return
	 */
	@Test
	public <T> void testUpdateWhere() {

	}

	/**
	 * 修改给定字段
	 * 
	 * @param t
	 *            给定的对象
	 * @param fields
	 *            给定的字段
	 * @return 返回布尔
	 */
	@Test
	public <T> void testUpdateByField() {
		pet.setName("xiaohuang");
		pet.setAge(3);
		pet.setPetId(12L);
		Master master = new Master();
		master.setId(5L);
		pet.setMaster(master);
		String[] fields = { "name", "age", "master" };
		Sql sql = update.update(fields)[0];
		Assert.assertEquals("UPDATE t_pet SET name = ? , age = ? , master_id = ?  WHERE id = '12' ;",sql.sql);
		Assert.assertEquals("xiaohuang", sql.args.get(0));
		Assert.assertEquals(3, sql.args.get(1));
		Assert.assertEquals(5l, sql.args.get(2));
	}

	/**
	 * 修改给定字段为给定值
	 * 
	 * @param <T>
	 * @param clazz
	 * @param fields
	 * @param values
	 * @return
	 */
	@Test
	public <T> void testUpdateByFieldAndValue() {

	}

	/**
	 * 执行给定sql
	 * 
	 * @param <T>
	 * @param clazz
	 * @param sqls
	 * @return
	 */
	@Test
	public <T> void testUpdateBySQL() {

	}

	/**
	 * 执行给定sql,支持？占位符
	 * 
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	@Test
	public <T> void testUpdateBySQLAndArgs() {

	}
}
