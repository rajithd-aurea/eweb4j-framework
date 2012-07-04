package org.eweb4j.orm.dao.select;

import java.util.List;

import org.eweb4j.orm.dao.DAOException;


/**
 * <b>分页DAO</b>
 * 
 * @author CFuture.aw
 * @since 1.a.433
 * @version 2011-05-10
 * 
 */
public interface DivPageDAO {
	/**
	 * <b>给定一个行号（例如id值），返回它的下一条记录。</b> <code>
	 * 假设数据表有6条记录，id为1-6连续
	 *  pet.setId(5);
	 *  pet = nextOne(pet);
	 *  pet.getId() ——> 6
	 * </code>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @return 返回POJO对象
	 */
	public <T> T nextOne(T t) throws DAOException;

	/**
	 * <b>给定一个可以排序的字段名和值，返回它的下一条记录</b> <code>
	 * 假设表中age字段值有4,5,6...
	 * pet = nextOne(Pet.class,"age",4);
	 * pet.getAge() ——> 5
	 * </code>
	 * 
	 * @param <T> POJO类型
	 * @param clazz POJO的class对象
	 * @param field 给定字段名
	 * @param value 给定字段值
	 * @return 返回POJO对象
	 */
	public <T> T nextOne(Class<T> clazz, String field, int value) throws DAOException;

	/**
	 * <b>给定一个行号（例如id值），返回它的上一条记录。</b> <code>
	 * 假设数据表有6条记录，id为1-6连续
	 *  pet.setId(5);
	 *  pet = preOne(pet);
	 *  pet.getId() ——> 4
	 * </code>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @return 返回POJO对象
	 */
	public <T> T preOne(T t) throws DAOException;

	/**
	 * <b>给定一个可以排序的字段名和值，返回它的上一条记录</b> <code>
	 * 假设表中age字段值有3,,4,5,6...
	 * pet = preOne(Pet.class,"age",4);
	 * pet.getAge() ——> 3
	 * </code>
	 * 
	 * @param <T> POJO类型
	 * @param clazz POJO的class对象
	 * @param field 给定字段名
	 * @param value 给定字段值
	 * @return 返回POJO对象
	 */
	public <T> T preOne(Class<T> clazz, String field, int value) throws DAOException;

	/**
	 * <b>分页,有额外条件</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param currPage
	 *            当前页号
	 * @param numPerPage
	 *            每页显示多少条
	 * @param orderField
	 *            排序字段名
	 * @param orderType
	 *            排序类型1升序-1降序
	 * @param condition
	 *            额外查询条件
	 * @param args
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByWhere(Class<T> clazz, int currPage,
			int numPerPage, String orderField, int orderType, String condition,Object[] args) throws DAOException;

	/**
	 * <b>分页</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param currPage
	 *            当前页号
	 * @param numPerPage
	 *            每页显示多少条
	 * @param orderField
	 *            排序字段名
	 * @param orderType
	 *            排序类型1升序-1降序
	 * @return
	 */
	public <T> List<T> divPage(Class<T> clazz, int currPage, int numPerPage,
			String orderField, int orderType) throws DAOException;

	/**
	 * <b>分页，对“id”字段排序</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示多少条
	 * @param orderType
	 *            排序类型 1升序 -1降序
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPage(Class<T> clazz, int currPage, int numPerPage,
			int orderType) throws DAOException;

	/**
	 * <b>分页，额外条件</b>
	 * 
	 * @param <T>
	 * @param clazz
	 * @param currPage 为负数的时候不分页
	 * @param numPerPage 为负数的时候不分页
	 * @param orderType
	 * @param condition 支持占位符
	 * @param args 占位符对应参数值
	 * @return
	 */
	public <T> List<T> divPageByWhere(Class<T> clazz, int currPage,
			int numPerPage, int orderType, String condition,Object[] args) throws DAOException;

