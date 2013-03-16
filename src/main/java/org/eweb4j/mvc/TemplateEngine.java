package org.eweb4j.mvc;

public interface TemplateEngine {

	public void loadFile(String path);
	
	public void loadContent(String content);
	
	public String parse();
	
}
