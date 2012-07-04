package org.eweb4j.ioc.config.bean;

import java.util.List;

import org.eweb4j.util.xml.AttrTag;

/**
 * IOC组件存取配置信息的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class IOCConfigBean {
	@AttrTag
	private String id;

	@AttrTag
	private String scope = "singleton";

	@AttrTag
	private String clazz;

	private List<Injection> inject;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public List<Injection> getInject() {
		return inject;
	}

	public void setInject(List<Injection> inject) {
		this.inject = inject;
	}

	@Override
	public String toString() {
		return "IOCConfigBean [id=" + id + ", scope=" + scope + ", clazz="
				+ clazz + ", inject=" + inject + "]";
	}

}
