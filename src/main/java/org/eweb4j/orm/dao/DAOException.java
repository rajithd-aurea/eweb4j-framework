package org.eweb4j.orm.dao;

import org.eweb4j.orm.jdbc.JdbcUtilException;

public class DAOException extends JdbcUtilException {
	private static final long serialVersionUID = 7567279073825068097L;

	public DAOException(String info, Throwable e) {
		super(info, e);
	}
}