	/**
	 * <b>分页, 按"id"字段排序且降序</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPage(Class<T> clazz, int currPage, int numPerPage) throws DAOException;

	/**
	 * <b>分页，按“id”排降序，额外条件</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class类
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @param condition
	 *            额外条件
	 * @param args
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByWhere(Class<T> clazz, int currPage,
			int numPerPage, String condition,Object[] args) throws DAOException;

	/**
	 * <b>分页，给定字段名等于给定字段值作为条件</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param fields
	 *            给定字段名
	 * @param values
	 *            给定字段值
	 * @param orderField
	 *            排序字段名
	 * @param orderType
	 *            排序类型1升序-1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldIsValue(Class<T> clazz, String[] fields,
			String[] values, String orderField, int orderType, int currPage,
			int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，给定字段名不等于给定字段值作为条件</b> 注意是<b>不等于</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param fields
	 *            给定字段名
	 * @param values
	 *            给定字段值
	 * @param orderField
	 *            排序字段名
	 * @param orderType
	 *            排序类型 1升序 -1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldNotIsValue(Class<T> clazz,
			String[] fields, String[] values, String orderField, int orderType,
			int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，给定字段名等于从pojo对象取出对应值来作为条件</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO的对象
	 * @param fields
	 *            给定字段名
	 * @param orderField
	 *            排序字段名
	 * @param orderType
	 *            排序类型 1升序-1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldIsValue(T t, String[] fields,
			String orderField, int orderType, int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，给定字段名不等于从pojo对象里取出对应的值作为条件</b> 注意是<b>不等于</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @param fields
	 *            给定字段名
	 * @param orderField
	 *            排序字段名
	 * @param orderType
	 *            排序类型 1升序 -1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldNotIsValue(T t, String[] fields,
			String orderField, int orderType, int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，对“id”排序，给定字段名等于给定字段值作为条件</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param fields
	 *            给定字段名
	 * @param values
	 *            给定字段值
	 * @param orderType
	 *            排序类型 1升序 -1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldIsValue(Class<T> clazz, String[] fields,
			String[] values, int orderType, int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，对“id”排序，给定字段名不等于给定字段值作为条件</b> 注意是<b>不等于</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param fields
	 *            给定字段名
	 * @param values
	 *            给定字段值
	 * @param orderType
	 *            排序类型 1升序 -1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldNotIsValue(Class<T> clazz,
			String[] fields, String[] values, int orderType, int currPage,
			int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，按“id”排序，给定字段名等于从pojo对象里取出来的对应值作为条件</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @param fields
	 *            给定字段名
	 * @param orderType
	 *            排序类型 1升序 -1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return
	 */
	public <T> List<T> divPageByFieldIsValues(T t, String[] fields,
			int orderType, int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，对“id”排序，给定字段名不等于从pojo对象里取出来的对应值作为条件</b> 注意是<b>不等于</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @param fields
	 *            给定字段名
	 * @param orderType
	 *            排序类型 1升序 -1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldNotIsValues(T t, String[] fields,
			int orderType, int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，按“id”排降序，给定字段名等于给定字段值</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param fields
	 *            给定字段名
	 * @param values
	 *            给定字段值
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldIsValue(Class<T> clazz, String[] fields,
			String[] values, int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，按“id”排降序，给定字段名不等于给定字段值作为条件</b> 注意是<b>不等于</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param clazz
	 *            POJO的class对象
	 * @param fields
	 *            给定字段名
	 * @param values
	 *            给定字段值
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldNotIsValue(Class<T> clazz,
			String[] fields, String[] values, int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，按“id”排降序，给定字段名等于从pojo对象取出来的对应值作为条件</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @param fields
	 *            给定字段值
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldIsValue(T t, String[] fields,
			int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，按“id”排降序，给定字段值不等于从pojo对象取出来的对应值作为条件</b> 注意是<b>不等于</b>
	 * 
	 * @param <T>
	 * @param t
	 * @param columns
	 * @param currPage
	 * @param numPerPage
	 * @return
	 */
	public <T> List<T> divPageByFieldNotIsValue(T t, String[] fields,
			int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，给定字段名模糊匹配给定字段值</b>
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
	 *            模糊匹配类型，-1左匹配，0全匹配，1右匹配
	 * @param orderField
	 *            排序字段名
	 * @param orderType
	 *            排序类型，1升序-1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldLikeValue(Class<T> clazz, String[] fields,
			String[] values, int likeType, String orderField, int orderType,
			int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，给定字段值“不模糊匹配”给定字段值作为条件</b> 注意是<b>不模糊匹配</b>
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
	 *            模糊匹配类型，-1左匹配，0全匹配，1右匹配
	 * @param orderField
	 *            排序字段名
	 * @param orderType
	 *            排序类型，1升序-1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldNotLikeValue(Class<T> clazz, String[] fields,
			String[] values, int likeType, String orderField, int orderType,
			int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，给定字段名模糊匹配从pojo对象取出来的 对应值作为条件。</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @param fields
	 *            给定字段名
	 * @param likeType
	 *            模糊匹配类型，-1左匹配，0全匹配，1右匹配
	 * @param orderField
	 *            排序字段名
	 * @param orderType
	 *            排序，1升序，-1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return List<POJO>
	 */
	public <T> List<T> divPageByFieldLikeValue(T t, String[] fields,
			int likeType, String orderField, int orderType, int currPage,
			int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，给定字段名不模糊匹配从pojo对象里取出来的对应值作为条件</b> 注意是<b>不模糊匹配</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @param fields
	 *            给定字段名
	 * @param likeType
	 *            匹配类型，-1左匹配，0全匹配，1右匹配
	 * @param orderField
	 *            排序字段
	 * @param orderType
	 *            排序类型，1升序，-1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldNotLikeValue(T t, String[] fields,
			int likeType, String orderField, int orderType, int currPage,
			int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，按“id”字段排序，给定字段名模糊匹配给定字段值作为条件</b>
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
	 *            匹配类型，-1左匹配，0全匹配，1右匹配
	 * @param orderType
	 *            排序类型，1升序，-1降序
	 * @param currPage
	 * @param numPerPage
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldLikeValue(Class<T> clazz, String[] fields,
			String[] values, int likeType, int orderType, int currPage,
			int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，按“id”排序，给定字段名不模糊匹配给定字段值作为条件</b>注意是<b>不模糊匹配</b>
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
	 *            匹配类型，-1左匹配，0全匹配，1右匹配
	 * @param orderType
	 *            排序类型，1升序，-1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldNotLikeValue(Class<T> clazz,
			String[] fields, String[] values, int likeType, int orderType,
			int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，按“id”排序，给定字段名模糊匹配从pojo对象取出的对应值作为条件。</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @param fields
	 *            给定字段名
	 * @param likeType
	 *            匹配类型，-1左匹配，0全匹配，1右匹配
	 * @param orderType
	 *            排序类型，1升序，-1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldLikeValue(T t, String[] fields,
			int likeType, int orderType, int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，按“id”排序，给定字段名不模糊匹配从pojo对象取出的对应值作为条件。</b>注意是<b>不模糊匹配</b>
	 * 
	 * @param <T>
	 *            POJO类型
	 * @param t
	 *            POJO对象
	 * @param fields
	 *            给定字段名
	 * @param likeType
	 *            匹配类型，-1左匹配，0全匹配，1右匹配
	 * @param orderType
	 *            排序类型，1升序，-1降序
	 * @param currPage
	 *            当前页码
	 * @param numPerPage
	 *            每页显示条数
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldNotLikeValue(T t, String[] fields,
			int likeType, int orderType, int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，按“id”字段排降序，给定字段名模糊匹配给定字段值作为条件</b>
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
	 *            匹配类型，-1左匹配，0全匹配，1右匹配
	 * @param currPage
	 * @param numPerPage
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldLikeValue(Class<T> clazz, String[] fields,
			String[] values, int likeType, int currPage, int numPerPage,boolean isOR) throws DAOException;

	/**
	 * <b>分页，按“id”字段排降序，给定字段名不模糊匹配给定字段值作为条件</b>注意是<b>不模糊匹配</b>
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
	 *            匹配类型，-1左匹配，0全匹配，1右匹配
	 * @param currPage
	 * @param numPerPage
	 * @return 返回List<POJO>
	 */
	public <T> List<T> divPageByFieldNotLikeValue(Class<T> clazz, String[] fields,
			String[] values, int likeType, int currPage, int numPerPage,boolean isOR) throws DAOException;

}
