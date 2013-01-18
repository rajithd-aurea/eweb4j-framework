package test.dao;

import junit.framework.Assert;

import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.Models;
import org.eweb4j.orm.dao.DAOFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import test.po.Pet;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-1-18 上午09:56:41
 */
public class CRUDTest {

	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null) {
			throw new Exception(err);
		}
	}
	
	public void testCreate() throws Exception{
		Pet pet = new Pet();
		pet.setAge(12);
		pet.setName("i'm laiweiwei");
		boolean flag = Models.inst(pet).create("age", "name");
		Assert.assertTrue(flag);
		Assert.assertTrue(pet.getPetId() > 0);
		Assert.assertEquals(12, pet.getAge());
		Assert.assertEquals("i'm laiweiwei", pet.getName());
	}
	
	public void testUpdate() throws Exception{
		Pet pet = new Pet();
		pet.setPetId(2);
		pet.setAge(12);
		pet.setName("i'm laiweiwei too");
		Models.inst(pet).save("age", "name");
		Assert.assertEquals(2, pet.getPetId());
		Assert.assertEquals(12, pet.getAge());
		Assert.assertEquals("i'm laiweiwei too", pet.getName());
	}
	
	@Test
	public void testSelect() throws Exception{
		Pet pet = DAOFactory.getDAO(Pet.class)
			.selectAll()
			.where()
				.field("name").equal("i'm laiweiwei")
			.queryOne();
		Assert.assertEquals(1, pet.getPetId());
		Assert.assertEquals(12, pet.getAge());
		Assert.assertEquals("i'm laiweiwei", pet.getName());
	}
}
