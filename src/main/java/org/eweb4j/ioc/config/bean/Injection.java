package org.eweb4j.ioc.config.bean;

import org.eweb4j.util.xml.AttrTag;

/**
 * IOC组件存取配置信息的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class Injection {
	@AttrTag
	private String ref;

	@AttrTag
	private String name;

	@AttrTag
	private String type;

	@AttrTag
	private String value;

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Injection [ref=" + ref + ", name=" + name + ", type=" + type
				+ ", value=" + value + "]";
	}

}
