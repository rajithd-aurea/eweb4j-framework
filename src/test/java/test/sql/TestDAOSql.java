package test.sql;

import java.util.Collection;

import junit.framework.Assert;

import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.Db;
import org.eweb4j.orm.dao.DAO;
import org.eweb4j.orm.dao.DAOFactory;
import org.eweb4j.util.CommonUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import test.po.Pet;

public class TestDAOSql {
	private static DAO dao;

	@BeforeClass
	public static void prepare() throws Exception {
		EWeb4JConfig.start("start.eweb.xml");
		dao = DAOFactory.getDAO(Pet.class);
	}

	@Test
	public void testSql(){
		String sql = "select * from t_pet where id > ? limit 10";
		Collection<Pet> pets = 
			Db.ar(Pet.class)
				.dao()
				.sql(sql).fillArgs(2)
				.query();
		
		System.out.println("!!!!fuck!!!!!pets=====>" + CommonUtil.toJson(pets));
	}
	
	@Test
	public void testSelectStr() {

		dao.clear();
		String sql = dao.selectStr("petId, name").toSql();
		Assert.assertEquals(" SELECT petId, name FROM  t_pet pet  ", sql);
	}

	@Test
	public void testSelectAll() {
		dao.clear();
		String sql = dao.selectAll().toSql();

		Assert.assertEquals(" SELECT pet.id, pet.num, pet.name, pet.age, pet.cate, pet.master_id, pet.user_id FROM  t_pet pet  ", sql);
	}

	@Test
	public void testSelectFields() {
		dao.clear();
		String[] fields = { "petId", "name" };
		String sql = dao.select(fields).toSql();
		Assert.assertEquals(" SELECT id, name FROM  t_pet pet  ", sql);
	}
	
	@Test
	public void testInsertFields() {
		dao.clear();
		String[] fields = { "petId", "name" };
		String sql = dao.insert(fields).toSql();
		Assert.assertEquals(" INSERT INTO t_pet(id, name) ", sql);
	}
	
	@Test
	public void testValues() {
		dao.clear();
		Object[] values = { 3, "baby" };
		String sql = dao.values(values).toSql();
		Assert.assertEquals(" VALUES(?, ?) ", sql);
	}
	
	@Test
	public void testUpdate() {
		dao.clear();
		String sql = dao.update("petId", "name").set(3, "baby").toSql();
		Assert.assertEquals(" UPDATE t_pet SET id = ? , name = ?  ", sql);
	}
	
	@Test
	public void testDel(){
		dao.clear();
		String sql = dao.delete().toSql();
	    Assert.assertEquals(" DELETE FROM t_pet ", sql);
	}
	
	@Test
	public void testWhere(){
		dao.clear();
		String sql = dao.where().toSql();
	    Assert.assertEquals(" WHERE ", sql);
	}
	
	@Test
	public void testWhereNotIn() {
		dao.clear();
		String sql = dao.where().field("id").notIn(1,2).toSql();
		Assert.assertEquals(" WHERE  id  not in(?, ?) ", sql);
	}
	
	@Test
	public void testOrder(){
		dao.clear();
		dao.desc("id").query(5);
	}
}
