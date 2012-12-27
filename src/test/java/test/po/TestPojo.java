package test.po;

import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.Models;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2012-12-26 下午06:57:16
 */
public class TestPojo {

	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null)
			throw new Exception(err);
	}
	
	@Test
	public void test() throws Exception{
		System.out.println(Models.inst(MyPojo.class).findAll());
	}
}
