package org.eweb4j.orm.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.orm.jdbc.transaction.ConThreadLocal;

public class JdbcUtil {

	private static Log log = LogFactory.getORMLogger(JdbcUtil.class);

	/**
	 * 执行更新sql语句
	 * 
	 * @param con
	 * @param sqls
	 * @return
	 * @throws Exception
	 */
	public static Number[] update(Connection con, String[] sqls)
			throws JdbcUtilException {
		return updateWithArgs(con, sqls, null);
	}

	public static Number update(Connection con, String sql) {
		return updateWithArgs(con, sql, null);
	}

	/**
	 * 执行sql语句，更新操作，支持有？占位符
	 * 
	 * @param con
	 * @param sqls
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static Number[] batchUpdateWithArgs(Connection con, String sql, Object[][] args) throws JdbcUtilException {
		Number[] result = null;

		if (con == null || sql == null || sql.trim().length() == 0 || args == null || args.length == 0)
			return result;
	
		PreparedStatement pstmt = null;
		try {
			result = new Number[args.length];
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < args.length; ++i) {
				for (int j = 0; j < args[i].length; j++){
					pstmt.setObject(j+1, args[i][j]);
				}
				pstmt.addBatch();
			}
			
			int[] row = pstmt.executeBatch();
			if (row != null && row.length > 0) {
				if (sql.contains("INSERT INTO")){
					ResultSet rs = pstmt.getGeneratedKeys();
					int i = 0;
					while (rs.next()){
						result[i] = rs.getInt(1);
						i++;
					}
				}else{
					for (int i = 0; i < row.length; i++){
						result[i] = row[i];
					}
				}
			}
			pstmt.close();
			logToOrm(sql, args, result);
		} catch (SQLException e) {
			logException(new StringBuilder(sql), e);
			throw new JdbcUtilException(sql + " exception", e);
		} finally {
			close(null, pstmt, null);
			if (!ConThreadLocal.isTrans()) {
				close(con);
			}
		}

		return result;
	}
	
	/**
	 * 执行sql语句，更新操作，支持有？占位符
	 * 
	 * @param con
	 * @param sqls
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static Number[] updateWithArgs(Connection con, String[] sqls, Object[][] args) throws JdbcUtilException {
		Number[] result = null;

		if (con != null && sqls != null && sqls.length > 0) {
			StringBuilder sql = new StringBuilder();
			PreparedStatement pstmt = null;
			try {
				if (args != null && args.length > 0)
					if (args.length != sqls.length)
						throw new JdbcUtilException("args error ...", null);

				result = new Number[sqls.length];
				for (int i = 0; i < sqls.length; ++i) {
					sqls[i] = sqls[i].replace(";", "");
					sql.append(sqls[i]);
					pstmt = con.prepareStatement(sqls[i], Statement.RETURN_GENERATED_KEYS);
					fillArgs(args, pstmt, i);
					result[i] = pstmt.executeUpdate();
					if (result[i].intValue() > 0 && sqls[i].contains("INSERT INTO")) {
						ResultSet rs = pstmt.getGeneratedKeys();
						if (rs.next()){
							result[i] = rs.getInt(1);
						}
					}
					pstmt.close();
					logToOrm(sqls, args, result, i);
				}
			} catch (SQLException e) {

				logException(sql, e);
				throw new JdbcUtilException(sql + " exception", e);
			} finally {
				close(null, pstmt, null);
				if (!ConThreadLocal.isTrans()) {
					close(con);
				}
			}
		}

		return result;
	}

	/**
	 * 填充参数
	 * 
	 * @param args
	 * @param pstmt
	 * @param i
	 * @throws SQLException
	 */
	private static void fillArgs(Object[][] args, PreparedStatement pstmt, int i) throws SQLException {
		if (args == null || args.length == 0)
			return ;
		if (args[i] != null && args[i].length > 0)
			for (int j = 0; j < args[i].length; ++j) 
				pstmt.setObject(j + 1, args[i][j]);
	}

