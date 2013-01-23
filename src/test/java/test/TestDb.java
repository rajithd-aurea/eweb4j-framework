package test;

import java.util.Arrays;

import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.Db;
import org.eweb4j.orm.config.ORMConfigBeanUtil;
import org.eweb4j.util.ReflectUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import test.po.Master;
import test.po.Pet;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-1-22 下午04:02:15
 */
public class TestDb {
	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null) {
			throw new Exception(err);
		}
	}
	
	public void test() throws Exception{
		String query = ORMConfigBeanUtil.parseQuery("master is null and user = ?", Pet.class);
		System.out.println(query);
		Pet p = Db.ar(Pet.class).find("master = ? and user = ?", 1, 2).first();
		System.out.println(p);
	}
	
	@Test
	public void testOrmUtil() throws Exception{
		String[] fields = new ReflectUtil(Master.class).getFieldsName();
		System.out.println(Arrays.asList(fields));
	}
	
}
