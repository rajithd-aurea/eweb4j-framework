package org.eweb4j.config.bean;

/**
 * EWeb4J用来存取配置信息的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class ConfigORM {
	private String open = "false";

	private LogsConfigBean logs;

	/* 数据源类完整路径，默认c3p0 */
	private String dataSource = "com.mchange.v2.c3p0.ComboPooledDataSource";

	private ScanPojoPackage scanPojoPackage;

	private ORMXmlFiles ormXmlFiles;

	private DBInfoXmlFiles dbInfoXmlFiles;

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public ScanPojoPackage getScanPojoPackage() {
		return scanPojoPackage;
	}

	public void setScanPojoPackage(ScanPojoPackage scanPojoPackage) {
		this.scanPojoPackage = scanPojoPackage;
	}

	public ORMXmlFiles getOrmXmlFiles() {
		return ormXmlFiles;
	}

	public void setOrmXmlFiles(ORMXmlFiles ormXmlFiles) {
		this.ormXmlFiles = ormXmlFiles;
	}

	public DBInfoXmlFiles getDbInfoXmlFiles() {
		return dbInfoXmlFiles;
	}

	public void setDbInfoXmlFiles(DBInfoXmlFiles dbInfoXmlFiles) {
		this.dbInfoXmlFiles = dbInfoXmlFiles;
	}

	public LogsConfigBean getLogs() {
		return logs;
	}

	public void setLogs(LogsConfigBean logs) {
		this.logs = logs;
	}

	@Override
	public String toString() {
		return "ConfigORM [open=" + open + ", logs=" + logs + ", dataSource="
				+ dataSource + ", scanPojoPackage=" + scanPojoPackage
				+ ", ormXmlFiles=" + ormXmlFiles + ", dbInfoXmlFiles="
				+ dbInfoXmlFiles + "]";
	}

}
