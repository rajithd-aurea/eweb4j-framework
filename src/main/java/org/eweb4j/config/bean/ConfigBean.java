package org.eweb4j.config.bean;

import org.eweb4j.cache.SingleBeanCache;

/**
 * EWeb4J用来存取配置信息的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class ConfigBean {
	private String reload = "0";

	private String debug = "1";

	private I18N locales;
	
	private Properties properties;
	
	private ConfigIOC ioc;

	private ConfigORM orm;

	private ConfigMVC mvc;

	public static ConfigBean get(){
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class);
		return cb;
	}
	
	public I18N getLocales() {
		return locales;
	}

	public void setLocales(I18N locales) {
		this.locales = locales;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public String getReload() {
		return reload;
	}

	public void setReload(String reload) {
		this.reload = reload;
	}

	public ConfigIOC getIoc() {
		return ioc;
	}

	public void setIoc(ConfigIOC ioc) {
		this.ioc = ioc;
	}

	public ConfigORM getOrm() {
		return orm;
	}

	public void setOrm(ConfigORM orm) {
		this.orm = orm;
	}

	public ConfigMVC getMvc() {
		return mvc;
	}

	public void setMvc(ConfigMVC mvc) {
		this.mvc = mvc;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	@Override
	public String toString() {
		return "ConfigBean [reload=" + reload + ", debug=" + debug
				+ ", properties=" + properties + ", locales=" + locales + ", ioc="
				+ ioc + ", orm=" + orm + ", mvc=" + mvc + "]";
	}

}
