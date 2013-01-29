package test.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.util.CommonUtil;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-1-29 下午04:12:19
 */
public class TestRender {

	@BeforeClass
	public static void prepare() throws Exception {
		String err = EWeb4JConfig.start("start.eweb.xml");
		if (err != null)
			throw new Exception(err);
	}
	
	@Test
	public void testExcelRender() throws Exception {
		List<String> fields  = Arrays.asList("field_1", "field_2","field_1", "field_2", "field_1", "field_2", "field_2", "field_3");
		List<Pojo> pojos = new ArrayList<Pojo>();
		for (int i = 0; i < 5 ; i++) {
			Pojo pojo = new Pojo("website_"+i, 20+i);
			for (String field : fields)
				pojo.count(field, CommonUtil.random(1, 50).longValue());
			pojos.add(pojo);
		}
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("fields", new HashSet<String>(fields));
		data.put("createTime", CommonUtil.getNowTime());
		data.put("pojos", pojos);
//		XLSTransformer trans = new XLSTransformer();
//		String src = ConfigConstant.CONFIG_BASE_PATH + "/deal-statistics-sample.xls";
//		String dest = ConfigConstant.CONFIG_BASE_PATH + "/deal-statistics-sample-output.xls";
//		trans.transformXLS(src, data, dest);
	}
	
	public static class Pojo {
		public String website;
		public Map<String, Long> counter = new HashMap<String, Long>();
		public long total;
		
		public Pojo(){}
		
		public Pojo(String website, long total) {
			super();
			this.website = website;
			this.total = total;
		}
		
		public void count(String field, long count){
			if (counter.containsKey(field)){
				counter.put(field, counter.get(field)+count);
				return ;
			}
			counter.put(field, count);
		}
		
	}
}
