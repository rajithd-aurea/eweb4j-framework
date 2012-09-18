package org.eweb4j.config;

public interface Log {
	public String info(String info);

	public String debug(String debug);

	public String warn(String warn);

	public String error(String error);

	public String fatal(String fatal);
}
