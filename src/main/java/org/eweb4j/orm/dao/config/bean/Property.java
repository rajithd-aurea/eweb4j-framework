package org.eweb4j.orm.dao.config.bean;

import org.eweb4j.util.xml.AttrTag;

public class Property {
	@AttrTag
	private String key;

	@AttrTag
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		String _key = key;
		String _value = value;
		if (key.equals("user"))
			_value = "******";

		if (key.equals("password"))
			_value = "******";

		return "Property [key=" + _key + ", value=" + _value + "]";
	}

}
