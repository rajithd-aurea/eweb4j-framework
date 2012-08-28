package test;

import java.util.Collection;
import java.util.List;

import org.eweb4j.cache.ActionConfigBeanCache;
import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.mvc.validator.annotation.Required;
import org.junit.Test;

public class UserControl {
	
	@Required
	private String name;
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	public String doHelloAtGet(){
		return "hello, "+name;
	}

	@Test
	public void testHello() throws Exception{
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null)
			throw new Exception(err);
		
		System.out.println(ActionConfigBeanCache.get("user/hello#GET"));
	}
	

	//@Test
	public void test() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null)
			throw new Exception(err);
		User user = new User();
		user.setId(5L);
		user.load();/* load 加载数据，默认从内存，找不到就从数据库查询 */
		user.delete();
		user.save();/* save(insert or update 当id不为空的时候是update) */
		
		/* insert */
		user.setId(0);
		user.setAccount("xxxx");
		user.setPassword("321456");
		user.create();
		
		user.cascade().merge("master");
		user.cascade().persist("master");
		user.cascade().refresh(222, "master");
		user.cascade().remove("master");
		
		List<User> users = User.inst.findAll();
		/* 分页 */
		Collection<User> page = User.inst.find().fetch(10);
		page = User.inst.find().fetch(2, 5);
		User u = User.inst.find("account = ?", "test").first();
		u = User.inst.findById(3);
		u = User.inst.find("byAccountAndPassword", "test", "123456").first();
		u.delete("byAccount", "test");
		u.deleteAll();
		

		User.inst.count();/* select count(*) */
		/* count(*) ... where account='test' */
		User.inst.count("byAccount", "test");
		
		User.inst.dao().insert("","").values("",3).execute();
		User.inst.dao().update().set(new String[]{"name", "age"}, "test", 4).execute();
		User.inst.dao().delete().execute();
		
		System.out.println(users);
		System.out.println(page);
		System.out.println(u);
	}
}
