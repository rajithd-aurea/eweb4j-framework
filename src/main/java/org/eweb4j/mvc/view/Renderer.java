package org.eweb4j.mvc.view;

import java.io.File;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.eweb4j.config.ConfigConstant;
import org.eweb4j.mvc.config.MVCConfigConstant;

public abstract class Renderer {

	protected Map<String, String> paths = new HashMap<String, String>();
	protected String layout;
	
	private void checkFile(String path) {
		File f = new File(ConfigConstant.ROOT_PATH + MVCConfigConstant.FORWARD_BASE_PATH + "/" + path);
		if (!f.isFile())
			throw new RuntimeException("file ->" + f.getAbsolutePath() + " is not a file");
		
		if (!f.exists())
			throw new RuntimeException("file ->" + f.getAbsolutePath() + " does not exists");
		
	}
	
	public Renderer target(String _path) {
		String path = _path.trim();
		if (!path.contains("->") && !path.contains(",")){
			String file = path;
			checkFile(file);
			this.paths.put(MVCConfigConstant.LAYOUT_SCREEN_CONTENT_KEY, file);
		} else{
			//处理形如 key1->value1,key2->value2
			//1.split by ,
			String[] pathStrs = path.split(",");
			//继续split by ->
			for (String ps : pathStrs){
				String str = ps.trim();
				String[] kv = str.split("->");
				String key = kv[0].trim();
				String value = kv[1].trim();
				checkFile(value);
				paths.put(key, value);
			}
		}
		
		return this;
	}
	
	public Renderer layout(String _path){
		String path = _path.trim();
		checkFile(path);
		this.layout = path;
		return this;
	}
	
	public abstract String render(Map<String, Object> datas);
	
	public abstract String render(String name, Object value);
	
	public abstract String render();
	
	public abstract void render(Writer writer, Map<String, Object> datas);
	
	public abstract void render(Writer writer);
	
	public abstract String render(Map<String, Object> datas, String template);
	
	public String render(String name, Object value, String template){
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put(name, value);
		return render(datas, template);
	}
}
