package org.eweb4j.orm.dao.select;

import java.util.List;

import org.eweb4j.orm.dao.DAOException;


/**
 * 条件查询接口
 * @author weiwei
 *
 */
public interface SearchDAO {
	
	/**
	 * 结合各种条件查询,POJO对象含有数据
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @param fields
	 *            给定字段名
	 * @param likeType
	 *            模糊匹配类型 -1左匹配'x%'，0全匹配'%x%'，1右匹配'%x'，注意，仅当isLike为true时有效。
	 * @param isLike
	 *            是否是模糊匹配
	 * @param isNot
	 *            是否取反(true ——> A <> '1' 或者 A NOT LIKE '%1%' ; false ——> A = '1'
	 *            或者 A LIKE '%1%' ;)
	 * @param isOR
	 *            连接符是否为OR(true ——> A = '1' OR B = '2' ; false ——> A = '1' AND B
	 *            = '2' ;)
	 * @param orderField
	 *            排序字段名，当为null值时，取主键且自增长字段，若取不到，就取"id"名
	 * @param oType
	 *            排序类型 1升序 -1降序
	 * @param currentPage
	 *            当前页码 当页码和每页显示条数同时<=0时不执行分页
	 * @param numPerPage
	 *            每页显示条数 当页码和每页显示条数同时<=0时不执行分页
	 * @return 返回List<POJO>
	 */
	public <T> List<T> searchByDivPage(T t, String[] fields, int likeType,
			boolean isLike, boolean isNot, boolean isOR, String orderField,
			int oType, int currentPage, int numPerPage) throws DAOException;

	/**
	 * 请参照：public <T> List<T> searchByDivPage(T t, String[] fields, int
	 * likeType, boolean isLike, boolean isNot, boolean isOR, String orderField,
	 * int oType, int currentPage, int numPerPage);
	 * 
	 * @param <T>
	 * @param t
	 * @param fields
	 * @param likeType
	 * @param isLike
	 * @param isNot
	 * @param isOR
	 * @param orderField
	 * @param oType
	 * @return
	 */
	public <T> List<T> search(T t, String[] fields, int likeType,
			boolean isLike, boolean isNot, boolean isOR, String orderField,
			int oType) throws DAOException;

	/**
	 * 结合各种条件查询
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param fields
	 *            给定字段名
	 * @param values
	 *            给定字段值
	 * @param likeType
	 *            模糊匹配类型 -1左匹配'x%'，0全匹配'%x%'，1右匹配'%x'，注意，仅当isLike为true时有效。
	 * @param isLike
	 *            是否是模糊匹配
	 * @param isNot
	 *            是否取反(true ——> A <> '1' 或者 A NOT LIKE '%1%' ; false ——> A = '1'
	 *            或者 A LIKE '%1%' ;)
	 * @param isOR
	 *            连接符是否为OR(true ——> A = '1' OR B = '2' ; false ——> A = '1' AND B
	 *            = '2' ;)
	 * @param orderField
	 *            排序字段名，当为null值时，取主键且自增长字段，若取不到，就取"id"名
	 * @param oType
	 *            排序类型 1升序 -1降序
	 * @param currentPage
	 *            当前页码 当页码和每页显示条数同时<=0时不执行分页
	 * @param numPerPage
	 *            每页显示条数 当页码和每页显示条数同时<=0时不执行分页
	 * @return 返回List<POJO>
	 */
	public <T> List<T> searchByDivPage(Class<T> clazz, String[] fields,
			String[] values, int likeType, boolean isLike, boolean isNot,
			boolean isOR, String orderField, int oType, int currentPage,
			int numPerPage) throws DAOException;

	/**
	 * 请参照：public <T> List<T> searchByDivPage(Class<T> clazz, String[] fields,
	 * String[] values, int likeType, boolean isLike, boolean isNot, boolean
	 * isOR, String orderField, int oType, int currentPage, int numPerPage);
	 * 
	 * @param <T>
	 * @param clazz
	 * @param fields
	 * @param values
	 * @param likeType
	 * @param isLike
	 * @param isNot
	 * @param isOR
	 * @param orderField
	 * @param oType
	 * @return
	 */
	public <T> List<T> search(Class<T> clazz, String[] fields, String[] values,
			int likeType, boolean isLike, boolean isNot, boolean isOR,
			String orderField, int oType) throws DAOException;

	/**
	 * 精确查询
	 * 
	 * @param <T>
	 * @param clazz
	 *            持久化对象的class类型
	 * @param fields
	 *            给定字段名
	 * @param values
	 *            给定字段名对应的值
	 * @param orderField
	 *            排序字段
	 * @param orderType
	 *            排序方式：1升序，-1降序
	 * @return
	 */
	public <T> List<T> searchByExact(Class<T> clazz, String[] fields,
			String[] values, String orderField, int orderType, boolean isOR)
			throws DAOException;

	/**
	 * 请参照：public <T> List<T> searchByExact(Class<T> clazz, String[] fields,
	 * String[] values, String orderField, int orderType, boolean isOR);
	 * 
	 * @param <T>
	 * @param clazz
	 * @param fields
	 * @param values
	 * @param orderField
	 * @param orderType
	 * @param isOR
	 * @return
	 */
	public <T> List<T> searchByNotExact(Class<T> clazz, String[] fields,
			String[] values, String orderField, int orderType, boolean isOR)
			throws DAOException;

