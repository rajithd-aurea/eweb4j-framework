package org.eweb4j.config.bean;

import org.eweb4j.util.xml.AttrTag;
import org.eweb4j.util.xml.Skip;

public class LogConfigBean {

	@AttrTag
	private String console = "1";
	@AttrTag
	private String level = "info";
	@AttrTag
	private String file = "logs/logs.log";
	@Skip
	@AttrTag
	private String insert = "";
	@AttrTag
	private String size = "5";

	public String getConsole() {
		return console;
	}

	public void setConsole(String console) {
		this.console = console;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getInsert() {
		return insert;
	}

	public void setInsert(String insert) {
		this.insert = insert;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "LogConfigBean [console=" + console + ", level=" + level
				+ ", file=" + file + ", insert=" + insert + ", size=" + size
				+ "]";
	}

}
