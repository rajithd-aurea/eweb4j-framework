package test.view;

import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.orm.Models;
import org.junit.BeforeClass;
import org.junit.Test;

import test.view.entity.MyView;

public class TestView {

	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null)
			throw new Exception(err);
	}
	
	@Test
	public void test(){
		MyView view = new MyView();
		System.out.println("views-------------->\n\t"+Models.inst(view).findAll());
	}
	
}
