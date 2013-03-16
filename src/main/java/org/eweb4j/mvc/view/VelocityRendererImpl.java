package org.eweb4j.mvc.view;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.mvc.config.MVCConfigConstant;
import org.eweb4j.util.CommonUtil;

/**
 * Velocity模板渲染实现类。
 * @author weiwei
 *
 */
public class VelocityRendererImpl extends Renderer {
	
	public static final Log log = LogFactory.getLogger(VelocityRendererImpl.class);

	public String render(String name, Object value){
		return render(CommonUtil.map(name, value));
	}
	
	public String render(){
		return render(new HashMap<String,Object>());
	}
	
	public String render(Map<String, Object> datas) {
		StringWriter writer = new StringWriter();
		render(writer, datas);
		return writer.toString();
	}

	public void render(Writer writer){
		render(writer, null);
	}
	
	public synchronized void render(Writer writer, Map<String, Object> datas) {
		VelocityContext context = new VelocityContext();
		if (datas != null) {
			for (Iterator<Entry<String, Object>> it = datas.entrySet().iterator(); it.hasNext(); ){
				Entry<String, Object> e = it.next();
				context.put(e.getKey(), e.getValue());
			}
		}
		
		// 初始化Velocity模板引擎
		VelocityEngine ve = (VelocityEngine) SingleBeanCache.get("velocity");
		if (ve == null) {
			File viewsDir = new File(ConfigConstant.ROOT_PATH + MVCConfigConstant.FORWARD_BASE_PATH);
	        Properties p = new Properties();
	        p.setProperty("resource.loader", "file");
	        p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
	        p.setProperty("file.resource.loader.path", viewsDir.getAbsolutePath());
	        p.setProperty("file.resource.loader.cache", "true");
	        p.setProperty("file.resource.loader.modificationCheckInterval", "2");
	        p.setProperty(Velocity.ENCODING_DEFAULT, "GBK");
	        p.setProperty(Velocity.INPUT_ENCODING, "GBK");
	        p.setProperty(Velocity.OUTPUT_ENCODING, "GBK");    
	        ve = new VelocityEngine();
	        ve.init(p);
	        
	        SingleBeanCache.add("velocity", ve);
		}
		
		String tplPath = path;
		
		// 将环境变量和输出部分结合
		if (this.layout != null){
			StringWriter w = new StringWriter();
			ve.getTemplate(path).merge(context, w);
			String screenContent = w.toString();
			context.put(MVCConfigConstant.LAYOUT_SCREEN_CONTENT_KEY, screenContent);
			tplPath = layout;
		}
		
		ve.getTemplate(tplPath).merge(context, writer);
	}
}
