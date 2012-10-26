package org.eweb4j.orm.sql;

public class SqlCreateException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public SqlCreateException(String name, Throwable e){
		super(name, e);
	}
	public SqlCreateException(String name){
		super(name);
	}
}
