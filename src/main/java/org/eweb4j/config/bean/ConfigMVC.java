package org.eweb4j.config.bean;

/**
 * EWeb4J用来存取配置信息的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class ConfigMVC {
	private String open = "true";

	private LogsConfigBean logs;
	
	private UploadConfigBean upload;

	private ScanActionPackage scanActionPackage;

	private ScanInterceptorPackage scanInterceptorPackage;

	private ActionXmlFile actionXmlFiles;

	private InterXmlFile interXmlFiles;

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public UploadConfigBean getUpload() {
		return upload;
	}

	public void setUpload(UploadConfigBean upload) {
		this.upload = upload;
	}

	public LogsConfigBean getLogs() {
		return logs;
	}

	public void setLogs(LogsConfigBean logs) {
		this.logs = logs;
	}

	public ActionXmlFile getActionXmlFiles() {
		return actionXmlFiles;
	}

	public void setActionXmlFiles(ActionXmlFile actionXmlFiles) {
		this.actionXmlFiles = actionXmlFiles;
	}

	public InterXmlFile getInterXmlFiles() {
		return interXmlFiles;
	}

	public void setInterXmlFiles(InterXmlFile interXmlFiles) {
		this.interXmlFiles = interXmlFiles;
	}

	public ScanActionPackage getScanActionPackage() {
		return scanActionPackage;
	}

	public void setScanActionPackage(ScanActionPackage scanActionPackage) {
		this.scanActionPackage = scanActionPackage;
	}

	public ScanInterceptorPackage getScanInterceptorPackage() {
		return scanInterceptorPackage;
	}

	public void setScanInterceptorPackage(
			ScanInterceptorPackage scanInterceptorPackage) {
		this.scanInterceptorPackage = scanInterceptorPackage;
	}

	@Override
	public String toString() {
		return "ConfigMVC [open=" + open + ", upload=" + upload + ", logs="
				+ logs + ", scanActionPackage=" + scanActionPackage
				+ ", scanInterceptorPackage=" + scanInterceptorPackage
				+ ", actionXmlFiles=" + actionXmlFiles + ", interXmlFiles="
				+ interXmlFiles + "]";
	}

}
