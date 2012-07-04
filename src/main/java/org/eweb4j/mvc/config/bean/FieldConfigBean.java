package org.eweb4j.mvc.config.bean;

import java.util.ArrayList;
import java.util.List;

import org.eweb4j.util.xml.AttrTag;

public class FieldConfigBean {

	@AttrTag
	private String name;

	@AttrTag
	private String message;

	private List<ParamConfigBean> param = new ArrayList<ParamConfigBean>();

	@Override
	public String toString() {
		return "FieldConfigBean [name=" + name + ", message=" + message
				+ ", param=" + param + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ParamConfigBean> getParam() {
		return param;
	}

	public void setParam(List<ParamConfigBean> param) {
		this.param = param;
	}
}
