package org.eweb4j.config;

import org.eweb4j.util.FileUtil;

/**
 * EWeb4J配置常量
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class ConfigConstant {
	public static String CLASS_PATH = FileUtil.getTopClassPath(ConfigConstant.class);
	public static String ROOT_PATH = FileUtil.getParent(CLASS_PATH, 2);
	public static String CONFIG_BASE_PATH = FileUtil.getParent(CLASS_PATH, 0); //
	public static String START_FILE_NAME = "eweb4j-start-config.xml";
	
	public static String CONFIG_BASE_PATH(){
		CONFIG_BASE_PATH = CONFIG_BASE_PATH.replace("${RootPath}", ROOT_PATH);
		
		return CONFIG_BASE_PATH;
	}
	
	public static String START_FILE_PATH() {
		CONFIG_BASE_PATH = CONFIG_BASE_PATH.replace("${RootPath}", ROOT_PATH);
		return CONFIG_BASE_PATH + START_FILE_NAME;
	}
	
	public final static String CONFIGBEAN_ID = "ConfigBeanID";
	public final static String SUCCESS_START = "successStart";
}
