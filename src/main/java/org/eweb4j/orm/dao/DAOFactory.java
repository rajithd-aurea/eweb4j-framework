package org.eweb4j.orm.dao;

import javax.sql.DataSource;

import org.eweb4j.cache.DBInfoConfigBeanCache;
import org.eweb4j.orm.dao.cascade.CascadeDAO;
import org.eweb4j.orm.dao.config.DAOConfigConstant;
import org.eweb4j.orm.dao.config.bean.DBInfoConfigBean;
import org.eweb4j.orm.dao.delete.DeleteDAO;
import org.eweb4j.orm.dao.delete.DeleteDAOImpl;
import org.eweb4j.orm.dao.insert.InsertDAO;
import org.eweb4j.orm.dao.insert.InsertDAOImpl;
import org.eweb4j.orm.dao.select.DivPageDAO;
import org.eweb4j.orm.dao.select.DivPageDAOImpl;
import org.eweb4j.orm.dao.select.SearchDAO;
import org.eweb4j.orm.dao.select.SearchDAOImpl;
import org.eweb4j.orm.dao.select.SelectDAO;
import org.eweb4j.orm.dao.select.SelectDAOImpl;
import org.eweb4j.orm.dao.update.UpdateDAO;
import org.eweb4j.orm.dao.update.UpdateDAOImpl;
import org.eweb4j.orm.jdbc.DataSourceWrapCache;


/**
 * DAO工厂，获取各种不同的DAO实例
 * 
 * @author CFuture.aw
 * @version 2011-05-11
 * @since 1.a.433
 */
public class DAOFactory {

	public static DAO getDAO(){
		return new DAOImpl("");
	}
	
	public static DAO getDAO(String dsName){
		return new DAOImpl(dsName);
	}
	
	public static DAO getDAO(Class<?> clazz, String dsName) {
		return new DAOImpl(clazz, dsName);

	}

	public static DAO getDAO(Class<?> clazz) {
		return new DAOImpl(clazz, null);

	}

	public static CascadeDAO getCascadeDAO(String dsName) {
		return new CascadeDAO(dsName);
	}

	public static CascadeDAO getCascadeDAO() {
		return new CascadeDAO(DAOConfigConstant.MYDBINFO);
	}

	/**
	 * 创建SelectDAOImpl实例
	 * 
	 * @param dsName
	 *            数据库配置信息Bean的名字，默认下在eweb4j-dbInfo-config.xml文件中<name></name>
	 *            找到这个值
	 * @return
	 */
	public static SelectDAO getSelectDAO(String dsName) {
		DataSource ds = DataSourceWrapCache.get(dsName);
		DBInfoConfigBean dcb = DBInfoConfigBeanCache.get(dsName);
		return new SelectDAOImpl(ds, dcb.getDataBaseType());
	}

	public static SelectDAO getSelectDAO() {
		DataSource ds = DataSourceWrapCache.get();

		DBInfoConfigBean dcb = DBInfoConfigBeanCache.get();

		return new SelectDAOImpl(ds, dcb.getDataBaseType());
	}

	/**
	 * 创建DeleteDAOImpl实例
	 * 
	 * @param dsName
	 *            数据库配置信息Bean的名字，默认下在eweb4j-dbInfo-config.xml文件中<name></name>
	 *            找到这个值
	 * @return
	 */
	public static DeleteDAO getDeleteDAO(String dsName) {
		DataSource ds = DataSourceWrapCache.get(dsName);
		return new DeleteDAOImpl(ds);
	}

	public static DeleteDAO getDeleteDAO() {
		DataSource ds = DataSourceWrapCache.get();
		return new DeleteDAOImpl(ds);
	}

	/**
	 * 创建UpdateDAOImpl实例
	 * 
	 * @param dsName
	 *            数据库配置信息Bean的名字，默认下在eweb4j-dbInfo-config.xml文件中<name></name>
	 *            找到这个值
	 * @return
	 */
	public static UpdateDAO getUpdateDAO(String dsName) {
		DataSource ds = DataSourceWrapCache.get(dsName);
		return new UpdateDAOImpl(ds);
	}

	public static UpdateDAO getUpdateDAO() {
		DataSource ds = DataSourceWrapCache.get();
		return new UpdateDAOImpl(ds);
	}

	/**
	 * 创建InsertDAOImpl实例
	 * 
	 * @param dsName
	 *            数据库配置信息Bean的名字，默认下在eweb4j-dbInfo-config.xml文件中<name></name>
	 *            找到这个值
	 * @return
	 */
	public static InsertDAO getInsertDAO(String dsName) {
		
		DataSource ds = DataSourceWrapCache.get(dsName);
		DBInfoConfigBean dcb = DBInfoConfigBeanCache.get(dsName);
		return new InsertDAOImpl(ds, dcb.getDataBaseType());
	}

	public static InsertDAO getInsertDAO() {
		DataSource ds = DataSourceWrapCache.get();
		DBInfoConfigBean dcb = DBInfoConfigBeanCache.get();
		return new InsertDAOImpl(ds, dcb.getDataBaseType());
	}

	/**
	 * 创建SearchDAOImpl实例
	 * 
	 * @param dsName
	 *            数据库配置信息Bean的名字，默认下在eweb4j-dbInfo-config.xml文件中<name></name>
	 *            找到这个值
	 * @return
	 */
	public static SearchDAO getSearchDAO(String dsName) {
		DataSource ds = DataSourceWrapCache.get(dsName);
		DBInfoConfigBean dcb = DBInfoConfigBeanCache.get(dsName);
		return new SearchDAOImpl(ds, dcb.getDataBaseType());
	}

	public static SearchDAO getSearchDAO() {
		DataSource ds = DataSourceWrapCache.get();
		DBInfoConfigBean dcb = DBInfoConfigBeanCache.get();
		return new SearchDAOImpl(ds, dcb.getDataBaseType());
	}

	/**
	 * 创建DivPageDAOImpl实例
	 * 
	 * @param dsName
	 *            数据库配置信息Bean的名字，默认下在eweb4j-dbInfo-config.xml文件中<name></name>
	 *            找到这个值
	 * @return
	 */
	public static DivPageDAO getDivPageDAO(String dsName) {
		DataSource ds = DataSourceWrapCache.get(dsName);
		DBInfoConfigBean dcb = DBInfoConfigBeanCache.get(dsName);
		return new DivPageDAOImpl(ds, dcb.getDataBaseType());
	}

	public static DivPageDAO getDivPageDAO() {
		DataSource ds = DataSourceWrapCache.get();
		DBInfoConfigBean dcb = DBInfoConfigBeanCache.get();
		return new DivPageDAOImpl(ds, dcb.getDataBaseType());
	}
}
