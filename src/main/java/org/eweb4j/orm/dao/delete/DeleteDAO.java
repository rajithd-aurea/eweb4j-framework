package org.eweb4j.orm.dao.delete;

import org.eweb4j.orm.dao.DAOException;

public interface DeleteDAO {
	/**
	 * 以ID值作为条件删除
	 * @param <T>
	 * @param ts
	 * @return
	 */
	public <T> Number[] batchDelete(T... ts) throws DAOException;
	
	/**
	 * 通过ID值删除记录
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public <T> Number deleteById(Class<T> clazz, Number id) throws DAOException;
	
	/**
	 * 通过ID值删除记录
	 * @param <T>
	 * @param t
	 * @return
	 * @throws DAOException
	 */
	public <T> Number deleteById(T t) throws DAOException;

	/**
	 * 给定字段名等于给定字段值作为条件删除
	 * @param <T>
	 * @param clazz
	 * @param fields
	 * @param values
	 * @return
	 */
	public <T> Number[] deleteByFieldIsValue(Class<T>[] clazz, String[] fields,
			String[] values) throws DAOException;
	
	public <T> Number[] deleteByFieldIsValue(Class<T>[] clazz, String field,
			String value) throws DAOException;

	/**
	 * 删除记录，按给定字段
	 * 
	 * @param <T>
	 * @param t
	 * @param columns
	 * @return
	 */
	public <T> Number deleteByFields(T t, String[] fields) throws DAOException;

	/**
	 * 删除记录，按给定字段、给定值
	 * 
	 * @param <T>
	 * @param clazz
	 * @param fields
	 * @param values
	 * @return
	 */
	public <T> Number deleteByFieldIsValue(Class<T> clazz, String[] fields,
			String values[]) throws DAOException;

	/**
	 * 删除记录，按给定字段、给定值
	 * 
	 * @param <T>
	 * @param clazz
	 * @param field
	 * @param value
	 * @return
	 */
	public <T> Number deleteByFieldIsValue(Class<T> clazz, String field,
			String value) throws DAOException;

	/**
	 * 给定条件删除记录，支持？占位符
	 * 
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @param args
	 * @return
	 */
	public <T> Number deleteWhere(Class<T> clazz, String condition,
			Object[] args) throws DAOException;
}
