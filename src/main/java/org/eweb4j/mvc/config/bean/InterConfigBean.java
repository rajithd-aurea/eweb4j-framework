package org.eweb4j.mvc.config.bean;

import java.util.ArrayList;
import java.util.List;

import org.eweb4j.util.xml.AttrTag;

public class InterConfigBean {

	private List<Uri> uri = new ArrayList<Uri>();

	private List<String> except = new ArrayList<String>();

	@AttrTag
	private String name;

	@AttrTag
	private String type = "before";

	@AttrTag
	private String policy = "and";

	@AttrTag
	private String priority = "0";

	@AttrTag
	private String scope = "prototype";

	@AttrTag
	private String clazz;

	@AttrTag
	private String method;

	public List<Uri> getUri() {
		return uri;
	}

	public void setUri(List<Uri> url) {
		this.uri = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getExcept() {
		if (except.isEmpty())
			except.add("");

		return except;
	}

	public void setExcept(List<String> except) {
		this.except = except;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return "InterConfigBean [uri=" + uri + ", except=" + except + ", name="
				+ name + ", type=" + type + ", policy=" + policy
				+ ", priority=" + priority + ", scope=" + scope + ", clazz="
				+ clazz + ", method=" + method + "]";
	}

}
