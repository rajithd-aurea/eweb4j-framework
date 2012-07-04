package org.eweb4j.config.bean;

import java.util.ArrayList;
import java.util.List;

public class LogsConfigBean {

	private List<LogConfigBean> log = new ArrayList<LogConfigBean>();

	public List<LogConfigBean> getLog() {
		return log;
	}

	public void setLog(List<LogConfigBean> log) {
		this.log = log;
	}

	@Override
	public String toString() {
		return "LogsConfigBean [log=" + log + "]";
	}

}
