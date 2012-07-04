package org.eweb4j.orm.dao.select;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.eweb4j.orm.LikeType;
import org.eweb4j.orm.dao.DAOException;
import org.eweb4j.orm.jdbc.JdbcUtil;
import org.eweb4j.orm.sql.SqlFactory;

public class SearchDAOImpl implements SearchDAO {
	private String dbType;
	private DataSource ds;

	public SearchDAOImpl(DataSource ds, String dbType) {
		this.ds = ds;
		this.dbType = dbType;
	}

	public <T> List<T> searchByDivPage(T t, String[] fields, int likeType,
			boolean isLike, boolean isNot, boolean isOR, String orderField,
			int oType, int currentPage, int numPerPage) throws DAOException {
		List<T> list = null;
		if (t != null) {
			Connection con = null;
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields, likeType, isLike, isNot, isOR, orderField,
						oType, currentPage, numPerPage);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {

				throw new DAOException("", e);
			}
		}
		return list;
	}

	public <T> List<T> search(T t, String[] fields, int likeType,
			boolean isLike, boolean isNot, boolean isOR, String orderField,
			int oType) throws DAOException {
		List<T> list = null;
		if (t != null) {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
			Connection con = null;
			try {
				con = ds.getConnection();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields, likeType, isLike, isNot, isOR, orderField,
						oType, -1, -1);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("", e);
			}
		}
		return list;
	}

	public <T> List<T> searchByDivPage(Class<T> clazz, String[] fields,
			String[] values, int likeType, boolean isLike, boolean isNot,
			boolean isOR, String orderField, int oType, int currentPage,
			int numPerPage) throws DAOException {
		List<T> list = null;
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				T t = clazz.newInstance();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields, values, likeType, isLike, isNot, isOR,
						orderField, oType, currentPage, numPerPage);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("", e);
			}
		}
		return list;
	}

	public <T> List<T> search(Class<T> clazz, String[] fields, String[] values,
			int likeType, boolean isLike, boolean isNot, boolean isOR,
			String orderField, int oType) throws DAOException {
		List<T> list = null;
		if (clazz != null) {
			Connection con = null;
			try {
				con = ds.getConnection();
				T t = clazz.newInstance();
				String sql = SqlFactory.getSelectSql(t, dbType).selectWhere(
						fields, values, likeType, isLike, isNot, isOR,
						orderField, oType, -1, -1);
				list = JdbcUtil.getList(con, clazz, sql);
			} catch (Exception e) {
				throw new DAOException("", e);
			}
		}
		return list;
	}

	public <T> List<T> searchByExact(Class<T> clazz, String[] fields,
			String[] values, String orderField, int orderType, boolean isOR)
			throws DAOException {
		return search(clazz, fields, values, LikeType.ALL_LIKE, false, false,
				isOR, orderField, orderType);
	}

	public <T> List<T> searchByNotExact(Class<T> clazz, String[] fields,
			String[] values, String orderField, int orderType, boolean isOR)
			throws DAOException {
		return search(clazz, fields, values, 0, false, true, isOR, orderField,
				orderType);
	}

	public <T> List<T> searchByExact(T t, String[] fields, String orderField,
			int orderType, boolean isOR) throws DAOException {
		return search(t, fields, LikeType.ALL_LIKE, false, false, isOR,
				orderField, orderType);
	}

	public <T> List<T> searchByNotExact(T t, String[] fields,
			String orderField, int orderType, boolean isOR) throws DAOException {
		return search(t, fields, 0, false, true, isOR, orderField, orderType);
	}

	public <T> List<T> searchByLike(Class<T> clazz, String[] fields,
			String[] values, int likeType, String orderField, int orderType,
			boolean isOR) throws DAOException {
		return search(clazz, fields, values, likeType, true, false, isOR,
				orderField, orderType);
	}

	public <T> List<T> searchByNotLike(Class<T> clazz, String[] fields,
			String[] values, int likeType, String orderField, int orderType,
			boolean isOR) throws DAOException {
		return search(clazz, fields, values, likeType, true, true, isOR,
				orderField, orderType);
	}

	public <T> List<T> searchByLike(T t, String[] fields, int likeType,
			String orderField, int orderType, boolean isOR) throws DAOException {
		return search(t, fields, likeType, true, false, isOR, orderField,
				orderType);
	}

	public <T> List<T> searchByNotLike(T t, String[] fields, int likeType,
			String orderField, int orderType, boolean isOR) throws DAOException {
		return search(t, fields, likeType, true, true, isOR, orderField,
				orderType);
	}

	public <T> List<T> searchByLikeAndOrderByIdField(Class<T> clazz,
			String[] fields, String[] values, int likeType, int orderType,
			boolean isOR) throws DAOException {
		return searchByLike(clazz, fields, values, likeType, null, orderType,
				isOR);
	}

	public <T> List<T> searchByNotLikeAndOrderByIdField(Class<T> clazz,
			String[] fields, String[] values, int likeType, int orderType,
			boolean isOR) throws DAOException {
		return searchByNotLike(clazz, fields, values, likeType, null,
				orderType, isOR);
	}

	public <T> List<T> searchByLikeAndOrderByIdField(T t, String[] fields,
			int likeType, int orderType, boolean isOR) throws DAOException {
		return searchByLike(t, fields, likeType, null, orderType, isOR);
	}

	public <T> List<T> searchByExactAndOrderByIdField(Class<T> clazz,
			String[] fields, String[] values, int orderType, boolean isOR)
			throws DAOException {
		return searchByExact(clazz, fields, values, null, orderType, isOR);
	}

	public <T> List<T> searchByNotExactAndOrderByIdField(Class<T> clazz,
			String[] fields, String[] values, int orderType, boolean isOR)
			throws DAOException {
		return searchByNotExact(clazz, fields, values, null, orderType, isOR);
	}

	public <T> List<T> searchByExactAndOrderByIdField(T t, String[] fields,
			int orderType, boolean isOR) throws DAOException {
		return searchByExact(t, fields, null, orderType, isOR);
	}

	public <T> List<T> searchByNotExactAndOrderByIdField(T t, String[] fields,
			int orderType, boolean isOR) throws DAOException {
		return searchByNotExact(t, fields, null, orderType, isOR);
	}

	public <T> List<T> searchByExactAndOrderByIdFieldDESC(Class<T> clazz,
			String[] fields, String[] values, boolean isOR) throws DAOException {
		return searchByExactAndOrderByIdField(clazz, fields, values, -1, isOR);
	}

	public <T> List<T> searchByNotExactAndOrderByIdFieldDESC(Class<T> clazz,
			String[] fields, String[] values, boolean isOR) throws DAOException {
		return searchByNotExactAndOrderByIdField(clazz, fields, values, -1,
				isOR);
	}

	public <T> List<T> searchByExactAndOrderByIdFieldDESC(T t, String[] fields,
			boolean isOR) throws DAOException {
		return searchByExactAndOrderByIdField(t, fields, -1, isOR);
	}

	public <T> List<T> searchByNotExactAndOrderByIdFieldDESC(T t,
			String[] fields, boolean isOR) throws DAOException {
		return searchByNotExactAndOrderByIdField(t, fields, -1, isOR);
	}

	public <T> List<T> searchByNotLikeAndOrderByIdField(T t, String[] fields,
			int likeType, int orderType, boolean isOR) throws DAOException {
		return searchByNotLike(t, fields, likeType, null, orderType, isOR);
	}

	public <T> List<T> searchByLikeAndOrderByIdFieldDESC(Class<T> clazz,
			String[] fields, String[] values, int likeType, boolean isOR)
			throws DAOException {
		return searchByLikeAndOrderByIdField(clazz, fields, values, likeType,
				-1, isOR);
	}

	public <T> List<T> searchByNotLikeAndOrderByIdFieldDESC(Class<T> clazz,
			String[] fields, String[] values, int likeType, boolean isOR)
			throws DAOException {
		return searchByNotLikeAndOrderByIdField(clazz, fields, values,
				likeType, -1, isOR);
	}

	public <T> List<T> searchByLikeAndOrderByIdFieldDESC(T t, String[] fields,
			int likeType, boolean isOR) throws DAOException {
		return this
				.searchByLikeAndOrderByIdField(t, fields, likeType, -1, isOR);
	}

	public <T> List<T> searchByNotLikeAndOrderByIdFieldDESC(T t,
			String[] fields, int likeType, boolean isOR) throws DAOException {
		return searchByNotLikeAndOrderByIdField(t, fields, likeType, -1, isOR);
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

}
