package org.eweb4j.orm.jdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.orm.dao.DAOException;
import org.eweb4j.util.CommonUtil;

public class Transaction {

	private static Log log = LogFactory.getORMLogger(Transaction.class);

	public static void execute(Trans trans, Object... args) {
		execute(Connection.TRANSACTION_READ_UNCOMMITTED, trans, args);
	}
	
	public static void execute(int level, Trans trans, Object... args){
		try {
			trans.begin(level);
			trans.run(args);// 执行事务
			trans.commit();
		} catch (Exception e) {
			try {
				trans.rollback();// 回滚事务
			} catch (SQLException e1) {
				throw new DAOException("roll back exception ", e);
			}
			String info = CommonUtil.getExceptionString(e);
			log.debug(info);
			//throw new DAOException("transaction execute exception ", e);
		} finally {
			try {
				trans.close();
			} catch (SQLException e) {
				//throw new DAOException("transaction close exception ", e);
			}
		}
	}
}
