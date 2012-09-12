package org.eweb4j.config.bean;

import org.eweb4j.orm.dao.config.DAOConfigConstant;
import org.eweb4j.util.xml.AttrTag;

public class Ddl {

	@AttrTag
	private String generate = "true";

	@AttrTag
	private String run = "false";

	@AttrTag
	private String ds = DAOConfigConstant.MYDBINFO;

	public String getGenerate() {
		return generate;
	}

	public void setGenerate(String generate) {
		this.generate = generate;
	}

	public String getRun() {
		return run;
	}

	public void setRun(String run) {
		this.run = run;
	}

	public String getDs() {
		return ds;
	}

	public void setDs(String ds) {
		this.ds = ds;
	}

}
