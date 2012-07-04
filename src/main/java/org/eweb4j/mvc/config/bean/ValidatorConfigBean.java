package org.eweb4j.mvc.config.bean;

import java.util.ArrayList;
import java.util.List;

import org.eweb4j.util.xml.AttrTag;

public class ValidatorConfigBean {

	@AttrTag
	private String name;

	@AttrTag
	private String clazz;

	private List<FieldConfigBean> field = new ArrayList<FieldConfigBean>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public List<FieldConfigBean> getField() {
		return field;
	}

	public void setField(List<FieldConfigBean> field) {
		this.field = field;
	}

	@Override
	public String toString() {
		return "ValidatorConfigBean [name=" + name + ", clazz=" + clazz
				+ ", field=" + field + "]";
	}
}
