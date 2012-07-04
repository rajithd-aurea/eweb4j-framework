package org.eweb4j.orm.dao.update;

import java.sql.Connection;

import javax.sql.DataSource;

import org.eweb4j.orm.dao.DAOException;
import org.eweb4j.orm.jdbc.JdbcUtil;
import org.eweb4j.orm.sql.SqlFactory;

public class UpdateDAOImpl implements UpdateDAO {
	private DataSource ds;

	public UpdateDAOImpl(DataSource ds) {
		this.ds = ds;
	}

	public <T> Number update(T t) throws DAOException {
		Number[] rs = batchUpdate(new Object[] { t });
		return rs == null ? 0 : rs[0];
	}

	public <T> Number[] batchUpdate(T... ts) throws DAOException {
		Number[] ids = null;
		if (ts != null && ts.length > 0) {
			Connection con = null;
			ids = new Number[ts.length];
			try {
				con = ds.getConnection();
				for (int i = 0; i < ts.length; i++) {
					String[] sqls = SqlFactory.getUpdateSql(new Object[] { ts[i] }).update();
					ids[i] = JdbcUtil.update(con, sqls[i]);
					// 更新缓存
				}
			} catch (Exception e) {
				throw new DAOException("batchUpdate exception ", e);
			}
		}
		return ids;
	}

	public <T> Number updateByFields(T t, String... fields) throws DAOException {
		Number[] rs = this.updateByFields(new Object[] { t }, fields);
		return rs == null ? 0 : rs[0];
	}

	public <T> Number updateByFieldIsValue(T t, String[] fields, String[] values)
			throws DAOException {
		Number[] rs = this.updateByFieldIsValue(new Object[] { t }, fields,
				values);
		return rs == null ? 0 : rs[0];
	}

	public <T> Number updateByFieldIsValue(T t, String field, String value)
			throws DAOException {
		return this.updateByFieldIsValue(t, new String[] { field },
				new String[] { value });
	}

	public <T> Number updateBySQLWithArgs(String sql, Object... args)
			throws DAOException {
		Number[] rs = this.updateBySQLWithArgs(new String[] { sql },
				new Object[][] { args });
		return rs == null ? 0 : rs[0];
	}

	public <T> Number[] updateByFields(T[] ts, String... fields)
			throws DAOException {
		Number[] ids = null;
		if (ts != null && ts.length > 0 && fields != null && fields.length > 0) {
			Connection con = null;
			ids = new Number[ts.length];
			try {
				con = ds.getConnection();
				for (int i = 0; i < ts.length; i++) {
					String[] sqls = SqlFactory.getUpdateSql(ts).update(fields);
					ids[i] = JdbcUtil.update(con, sqls[i]);
					// 更新缓存
				}
			} catch (Exception e) {
				throw new DAOException("updateByFields exception ", e);
			}
		}
		return ids;
	}

	public <T> Number[] updateByFieldIsValue(T[] ts, String[] fields,
			String[] values) throws DAOException {
		Number[] ids = null;
		if (ts != null && ts.length > 0 && fields != null && fields.length > 0) {
			Connection con = null;
			ids = new Number[ts.length];
			try {
				con = ds.getConnection();
				for (int i = 0; i < ts.length; i++) {
					String[] sqls = SqlFactory.getUpdateSql(ts).update(fields,
							values);
					ids[i] = JdbcUtil.update(con, sqls[i]);
					// 更新缓存
				}
			} catch (Exception e) {
				throw new DAOException("updateByFieldIsValue exception ", e);
			}
		}
		return ids;
	}

	public <T> Number[] updateByFieldIsValue(Class<T>[] clazz, String field,
			String value) throws DAOException {
		return this.updateByFieldIsValue(clazz, new String[] { field },
				new String[] { value });
	}

	public <T> Number[] updateBySQL(String... sqls) throws DAOException {
		Number[] result = null;
		Connection con = null;
		try {
			con = ds.getConnection();
			result = JdbcUtil.update(con, sqls);
		} catch (Exception e) {
			throw new DAOException("updateBySQL exception ", e);
		}

		return result;
	}

	public <T> Number[] updateBySQLWithArgs(String[] sqls, Object[][] args)
			throws DAOException {
		Number[] result = null;
		Connection con = null;
		try {
			con = ds.getConnection();
			result = JdbcUtil.updateWithArgs(con, sqls, args);
		} catch (Exception e) {
			throw new DAOException("updateBySQLWithArgs exception ", e);
		}

		return result;
	}
	

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}
}