	/**
	 * 请参照：public <T> List<T> searchByNotExact(Class<T> clazz, String[] fields,
	 * String[] values, String orderField, int orderType, boolean isOR);
	 * 
	 * @param <T>
	 * @param t
	 * @param fields
	 * @param orderField
	 * @param orderType
	 * @param isOR
	 * @return
	 */
	public <T> List<T> searchByExact(T t, String[] fields, String orderField,
			int orderType, boolean isOR) throws DAOException;

	/**
	 * 请参照：public <T> List<T> searchByExact(T t, String[] fields, String
	 * orderField, int orderType, boolean isOR);
	 * 
	 * @param <T>
	 * @param t
	 * @param fields
	 * @param orderField
	 * @param orderType
	 * @param isOR
	 * @return
	 */
	public <T> List<T> searchByNotExact(T t, String[] fields,
			String orderField, int orderType, boolean isOR) throws DAOException;

	/**
	 * 请参照：
	 * 
	 * @param <T>
	 * @param clazz
	 * @param fields
	 * @param values
	 * @param orderType
	 * @param isOR
	 * @return
	 */
	public <T> List<T> searchByExactAndOrderByIdField(Class<T> clazz,
			String[] fields, String[] values, int orderType, boolean isOR)
			throws DAOException;

	public <T> List<T> searchByNotExactAndOrderByIdField(Class<T> clazz,
			String[] fields, String[] values, int orderType, boolean isOR)
			throws DAOException;

	public <T> List<T> searchByExactAndOrderByIdField(T t, String[] fields,
			int orderType, boolean isOR) throws DAOException;

	public <T> List<T> searchByNotExactAndOrderByIdField(T t, String[] fields,
			int orderType, boolean isOR) throws DAOException;

	public <T> List<T> searchByExactAndOrderByIdFieldDESC(Class<T> clazz,
			String[] fields, String[] values, boolean isOR) throws DAOException;

	public <T> List<T> searchByNotExactAndOrderByIdFieldDESC(Class<T> clazz,
			String[] fields, String[] values, boolean isOR) throws DAOException;

	public <T> List<T> searchByExactAndOrderByIdFieldDESC(T t, String[] fields,
			boolean isOR) throws DAOException;

	public <T> List<T> searchByNotExactAndOrderByIdFieldDESC(T t,
			String[] fields, boolean isOR) throws DAOException;

	/**
	 * 模糊查询
	 * 
	 * @param <T>
	 * @param clazz
	 *            给定类型
	 * @param fields
	 *            给定字段
	 * @param values
	 *            给定字段的值
	 * @param likeType
	 *            模糊匹配类型，-1左匹配 0全匹配 1正匹配
	 * @param orderField
	 *            排序字段
	 * @param orderType
	 *            排序类型 -1降序 1升序
	 * @return 返回list集合
	 */
	public <T> List<T> searchByLike(Class<T> clazz, String[] fields,
			String[] values, int likeType, String orderField, int orderType,
			boolean isOR) throws DAOException;

	public <T> List<T> searchByNotLike(Class<T> clazz, String[] fields,
			String[] values, int likeType, String orderField, int orderType,
			boolean isOR) throws DAOException;

	public <T> List<T> searchByLike(T t, String[] fields, int likeType,
			String orderField, int orderType, boolean isOR) throws DAOException;

	public <T> List<T> searchByNotLike(T t, String[] fields, int likeType,
			String orderField, int orderType, boolean isOR) throws DAOException;

	/**
	 * 模糊查询 按id排序
	 * 
	 * @param <T>
	 * @param clazz
	 * @param fields
	 * @param values
	 * @param likeType
	 * @param orderType
	 * @return
	 */
	public <T> List<T> searchByLikeAndOrderByIdField(Class<T> clazz,
			String[] fields, String[] values, int likeType, int orderType,
			boolean isOR) throws DAOException;

	public <T> List<T> searchByNotLikeAndOrderByIdField(Class<T> clazz,
			String[] fields, String[] values, int likeType, int orderType,
			boolean isOR) throws DAOException;

	public <T> List<T> searchByLikeAndOrderByIdField(T t, String[] fields,
			int likeType, int orderType, boolean isOR) throws DAOException;

	public <T> List<T> searchByNotLikeAndOrderByIdField(T t, String[] fields,
			int likeType, int orderType, boolean isOR) throws DAOException;

	public <T> List<T> searchByLikeAndOrderByIdFieldDESC(Class<T> clazz,
			String[] fields, String[] values, int likeType, boolean isOR)
			throws DAOException;

	public <T> List<T> searchByNotLikeAndOrderByIdFieldDESC(Class<T> clazz,
			String[] fields, String[] values, int likeType, boolean isOR)
			throws DAOException;

	public <T> List<T> searchByLikeAndOrderByIdFieldDESC(T t, String[] fields,
			int likeType, boolean isOR) throws DAOException;

	public <T> List<T> searchByNotLikeAndOrderByIdFieldDESC(T t,
			String[] fields, int likeType, boolean isOR) throws DAOException;
}
