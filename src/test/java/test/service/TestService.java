package test.service;

import java.util.List;

import org.eweb4j.orm.dao.DAOFactory;
import org.eweb4j.orm.jdbc.transaction.Trans;
import org.eweb4j.orm.jdbc.transaction.Transaction;

import test.po.Pet;


public class TestService {

	// 测试事务
	public String testTrans(Pet _pet) {
		String error = null;

		// 事务模板
		Transaction.execute(new Trans() {
			@Override
			public void run(Object... args) throws Exception {
				// some eweb4j dao
			}
		}, 1, 2);

		List<Pet> pets;

		pets = DAOFactory.getSelectDAO().selectAll(Pet.class);
		if (pets != null) {
		}

		return error;
	}
}
