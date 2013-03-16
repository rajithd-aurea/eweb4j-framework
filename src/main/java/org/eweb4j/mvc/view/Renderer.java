package org.eweb4j.mvc.view;

import java.io.File;
import java.io.Writer;
import java.util.Map;

import org.eweb4j.config.ConfigConstant;
import org.eweb4j.mvc.config.MVCConfigConstant;

public abstract class Renderer {

	protected String path;
	protected String layout;
	
	private void checkFile(String path) {
		File f = new File(ConfigConstant.ROOT_PATH + MVCConfigConstant.FORWARD_BASE_PATH + "/" + path);
		if (!f.isFile())
			throw new RuntimeException("file ->" + f.getAbsolutePath() + " is not a file");
		
		if (!f.exists())
			throw new RuntimeException("file ->" + f.getAbsolutePath() + " does not exists");
		
	}
	
	public Renderer target(String path) {
		checkFile(path);
		this.path = path;
		return this;
	}
	
	public Renderer layout(String path){
		checkFile(path);
		this.layout = path;
		return this;
	}
	
	public abstract String render(Map<String, Object> datas);
	
	public abstract String render(String name, Object value);
	
	public abstract String render();
	
	public abstract void render(Writer writer, Map<String, Object> datas);
	
	public abstract void render(Writer writer);
}
