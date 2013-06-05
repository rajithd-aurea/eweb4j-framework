package test.logger;

import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-4 下午03:20:22
 */
public class TestLogger {

	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null) {
			throw new Exception(err);
		}
	}
	
	@Test
	public void test(){
		Log logger = LogFactory.getLogger(TestLogger.class, true, "debug", "test.log", "1m");
		try {
			int i = 9/0;
			logger.debug("i = " + i);
		}catch(Throwable e){
			logger.error("除法错误", e);
		}
	}
	
}
