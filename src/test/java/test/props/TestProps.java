package test.props;

import junit.framework.Assert;

import org.eweb4j.cache.Props;
import org.eweb4j.config.EWeb4JConfig;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestProps {
	
	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null){
			System.out.println(">>>EWeb4J Start Error --> " + err);
			System.exit(-1);
		}
	}

	@Test
	public void testVarable() throws Exception{
		Assert.assertEquals("eweb4j/hello", Props.get("TEST"));
	}
}
