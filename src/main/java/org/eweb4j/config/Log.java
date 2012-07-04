package org.eweb4j.config;

public interface Log {
	public void info(String info);

	public void debug(String debug);

	public void warn(String warn);

	public void error(String error);

	public void fatal(String fatal);
}
