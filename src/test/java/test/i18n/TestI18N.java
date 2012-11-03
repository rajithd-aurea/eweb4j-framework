package test.i18n;

import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;

import org.eweb4j.cache.Props;
import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.i18n.Lang;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("all")
public class TestI18N {
	
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
		Map map = Props.getMap("Message");
		
		Lang.set(Locale.US);
		Assert.assertEquals("Welcome to eweb4j !", map.get("welcome"));
		
		Lang.set(Locale.SIMPLIFIED_CHINESE);
		Assert.assertEquals("欢迎使用 eweb4j 框架 !", map.get("welcome"));
	}
	
	
}
