package org.eweb4j.mvc;

import java.io.File;

import org.eweb4j.config.ConfigConstant;
import org.eweb4j.mvc.config.MVCConfigConstant;
import org.eweb4j.util.FileUtil;

public class VelocityEngineImpl implements TemplateEngine{

	private String content = null;
	
	public void loadFile(String path) {
		File f = new File(ConfigConstant.ROOT_PATH + MVCConfigConstant.FORWARD_BASE_PATH + "/" + path);
		if (!f.isFile())
			throw new RuntimeException("file ->" + f.getAbsolutePath() + " is not a file");
		
		if (!f.exists())
			throw new RuntimeException("file ->" + f.getAbsolutePath() + " does not exists");
		
		this.content = FileUtil.readFile(f);
	}

	public void loadContent(String content) {
		this.content = content;
	}

	public String parse() {
		
		return null;
	}

}
