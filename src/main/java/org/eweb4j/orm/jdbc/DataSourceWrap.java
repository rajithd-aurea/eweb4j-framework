package org.eweb4j.orm.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.eweb4j.cache.DBInfoConfigBeanCache;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.orm.dao.config.DAOConfigConstant;
import org.eweb4j.orm.jdbc.transaction.ConThreadLocal;
import org.eweb4j.util.StringUtil;

import com.mchange.v2.c3p0.DataSources;

/**
 * 数据源包装器
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public final class DataSourceWrap implements DataSource {

	private static Log log = LogFactory.getORMLogger(DataSourceWrap.class);

	private String dsName;
	private DataSource ds;// 真正的连接池

	private void init(String dsName, DataSource ds) {
		this.ds = ds;
		this.dsName = dsName == null ? DAOConfigConstant.MYDBINFO : dsName;
	}

	public DataSourceWrap() {
	}

	public DataSourceWrap(String dsName, DataSource ds) {
		init(dsName, ds);
	}

	public DataSourceWrap(DataSource ds) {
		init(null, ds);
	}

	/**
	 * 
	 */

	public Connection getConnection() {
		try {
			Connection con = null;

			// 判断当前线程是否开启了事务锁
			// 若没有开启，则正常的从数据源获取一条连接
			if (!ConThreadLocal.isTrans())
				return ds.getConnection();// 这个是真正的数据源取出来的连接对象

			// 若开启，则从当前线程连接池中获取数据库连接
			// 这样能保证当前线程下任何地方获取的数据库连接都是唯一的
			con = ConThreadLocal.getCon(dsName);
			if (con == null) {
				// 如果没有，就从连接池取出来一条
				con = ds.getConnection();
				con.setAutoCommit(false);
				// 然后放入到本地线程变量中保存
				ConThreadLocal.put(dsName, con);
			}

			return con;
		} catch (SQLException e) {
			log.error(StringUtil.getExceptionString(e));
			// try {
			// this.finalize();
			// } catch (Throwable e1) {
			// e1.printStackTrace();
			// }
		}

		return null;
	}

	protected void finalize() throws Throwable {
		DataSourceWrapCache.remove(this.dsName);
		DataSources.destroy(this.ds); // 关闭datasource
		DataSource _ds = DataSourceCreator.create(DBInfoConfigBeanCache
				.get(dsName));
		DataSourceWrap dsw = new DataSourceWrap(dsName, _ds);
		DataSourceWrapCache.put(dsName, dsw);
		super.finalize();

	}

	public String getDsName() {
		return dsName;
	}

	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
