package org.eweb4j.config;

public interface Log {
	
	public String debug(String debug);
	
	public String info(String info);

	public String warn(String warn);
	
	public String warn(String warn, Throwable e);

	public String error(String error);
	
	public String error(String error, Throwable e);

	public String fatal(String fatal);
	
	public String fatal(String fatal, Throwable e);
	
}
