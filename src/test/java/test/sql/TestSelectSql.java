package test.sql;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.sql.SelectSqlCreator;
import org.eweb4j.orm.sql.SqlFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import test.po.Master;
import test.po.Pet;

/**
 * <b>测试SELECT语句</b>
 * 
 * @author CFuture.aw
 * @version 2011-05-10
 * @since 1.a.433
 * 
 */
public class TestSelectSql {
	private static SelectSqlCreator<Pet> select;
	private static Pet pet;
	private static Master master;
	private static String fieldAdd;

	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null) {
			System.out.println(">>>EWeb4J Start Error --> " + err);
			System.exit(-1);
		}
		pet = new Pet();
		master = new Master();
		master.setId(5L);
		pet.setMaster(master);
		pet.setName("xiaobai");
		select = SqlFactory.getSelectSql(pet, "mysql");
		fieldAdd = "+0";

	} 
 
	@Test
	public void testDivPageByMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("table", "t_pet");
		map.put("columns", new String[] { "id", "num", "name", "age", "cate" });
		SelectSqlCreator<Map<String, Object>> select = SqlFactory.getSelectSql(
				map, "mysql");
		String sql = select.divPage(1, 5);
		Assert.assertEquals("SELECT id, num, name, age, cate FROM t_pet  ORDER BY id+0 DESC LIMIT 0, 5 ;", sql);
	}

	@Test
	public void testSelectByField() throws Exception {
		String[] fields = { "master", "name" };
		String sql = select.selectWhere(fields);
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet WHERE master_id  =  '5'  AND name  =  'xiaobai'  ORDER BY id"
						+ fieldAdd + " DESC ;", sql);
		String[] values = { "4", "xiaobai" };
		int likecate = 0;
		boolean isLike = true;
		boolean isNot = true;
		boolean isOR = true;
		String orderField = "id+0";
		int ocate = -1;
		int currentPage = -1;
		int numPerPage = -1;
		sql = select.selectWhere(fields, values, likecate, isLike, isNot, isOR,
				orderField, ocate, currentPage, numPerPage);
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet WHERE master_id  NOT LIKE  '%4%'  OR name  NOT LIKE  '%xiaobai%'  ORDER BY id"
						+ fieldAdd + " DESC ;", sql);
	}

	@Test
	public void testNextOne() {
		pet.setPetId(3L);
		String sql = select.nextOne();
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet WHERE id"
						+ fieldAdd + " > 3 ORDER BY id" + fieldAdd
						+ " ASC LIMIT 1;", sql);
	}

	@Test
	public void testPreOne() {
		pet.setPetId(5L);
		String sql = select.preOne();
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet WHERE id"
						+ fieldAdd + " < 5 ORDER BY id" + fieldAdd
						+ " DESC LIMIT 1;", sql);
	}

	@Test
	public void testDivPageByWhere() {
		String sql = select.divPage(2, 5, "age+0", 1, "xxx = 'ooo'");
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet  WHERE xxx = 'ooo' ORDER BY age"
						+ fieldAdd + " ASC LIMIT 5, 5 ;", sql);
	}

	@Test
	public void testDivPage() {
		String sql = select.divPage(1, 5, "age+0", 1);
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet  ORDER BY age"
						+ fieldAdd + " ASC LIMIT 0, 5 ;", sql);
	}

	@Test
	public void testDivPageOrderByIdField() {
		String sql = select.divPage(1, 5, 1);
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet  ORDER BY id"
						+ fieldAdd + " ASC LIMIT 0, 5 ;", sql);
	}

	@Test
	public void testDivPageByWhereOrderByIdField() {
		String sql = select.divPage(1, 5, 1, "ooo = 'xxx'");
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet  WHERE ooo = 'xxx' ORDER BY id"
						+ fieldAdd + " ASC LIMIT 0, 5 ;", sql);
	}

	@Test
	public void testDivPageOrderByIdFieldDESC() {
		String sql = select.divPage(1, 2);
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet  ORDER BY id"
						+ fieldAdd + " DESC LIMIT 0, 2 ;", sql);
	}

	@Test
	public void testDivPageOrderByIdFieldDESCAndByWhere() {
		String sql = select.divPage(1, 2, "xxx = 'ooo'");
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet  WHERE xxx = 'ooo' ORDER BY id"
						+ fieldAdd + " DESC LIMIT 0, 2 ;", sql);
	}

	@Test
	public void testDivPageByFieldIsValue() {
		String[] fields = new String[] { "name", "age" };
		String[] values = new String[] { "小黑", "4" };

		String sql = select.selectWhere(fields, values, "age+0", 1, 2, 5);
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet  WHERE name  =  '小黑'  AND age  =  '4'  ORDER BY age"
						+ fieldAdd + " ASC LIMIT 5, 5 ;", sql);
	}

	@Test
	public void testDivPageByFieldNotIsValue() {
		String[] fields = new String[] { "name", "age" };
		String[] values = new String[] { "小黑", "4" };

		String sql = select.selectWhereNot(fields, values, "age+0", -1, 1, 2);
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet  WHERE name <> '小黑'  AND age <> '4'  ORDER BY age"
						+ fieldAdd + " DESC LIMIT 0, 2 ;", sql);
	}

	@Test
	public void testDivPageByFieldIsValueByPOJO() {
		String[] fields = new String[] { "name", "age", "master" };
		pet.setName("小白");
		pet.setAge(19);
		Master master = new Master();
		master.setId(8L);
		pet.setMaster(master);
		String sql = select.selectWhere(fields, "age+0", 1, 2, 5);
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet  WHERE name  =  '小白'  AND age  =  '19'  AND master_id  =  '8'  ORDER BY age"
						+ fieldAdd + " ASC LIMIT 5, 5 ;", sql);
	}

	@Test
	public void testDivPageByFieldNotIsValueByPOJO() {
		String[] fields = new String[] { "name", "age" };
		pet.setName("小白");
		pet.setAge(19);
		String sql = select.selectWhereNot(fields, "age+0", 1, 2, 5);
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet  WHERE name <> '小白'  AND age <> '19'  ORDER BY age"
						+ fieldAdd + " ASC LIMIT 5, 5 ;", sql);
	}

	@Test
	public void testDivPageByFieldIsValueOrderByIdField() {
		String[] fields = new String[] { "name", "age" };
		String[] values = new String[] { "小黑", "4" };

		String sql = select.selectWhere(fields, values, 1, 2, 5);
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet  WHERE name  =  '小黑'  AND age  =  '4'  ORDER BY id"
						+ fieldAdd + " ASC LIMIT 5, 5 ;", sql);
	}

	@Test
	public void testDivPageByFieldNotIsValueOrderByIdField() {
		String[] fields = new String[] { "name", "age" };
		String[] values = new String[] { "小黑", "4" };

		String sql = select.selectWhereNot(fields, values, -1, 1, 2);
		Assert.assertEquals(
				"SELECT id, num, name, age, cate, master_id FROM t_pet  WHERE name <> '小黑'  AND age <> '4'  ORDER BY id"
						+ fieldAdd + " DESC LIMIT 0, 2 ;", sql);
	}
}
