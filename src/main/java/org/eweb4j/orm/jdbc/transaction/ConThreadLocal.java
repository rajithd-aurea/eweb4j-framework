package org.eweb4j.orm.jdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eweb4j.orm.dao.config.DAOConfigConstant;


public class ConThreadLocal {
	/**
	 * 标记当前线程是否是事务操作
	 */
	private final static ThreadLocal<Boolean> transLock = new ThreadLocal<Boolean>();
	
	/**
	 * 保存当前线程中所有正在操作的数据库连接。
	 * kye:dataSourceName
	 * value:connection
	 * 
	 * 注意：只有transLock=true的前提下，才会保存。每次close之后，都会清空
	 */
	private final static ThreadLocal<HashMap<String, Connection>> cons = new ThreadLocal<HashMap<String, Connection>>();

	/**
	 * 给当前进行的事务上锁
	 * 
	 * @param value
	 */
	public static void lock(boolean value) {
		transLock.set(value);
	}

	/**
	 * 获取当前的事务是否被上锁了
	 * 
	 * @return
	 */
	public static Boolean isTrans() {
		if (transLock.get() == null) {
			transLock.set(false);
		}
		return transLock.get();
	}

	/**
	 * 获取当前线程下的所有数据源的连接
	 * 
	 * @return
	 */
	public static Map<String, Connection> getCons() {
		return cons.get();
	}

	public static void setLevel(int level) throws SQLException{
		Map<String, Connection> cons = ConThreadLocal.getCons();
		if (cons != null)
			for (Entry<String, Connection> entry : cons.entrySet()) {
				entry.getValue().setTransactionIsolation(level);
			}
	}
	
	/**
	 * 从当前线程变量中获取所有的连接，并全部一起提交。
	 * 
	 * @throws SQLException
	 */
	public static void commit() throws SQLException {
		Map<String, Connection> cons = ConThreadLocal.getCons();
		if (cons != null)
			for (Entry<String, Connection> entry : cons.entrySet()) {
				entry.getValue().commit();
			}
	}

	/**
	 * 从当前线程变量中获取所有连接，并全部一起回滚
	 * 
	 * @throws SQLException
	 */
	public static void rollback() throws SQLException {
		Map<String, Connection> cons = ConThreadLocal.getCons();
		if (cons != null)
			for (Entry<String, Connection> entry : cons.entrySet()) {
				entry.getValue().rollback();
			}
	}

	/**
	 * 从当前线程变量中获取所有连接，并全部一起关闭
	 * 
	 * @throws SQLException
	 */
	public static void close() throws SQLException {
		Map<String, Connection> cons = ConThreadLocal.getCons();
		if (cons != null)
			for (Entry<String, Connection> entry : cons.entrySet()) {
				entry.getValue().close();
			}

		ConThreadLocal.remove();// 移除当前线程中已经被关闭的连接

		ConThreadLocal.lock(false);// 解开线程锁
	}

	/**
	 * 获取当前线程下的给定数据源名称的连接
	 * 
	 * @param dsName
	 * @return
	 */
	public static Connection getCon(String dsName) {
		if (dsName == null) {
			dsName = DAOConfigConstant.MYDBINFO;
		}
		if (cons.get() == null) {
			cons.set(new HashMap<String, Connection>());
		}
		return cons.get().get(dsName);
	}

	/**
	 * 获取默认数据源名称的连接
	 * 
	 * @return
	 */
	public static Connection getCon() {
		return getCon(null);
	}

	public static void put(String dsName, Connection con) {
		if (transLock.get()) {
			Map<String, Connection> map = cons.get();
			if (map == null)
				map = new HashMap<String, Connection>();
			map.put(dsName, con);
		}
	}

	public static void remove() {
		cons.remove();
	}

}
