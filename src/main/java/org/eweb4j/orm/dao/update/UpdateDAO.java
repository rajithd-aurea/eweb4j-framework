package org.eweb4j.orm.dao.update;

import org.eweb4j.orm.dao.DAOException;

public interface UpdateDAO {
	/**
	 * 修改表记录所有字段，通过主键值作为条件
	 * 
	 * @param <T>
	 * @param ts
	 * @return
	 */
	public <T> Number[] batchUpdate(T... ts) throws DAOException;

	public <T> Number update(T t) throws DAOException;

	/**
	 * 修改表记录部分字段，通过主键值作为条件
	 * 
	 * @param <T>
	 * @param ts
	 * @param fields
	 * @return
	 */
	public <T> Number[] updateByFields(T[] ts, String... fields)
			throws DAOException;

	/**
	 * 修改给定字段，通过主键值作为条件
	 * 
	 * @param <T>
	 * @param t
	 *            给定的对象
	 * @param fields
	 *            给定的字段
	 * @return 返回布尔
	 */
	public <T> Number updateByFields(T t, String... fields) throws DAOException;

	/**
	 * 修改给定字段为给定值,通过主键作为条件
	 * 
	 * @param <T>
	 * @param t
	 * @param fields
	 * @param values
	 * @return
	 */
	public <T> Number updateByFieldIsValue(T t, String[] fields, String[] values)
			throws DAOException;

	public <T> Number[] updateByFieldIsValue(T[] ts, String[] fields,
			String[] values) throws DAOException;

	/**
	 * 修改给定字段为给定值,通过主键做条件
	 * 
	 * @param <T>
	 * @param t
	 * @param field
	 * @param value
	 * @return
	 */
	public <T> Number updateByFieldIsValue(T t, String field, String value)
			throws DAOException;

	public <T> Number[] updateByFieldIsValue(Class<T>[] clazz, String field,
			String value) throws DAOException;

	/**
	 * 执行给定sql
	 * 
	 * @param <T>
	 * @param sqls
	 * @return
	 */
	public <T> Number[] updateBySQL(String... sqls) throws DAOException;

	/**
	 * 执行给定sql,支持？占位符
	 * 
	 * @param <T>
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> Number updateBySQLWithArgs(String sql, Object... args)
			throws DAOException;

	/**
	 * 执行给定sql,支持？占位符
	 * 
	 * @param <T>
	 * @param sqls
	 * @param args
	 * @return
	 */
	public <T> Number[] updateBySQLWithArgs(String[] sqls, Object[][] args)
			throws DAOException;

}
