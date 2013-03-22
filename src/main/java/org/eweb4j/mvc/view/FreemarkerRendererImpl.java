package org.eweb4j.mvc.view;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.mvc.config.MVCConfigConstant;
import org.eweb4j.util.CommonUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerRendererImpl extends Renderer{

	public String render(String name, Object value){
		return render(CommonUtil.map(name, value));
	}
	
	public String render(Map<String, Object> datas) {
		StringWriter writer = new StringWriter();
		render(writer, datas);
		return writer.toString();
	}
	
	public String render(){
		return render(new HashMap<String,Object>());
	}
	
	public void render(Writer writer){
		render(writer);
	}
	
	public synchronized void render(Writer writer, Map<String, Object> datas) {
		if (datas == null)
			datas = new HashMap<String, Object>();
		
		try {
			Configuration cfg = (Configuration) SingleBeanCache.get("freemarker");
			if (cfg == null){
				//初始化
				cfg = new Configuration();
				// 指定模板从何处加载的数据源，这里设置成一个文件目录。
				cfg.setDirectoryForTemplateLoading(new File(ConfigConstant.ROOT_PATH + MVCConfigConstant.FORWARD_BASE_PATH));
				// 指定模板如何检索数据模型
				cfg.setObjectWrapper(new DefaultObjectWrapper());
				cfg.setDefaultEncoding("UTF-8");
				SingleBeanCache.add("freemarker", cfg);
			}
			
			String tplPath = paths.get(MVCConfigConstant.LAYOUT_SCREEN_CONTENT_KEY);
			
			// 将环境变量和输出部分结合
			if (this.layout != null){
				
				for (Iterator<Entry<String, String>> it = this.paths.entrySet().iterator(); it.hasNext(); ){
					Entry<String, String> e = it.next();
					String paramName = e.getKey();
					String path = e.getValue();
					
					StringWriter w = new StringWriter();
					Template template = cfg.getTemplate(path);
					template.setEncoding("UTF-8");
					template.process(datas, w);
					String screenContent = w.toString();
					
					datas.put(paramName, screenContent);
				}
				
				tplPath = layout;
			}
			
			Template template = cfg.getTemplate(tplPath);
			template.setEncoding("UTF-8");
			template.process(datas, writer);
		} catch (Exception e){
			throw new RuntimeException(e);
		}

	}

}
