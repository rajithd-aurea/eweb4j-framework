package org.eweb4j.mvc.config.bean;

import org.eweb4j.util.xml.AttrTag;

public class ParamConfigBean {

	@AttrTag
	private String name;

	@AttrTag
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ParamConfigBean [name=" + name + ", value=" + value + "]";
	}
}