	/**
	 * 获取List数据
	 * 
	 * @param <T>
	 * @param con
	 * @param clazz
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getList(Connection con, Class<T> clazz, String sql)throws JdbcUtilException {
		return getListWithArgs(con, clazz, sql, null);
	}

	/**
	 * 获取List数据，支持？占位符
	 * 
	 * @param <T>
	 * @param con
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getListWithArgs(Connection con, Class<T> clazz,String sql, Object[] args) throws JdbcUtilException {
		return getListWithoutCache(con, clazz, sql, args);
	}

	/**
	 * 获取List数据
	 * 
	 * @param <T>
	 * @param con
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 * @throws JdbcUtilException
	 */
	public static <T> List<T> getListWithoutCache(Connection con, Class<T> clazz, String sql, Object[] args) throws JdbcUtilException {

		List<T> list = null;
		if (clazz == null || sql == null) {
			return null;
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if (con != null) {
			try {
				sql = sql.replace(";", "");
				pstmt = con.prepareStatement(sql);
				if (args != null && args.length > 0) {
					for (int i = 0; i < args.length; ++i) {
						pstmt.setString(i + 1, String.valueOf(args[i]));
					}
				}
				rs = pstmt.executeQuery();
				list = RowMapping.mapRows(rs, clazz);

				// ----------------------
				logOrm(sql, args, list);
			} catch (Exception e) {
				logException(sql, args, e);
				throw new JdbcUtilException(sql + " exception ", e);
			} finally {
				close(rs, pstmt, null);
				if (!ConThreadLocal.isTrans()) {
					close(con);
				}
			}
		}

		return list;
	}

	/**
	 * 更新
	 * 
	 * @param con
	 * @param sql
	 * @param args
	 * @return
	 * @throws JdbcUtilException
	 */
	public static Number updateWithArgs(Connection con, String sql,Object[] args) throws JdbcUtilException {
		Number[] res = updateWithArgs(con, new String[] { sql }, new Object[][] { args });
		return res == null ? 0 : res[0];
	}

	/**
	 * 获取
	 * 
	 * @param con
	 * @param sql
	 * @return
	 * @throws JdbcUtilException
	 */
	public static Object getObject(Connection con, String sql)throws JdbcUtilException {
		return getObject(con, sql, null);
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @param args
	 * @return
	 * @throws JdbcUtilException
	 */
	public static String getString(Connection con, String sql, Object[] args)throws JdbcUtilException {
		return String.valueOf(getObject(con, sql, args));
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @return
	 * @throws JdbcUtilException
	 */
	public static String getString(Connection con, String sql)throws JdbcUtilException {
		return getString(con, sql, null);
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @return
	 */
	public static Integer getInteger(Connection con, String sql) {
		String temp = getString(con, sql);
		int result = temp == null ? 0 : Integer.parseInt(temp);
		return result;
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @param args
	 * @return
	 */
	public static Integer getInteger(Connection con, String sql, Object... args) {
		String temp = getString(con, sql, args);
		int result = temp == null ? 0 : Integer.parseInt(temp);
		return result;
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @param args
	 * @return
	 * @throws JdbcUtilException
	 */
	public static Map<String, Object> getResultSetAsMap(Connection con,String sql, Object[] args) throws JdbcUtilException {
		Map<String, Object> result = null;
		if (sql == null) {
			return null;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		if (con != null) {
			try {
				sql = sql.replace(";", "");
				pstmt = con.prepareStatement(sql);
				if (args != null && args.length > 0) {
					for (int i = 0; i < args.length; ++i) {
						pstmt.setObject(i + 1, args[i]);
					}
				}
				rs = pstmt.executeQuery();
				rsmd = rs.getMetaData();
				List<String> columns = new ArrayList<String>();
				for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
					columns.add(rsmd.getColumnName(i));
				}
				while (rs.next()) {
					result = new HashMap<String, Object>();
					for (int i = 1; i <= columns.size(); ++i) {
						String name = columns.get(i - 1);
						result.put(name, rs.getObject(name));
					}
				}
				logOrm(sql, args, result);
			} catch (Exception e) {

				logException(sql, args, e);
				throw new JdbcUtilException(sql + " exception ", e);
			} finally {
				close(rs, pstmt, null);
				if (!ConThreadLocal.isTrans()) {
					close(con);
				}
			}
		}

		return result;
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @param args
	 * @return
	 * @throws JdbcUtilException
	 */
	public static Object getObject(Connection con, String sql, Object[] args) throws JdbcUtilException {
		return getResultSetAsList(con, sql, args).get(0);
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @param args
	 * @return
	 * @throws JdbcUtilException
	 */
	public static List<Object> getResultSetAsList(Connection con, String sql, Object[] args) throws JdbcUtilException {
		List<Object> result = null;
		Map<String, Object> map = getResultSetAsMap(con, sql, args);
		if (map != null) {
			result = new ArrayList<Object>();
			for (Entry<String, Object> entry : map.entrySet()) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	/**
	 * 关闭资源
	 * 
	 * @param rs
	 * @param pstmt
	 * @param con
	 * @throws Exception
	 */
	public static void close(ResultSet rs, PreparedStatement pstmt,Connection con) throws JdbcUtilException {
		try {
			if (rs != null)
				rs.close();

			if (pstmt != null)
				pstmt.close();

			if (con != null) {
				con.close();
			}

		} catch (SQLException e) {
			throw new JdbcUtilException("jdbc connection close exception...", e);
		}
	}

	public static void close(Connection con) {
		close(null, null, con);
	}

	private static void logException(StringBuilder sql, SQLException e) {
		StringBuilder sb = new StringBuilder(e.toString());
		for (StackTraceElement ste : e.getStackTrace())
			sb.append("\n").append(ste.toString());

		sb.append(sql.append("execute sql fail!").toString());
		log.error(sb.toString());
	}

	private static void logToOrm(String sql, Object[][] args, Number[] result) {

		StringBuilder sb = new StringBuilder();
		sb.append("execute sql：").append(sql);
		if (args != null && args.length > 0) {
			sb.append(" args->");
			for (Object[] arg : args){
				sb.append(Arrays.asList(arg)).append(",");
			}
		}
		sb.append(" result->").append(Arrays.asList(result)).append(";");
		log.debug(sb.toString());
	}
	
	private static void logToOrm(String[] sqls, Object[][] args, Number[] result, int i) {

		StringBuilder sb = new StringBuilder();
		sb.append("execute sql：").append(sqls[i]);
		if (args != null && args[i] != null && args.length > 0 && args[i].length > 0)
			sb.append(" args->").append(Arrays.asList(args[i]));
		sb.append(" result->").append(result[i]).append(";");
		log.debug(sb.toString());
	}

	private static void logException(String sql, Object[] args, Exception e) {
		StringBuilder sb = new StringBuilder(e.toString());
		for (StackTraceElement ste : e.getStackTrace()) {
			sb.append("\n").append(ste.toString());
		}
		sb.append(sql);
		if (args != null && args.length > 0)
			sb.append(" args->").append(Arrays.asList(args));
		sb.append(" execute sql fail!");
		log.error(sb.toString());
	}

	private static <T> void logOrm(String sql, Object[] args, List<T> list) {

		StringBuilder sb = new StringBuilder();
		int count = list == null ? 0 : list.size();
		sb.append("execute sql：").append(sql);
		if (args != null && args.length > 0)
			sb.append(" args->").append(Arrays.asList(args));
		sb.append(" affected rows：").append(count).append(";");
		log.debug(sb.toString());
	}

	private static void logOrm(String sql, Object[] args, Map<String, Object> result) {

		StringBuilder sb = new StringBuilder();
		sb.append("execute sql：").append(sql);
		if (args != null && args.length > 0)
			sb.append(" args->").append(Arrays.asList(args));
		sb.append(" result rows：").append(result.size()).append(";");
		log.debug(sb.toString());
	}

}
