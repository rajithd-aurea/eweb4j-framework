package org.eweb4j.orm.jdbc;

public class JdbcUtilException extends RuntimeException {

	private static final long serialVersionUID = 7224252972900965509L;

	public JdbcUtilException(String err, Throwable e) {
		super(err, e);
	}
}
