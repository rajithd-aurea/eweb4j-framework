package org.eweb4j.config;

import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.config.bean.LogConfigBean;
import org.eweb4j.config.bean.LogsConfigBean;

public class LogFactory {
	public static Log getIOCLogger(Class<?> clazz) {
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigConstant.CONFIGBEAN_ID);

		LogsConfigBean logs = cb == null ? new LogsConfigBean() : cb.getIoc().getLogs();

		return new LogImpl(logs, "IOC", clazz);
	}

	public static Log getMVCLogger(Class<?> clazz) {
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigConstant.CONFIGBEAN_ID);

		LogsConfigBean logs = cb == null ? new LogsConfigBean() : cb.getMvc().getLogs();

		return new LogImpl(logs, "MVC", clazz);
	}

	public static Log getORMLogger(Class<?> clazz) {
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigConstant.CONFIGBEAN_ID);

		LogsConfigBean logs = cb == null ? new LogsConfigBean() : cb.getOrm().getLogs();

		return new LogImpl(logs, "ORM", clazz);
	}

	public static Log getConfigLogger(Class<?> clazz) {
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigConstant.CONFIGBEAN_ID);

		LogConfigBean log = new LogConfigBean();
		log.setLevel("debug");
		log.setFile(null);
		log.setSize("0");
		LogsConfigBean logs = new LogsConfigBean();
		logs.getLog().add(log);
		if (cb == null)
			return new LogImpl(logs, "CONFIG", clazz);

		String debug = cb.getDebug();
		if ("true".equals(debug) || "1".equals(debug)) {
			log.setConsole("1");
			return new LogImpl(logs, "CONFIG", clazz);
		} else {
			log.setConsole("0");
			return new LogImpl(logs, "CONFIG", clazz);
		}
	}

}
