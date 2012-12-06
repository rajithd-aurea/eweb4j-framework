package test.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.dao.DAOFactory;
import org.eweb4j.orm.jdbc.transaction.Trans;
import org.eweb4j.orm.jdbc.transaction.Transaction;
import org.junit.BeforeClass;
import org.junit.Test;

import test.po.Pet;

public class SelectDAOTest {
	
	
	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null) {
			System.out.println(">>>EWeb4J Start Error --> " + err);
			System.exit(-1);
		}
	}

	@Test
	public void testMap() {
		Transaction.execute(new Trans() {

			@Override
			public void run(Object... args) throws Exception {
				Pet pet = Pet.inst.find().first();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("table", "t_pet");
				map.put("idColumn", "id");
				map.put("idValue", pet.getId());
				map.put("columns",
						new String[] { "num", "name", "age", "cate" });
				map.put("values", new Object[] { 123, "weiwei", 3, "dog" });
				DAOFactory.getUpdateDAO().update(map);
				DAOFactory.getInsertDAO().insert(map);
				DAOFactory.getDeleteDAO().deleteById(map);

				throw new Exception("just for test");
			}
		});

	}

	@Test
	public void testSelect() throws Exception {
		List<Pet> pets = DAOFactory.getSelectDAO().selectAll(Pet.class);
		if (pets != null) {
			DAOFactory.getCascadeDAO().select(pets.toArray(new Pet[] {}));
			for (Pet pet : pets) {
				System.out.println(pet);
			}
		}
	}
}
