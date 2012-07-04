package org.eweb4j.orm.jdbc.transaction;

import java.sql.SQLException;

public abstract class Trans {

	private boolean isFirst = false;

	/**
	 * 开启当前线程的数据库事务锁。包括做以下事情： 1. 锁定当前线程的数据库连接，保持当前线程获取的连接是唯一的 2.
	 * 其他地方在获取数据库连接的时候，判断是否开启事务锁。 3.
	 * 若开启，先从当前线程连接池中获取，若获取不到，则从实际的数据源中获取一条，然后关闭其自动提交功能，并将其放入当前线程变量连接池中。 4.
	 * 这样就能保证当前线程执行的所有数据库操作都用的是同一个连接对象，达到控制事务的目的。
	 * 
	 * @throws SQLException
	 */
	public void begin(int level) throws SQLException {
		ConThreadLocal.setLevel(level);
		
		if (!ConThreadLocal.isTrans()) {
			this.isFirst = true;
			ConThreadLocal.lock(true);// 给当前线程的数据库连接上锁
		}
	}

	/**
	 * 从当前线程变量中获取所有的连接，并全部一起提交。
	 * 
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		if (isFirst)
			ConThreadLocal.commit();
	}

	/**
	 * 从当前线程变量中获取所有连接，并全部一起回滚
	 * 
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		if (isFirst)
			ConThreadLocal.rollback();
	}

	/**
	 * 从当前线程变量中获取所有连接，并全部一起关闭
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		if (isFirst)
			ConThreadLocal.close();
	}

	/**
	 * 执行数据库操作
	 * 
	 * @param args
	 * @throws Exception
	 */
	public abstract void run(Object... args) throws Exception;
}
