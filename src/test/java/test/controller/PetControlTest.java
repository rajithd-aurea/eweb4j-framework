package test.controller;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eweb4j.cache.ActionConfigBeanCache;
import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.mvc.config.bean.ActionConfigBean;
import org.junit.BeforeClass;
import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

@SuppressWarnings("all")
public class PetControlTest {

	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null) {
			System.out.println(">>>EWeb4J Start Error --> " + err);
			System.exit(-1);
		}
	}

	//@Test
	public void test() throws Exception {
		for (Iterator<Entry<Object, ActionConfigBean>> it = ActionConfigBeanCache
				.getAll().entrySet().iterator(); it.hasNext();) {
			System.out.println(it.next()+"\n");
		}
	}

	@Test
	public void testFreeMarker() throws Exception{
		Configuration cfg = new Configuration();
		// 指定模板从何处加载的数据源，这里设置成一个文件目录。
		cfg.setDirectoryForTemplateLoading(new File("src/test/java/test/ftl"));
		// 指定模板如何检索数据模型
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		
		Map root = new HashMap();
		root.put("user", "Big Joe");
		Map latest = new HashMap();
		root.put("latestProduct", latest);
		latest.put("url", "produces/greenmouse.html");
		latest.put("name", "green mouse");
		
		Template template = cfg.getTemplate("hello.html");
		
		Writer out = new OutputStreamWriter(System.out);
		template.process(root, out);
		out.flush();
	}
	
}
