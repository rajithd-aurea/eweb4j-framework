package org.eweb4j.orm.dao.insert;

import java.sql.Connection;

import javax.sql.DataSource;

import org.eweb4j.orm.dao.DAOException;
import org.eweb4j.orm.dao.DAOUtil;
import org.eweb4j.orm.jdbc.JdbcUtil;
import org.eweb4j.orm.sql.SqlFactory;

public class InsertDAOImpl implements InsertDAO {
	private DataSource ds;
	private String dbType;

	public InsertDAOImpl(DataSource ds, String dbType) {
		this.ds = ds;
		this.dbType = dbType;
	}

	public <T> Number[] batchInsert(T... ts) throws DAOException {
		Number[] ids = null;
		Connection con = null;
		if (ts == null || ts.length == 0)
			return ids;

		ids = new Number[ts.length];

		try {
			con = ds.getConnection();
			for (int i = 0; i < ts.length; i++) {
				String[] sqls = SqlFactory.getInsertSql(new Object[] { ts[i] })
						.create();
				if (sqls == null)
					ids[i] = -1;
				else {
					int rs = (Integer) JdbcUtil.update(con, sqls[0]);
					if (rs > 0) {
						ids[i] = DAOUtil.selectMaxId(ts[i], ds.getConnection(),
								dbType);
						// 缓存
					}
				}

			}
		} catch (Exception e) {
			throw new DAOException("", e);
		}

		return ids;
	}

	public <T> Number insert(T t) throws DAOException {
		Number[] rs = batchInsert(new Object[] { t });
		return rs == null ? 0 : rs[0];
	}

	public <T> Number insertBySql(Class<T> clazz, String sql, Object... args)
			throws DAOException {
		Number id = 0;
		if (sql == null)
			return -1;

		Connection con = null;
		try {
			con = ds.getConnection();

			int rs = (Integer) JdbcUtil.updateWithArgs(con, sql, args);
			if (rs > 0) {
				id = DAOUtil.selectMaxId(clazz, ds.getConnection(), dbType);
			}
		} catch (Exception e) {
			throw new DAOException("", e);
		}

		return id;
	}

	public <T> Number[] insertByCondition(T[] ts, String condition)
			throws DAOException {
		Number[] ids = null;
		Connection con = null;
		if (ts == null || ts.length == 0)
			return ids;

		ids = new Number[ts.length];
		try {
			con = ds.getConnection();
			for (int i = 0; i < ts.length; i++) {
				String[] sqls = SqlFactory.getInsertSql(new Object[] { ts[i] })
						.create(condition);
				if (sqls == null)
					ids[i] = -1;
				else {
					int rs = (Integer) JdbcUtil.update(con, sqls[0]);
					if (rs > 0) {
						ids[i] = DAOUtil.selectMaxId(ts[i], ds.getConnection(),
								dbType);
						// 缓存
					}
				}

			}
		} catch (Exception e) {
			throw new DAOException("", e);
		}

		return ids;
	}

	public <T> Number[] insertByFields(T[] ts, String[] fields)
			throws DAOException {
		Number[] ids = null;
		Connection con = null;
		if (ts == null || ts.length == 0)
			return ids;

		ids = new Number[ts.length];
		try {
			con = ds.getConnection();

			for (int i = 0; i < ts.length; i++) {
				String[] sqls = SqlFactory.getInsertSql(new Object[] { ts[i] })
						.createByFields(fields);
				if (sqls == null)
					ids[i] = -1;
				else {
					int rs = (Integer) JdbcUtil.update(con, sqls[0]);
					if (rs > 0) {
						ids[i] = DAOUtil.selectMaxId(ts[i], ds.getConnection(),
								dbType);
						// 缓存
					}
				}

			}
		} catch (Exception e) {
			throw new DAOException("", e);
		}

		return ids;
	}

	public <T> Number insertByField(T t, String... field) throws DAOException {
		Number[] rs = this.insertByFields(new Object[] { t }, field);
		return rs == null ? 0 : rs[0];
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

}
