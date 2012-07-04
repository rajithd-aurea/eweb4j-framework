package org.eweb4j.config.bean;

/**
 * EWeb4J用来存取配置信息的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class ConfigIOC {
	private String open = "true";

	private LogsConfigBean logs;

	private IOCXmlFiles iocXmlFiles;

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public LogsConfigBean getLogs() {
		return logs;
	}

	public void setLogs(LogsConfigBean logs) {
		this.logs = logs;
	}

	public IOCXmlFiles getIocXmlFiles() {
		return iocXmlFiles;
	}

	public void setIocXmlFiles(IOCXmlFiles iocXmlFiles) {
		this.iocXmlFiles = iocXmlFiles;
	}

	@Override
	public String toString() {
		return "ConfigIOC [open=" + open + ", logs=" + logs + ", iocXmlFiles="
				+ iocXmlFiles + "]";
	}

}
