package test;

import java.util.Collection;

import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.Db;
import org.junit.BeforeClass;
import org.junit.Test;

import test.po.Pet;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-11 下午06:29:08
 */
public class TestAll {

	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null) {
			throw new Exception(err);
		}
	}
	
	public void test(){
		Number rs = 
			Db.ar(Users.class)
				.executeBySql("update #table set #account = ? where #id = ?", "test-acc", 1);
		System.out.println(rs);
		
		Collection<Users> users = 
			Db.ar(Users.class)
				.findBySql("select #account, #password from #table where #id = ?", 1); 
		System.out.println(users);
		
		//postgresql 的分页, 从0开始，取2条
		Collection<Pet> pets = 
			Db.ar(Pet.class)
				.alias("p")
				.join("user", "u")
					.findBySql("select p.* from #p.table p, #u.table u where #p.user = #u.id order by #p.id asc limit ? offset ?", 2, 0);
		
		System.out.println(pets);
	}
}
