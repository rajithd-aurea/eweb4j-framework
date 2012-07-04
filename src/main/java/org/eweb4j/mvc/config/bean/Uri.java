package org.eweb4j.mvc.config.bean;

import org.eweb4j.util.xml.AttrTag;

public class Uri {

	@AttrTag
	private String value;

	@AttrTag
	private String type;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Uri [value=" + value + ", type=" + type + "]";
	}

}
