package org.eweb4j.config;

import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.config.bean.LogConfigBean;
import org.eweb4j.config.bean.LogsConfigBean;

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
	
	@Deprecated
	public static Log getConfigLogger(Class<?> clazz) {
		return getLogger(clazz);
	}

}
