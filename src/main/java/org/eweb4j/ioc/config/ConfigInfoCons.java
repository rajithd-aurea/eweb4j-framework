package org.eweb4j.ioc.config;

public interface ConfigInfoCons {
	public static final String CANNOT_READ_CONFIG_FILE = "can not read any configuration! now it has bean repaired, please restart.";;
	public static final String READ_CONFIG_FILE_SUCCESS = "start configuration has bean validated and pushed to the cache";
	public static final String REPAIR_FILE = "configuration error, now it has bean reparied. exception：";
	public static final String CANNOT_REPAIR_FILE = "can not write any configuration, exception:";
}
