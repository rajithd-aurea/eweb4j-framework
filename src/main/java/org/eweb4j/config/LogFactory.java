package org.eweb4j.config;

import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.config.bean.LogConfigBean;
import org.eweb4j.config.bean.LogsConfigBean;
import org.eweb4j.util.CommonUtil;

public class LogFactory {
	public static Log getIOCLogger(Class<?> clazz) {
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class.getName());

		LogsConfigBean logs = cb == null ? new LogsConfigBean() : cb.getIoc().getLogs();

		return new LogImpl(logs, "IOC", clazz);
	}

	public static Log getMVCLogger(Class<?> clazz) {
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class.getName());

		LogsConfigBean logs = cb == null ? new LogsConfigBean() : cb.getMvc().getLogs();

		return new LogImpl(logs, "MVC", clazz);
	}

	public static Log getORMLogger(Class<?> clazz) {
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class.getName());

		LogsConfigBean logs = cb == null ? new LogsConfigBean() : cb.getOrm().getLogs();

		return new LogImpl(logs, "ORM", clazz);
	}

	public static Log getLogger(Class<?> clazz){
		return getLogger(clazz, true);
	}
	
	public static Log getLogger(Class<?> clazz, boolean isConsole){
		LogConfigBean log = new LogConfigBean();
		log.setLevel("debug");
		log.setFile(null);
		log.setSize("0");
		log.setConsole(String.valueOf(isConsole));
		LogsConfigBean logs = new LogsConfigBean();
		logs.getLog().add(log);
		return new LogImpl(logs, "CONFIG", clazz);
	}
	
	public static Log getLogger(Class<?> clazz, boolean isConsole, String level, String file, String fileSize){
		LogConfigBean log = new LogConfigBean();
		log.setLevel(level);
		log.setFile(file);
		log.setSize(""+CommonUtil.parseFileSize(fileSize));
		log.setConsole(String.valueOf(isConsole));
		LogsConfigBean logs = new LogsConfigBean();
		logs.getLog().add(log);
		return new LogImpl(logs, "LOG", clazz);
	}
	
	public static Log getFileLogger(Class<?> clazz, String level, String file){
		return getLogger(clazz, false, level, file, "5m");
	}
	
	public static Log getErrorLogger(Class<?> clazz, String file){
		return getLogger(clazz, false, "error", file, "5m");
	}
	
	public static Log getErrorLogger(Class<?> clazz){
		return getLogger(clazz, false, "error", "eweb4j-error.log", "5m");
	}
	
	@Deprecated
	public static Log getConfigLogger(Class<?> clazz) {
		return getLogger(clazz);
	}

}
