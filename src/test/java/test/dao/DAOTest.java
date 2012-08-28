package test.dao;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.dao.DAO;
import org.eweb4j.orm.dao.DAOFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import test.po.Pet;


public class DAOTest {
	private static DAO dao = null;

	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null) {
			System.out.println(">>>EWeb4J Start Error --> " + err);
			System.exit(-1);
		}

		System.out.println(ConfigConstant.START_FILE_PATH());
		dao = DAOFactory.getDAO(Map.class);
	}

	@Test
	public void test() throws Exception {
		dao.setTable("t_pet");
		
		Map<String, Object> map = dao.selectAll().queryOne();
		if (map == null)
			return ;
		
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			System.out.print(key + "(" + value + "), ");
		}
		// List<Map<String, Object>> maps = dao.selectAll().where()
		// .field("department_id").equal(8).and("user_id").equal(126)
		// .query();
		//
		// for (Map<String, Object> m : maps) {
		// for (Entry<String, Object> entry : m.entrySet()) {
		// String key = entry.getKey();
		// Object value = entry.getValue();
		// System.out.print(key + "(" + value + "), ");
		// }
		// System.out.println("");
		// }
	}
	
	@Test
	public void testDAO(){
		DAO dao = DAOFactory.getDAO(Pet.class);
		Collection<Pet> pets = dao.clear().selectAll().query(1,4);
		System.out.println(pets);
	}
}
