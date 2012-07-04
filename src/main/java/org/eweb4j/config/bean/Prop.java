package org.eweb4j.config.bean;

import org.eweb4j.util.xml.AttrTag;

public class Prop {
	@AttrTag
	private String id;

	@AttrTag
	private String path;

	@AttrTag
	private String global = "false";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getGlobal() {
		return global;
	}

	public void setGlobal(String global) {
		this.global = global;
	}

	@Override
	public String toString() {
		return "Prop [id=" + id + ", path=" + path + ", global=" + global + "]";
	}

}
