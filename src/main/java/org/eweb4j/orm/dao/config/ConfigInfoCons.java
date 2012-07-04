package org.eweb4j.orm.dao.config;

public interface ConfigInfoCons {
	public static final String CANNOT_READ_CONFIG_FILE = "DAOConfig.class : 无法获取配置文件";
	public static final String REPAIR_FILE_INFO = "can not read any configuration! now it has bean repaired, please restart.";
	public static final String CANNOT_GET_DB_CON = "can not connect database";
	public static final String READ_CONFIG_INFO_SUCCESS = "start configuration has bean validated and pushed to the cache";
	public static final String REPAIR_CONFIG_FILE = "configuration error, now it has bean reparied. exception：";
	public static final String CANNOT_REPAIR_FILE = "can not write any configuration, exception:";
}
