package org.eweb4j.orm.dao.select;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.eweb4j.orm.OrderType;
import org.eweb4j.orm.config.ORMConfigBeanUtil;
import org.eweb4j.orm.dao.DAOException;
import org.eweb4j.orm.jdbc.JdbcUtil;
import org.eweb4j.orm.sql.SqlFactory;
import org.eweb4j.util.StringUtil;

public class SelectDAOImpl implements SelectDAO {
	private DataSource ds;
	private String dbType;

	public SelectDAOImpl(DataSource ds, String dbType) {
		this.ds = ds;
		this.dbType = dbType;
	}

	public <T> List<T> selectAll(Class<T> clazz, String orderField,
			int orderType) throws DAOException {
		List<T> list = null;
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(clazz.newInstance(), dbType).selectAll(orderField, orderType);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("", e);
			}
		}
		return list;
	}

	public <T> List<T> selectAll(Class<T> clazz, int orderType)
			throws DAOException {
		return this.selectAll(clazz, null, orderType);
	}

	public <T> List<T> selectAll(Class<T> clazz) throws DAOException {
		return this.selectAll(clazz, null, OrderType.DESC_ORDER);
	}

	public <T> T selectOne(T t, String... fields) throws DAOException {
		T result = null;
		if (t != null) {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields);
				List<T> list = JdbcUtil.getList(con, clazz, sql);
				if (list != null && !list.isEmpty()) {
					result = list.get(0);
				}
			} catch (Exception e) {
				throw new DAOException("", e);
			}
		}
		return result;
	}

	public <T> T selectOne(Class<T> clazz, String[] fields, String[] values)
			throws DAOException {
		T result = null;
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				T t = clazz.newInstance();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields, values);
				List<T> list = JdbcUtil.getList(con, clazz, sql);
				if (list != null && !list.isEmpty()) {
					result = list.get(0);
				}
			} catch (Exception e) {
				throw new DAOException("", e);
			}
		}
		return result;
	}

	public <T> T selectOneByWhere(Class<T> clazz, String condition) throws DAOException {
		T result = null;
		List<T> list = this.selectWhere(clazz, ORMConfigBeanUtil.parseQuery(condition, clazz));
		if (list != null && !list.isEmpty()) {
			result = list.get(0);
		}
		return result;
	}

	public <T> long selectCount(Class<T> clazz) throws DAOException {
		return this.selectCount(clazz, null);
	}

	public <T> long selectCount(Class<T> clazz, String condition) throws DAOException {
		long result = -1;
		Connection con = null;
		try {
			con = ds.getConnection();
			if (clazz != null) {
				T t = clazz.newInstance();
				String sql = SqlFactory.getSelectSql(t, dbType).selectCount(ORMConfigBeanUtil.parseQuery(condition, clazz));
				String str = String.valueOf(JdbcUtil.getObject(con, sql));
				if (StringUtil.isNumeric(str)) {
					result = Integer.parseInt(str);
				}
			}
		} catch (Exception e) {
			throw new DAOException("", e);
		}
		return result;
	}

	public <T> List<T> selectWhere(Class<T> clazz, String condition, Object... args) throws DAOException {
		List<T> result = null;
		Connection con = null;
		try {
			con = ds.getConnection();
			T t = clazz.newInstance();
			String sql = SqlFactory.getSelectSql(t, dbType).select(ORMConfigBeanUtil.parseQuery(condition, clazz));
			result = JdbcUtil.getListWithArgs(con, clazz, sql, args);
		} catch (Exception e) {
			throw new DAOException("", e);
		}
		return result;
	}

	public <T> List<T> selectBySQL(Class<T> clazz, String sql, Object... args)
			throws DAOException {
		List<T> result = null;
		Connection con = null;
		try {
			con = ds.getConnection();
			result = JdbcUtil.getListWithArgs(con, clazz, sql, args);
		} catch (Exception e) {
			throw new DAOException("", e);
		}

		return result;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public <T> long selectCount(Class<T> clazz, String condition,
			Object... args) throws DAOException {
		long result = -1;
		Connection con = null;
		try {
			con = ds.getConnection();
			if (clazz != null) {
				T t = clazz.newInstance();
				String sql = SqlFactory.getSelectSql(t, dbType).selectCount(ORMConfigBeanUtil.parseQuery(condition, clazz));
				String str = String.valueOf(JdbcUtil.getObject(con, sql, args));
				if (StringUtil.isNumeric(str)) {
					result = Integer.parseInt(str);
				}
			}
		} catch (Exception e) {
			throw new DAOException("", e);
		}
		return result;
	}

	public <T> T selectOneById(Class<T> clazz, Number id) throws DAOException {
		if (clazz == null || id == null)
			return null;

		String idField = ORMConfigBeanUtil.getIdField(clazz);
		if (idField == null)
			return null;

		String[] fields = { idField };
		String[] values = { String.valueOf(id) };

		return this.selectOne(clazz, fields, values);
	}

	public <T> T selectOneById(T t) throws DAOException {
		if (t == null)
			return null;

		String idField = ORMConfigBeanUtil.getIdField(t.getClass());
		if (idField == null)
			return null;

		return this.selectOne(t, idField);
	}
}
