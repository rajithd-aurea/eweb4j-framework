package test.sql;

import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.sql.Model2Table;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestModel2Sql {

	@BeforeClass
	public static void prepare() throws Exception {
		EWeb4JConfig.start("start.eweb.xml");
	}

	@Test
	public void test() {
		Model2Table.write("petstore_db");
	}

}
