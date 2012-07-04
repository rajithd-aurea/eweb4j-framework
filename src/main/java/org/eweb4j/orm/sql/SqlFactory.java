package org.eweb4j.orm.sql;

/**
 * 一个用来生成sql语句的工厂
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class SqlFactory {
	public static <T> InsertSqlCreator<T> getInsertSql(T... ts) {
		return new InsertSqlCreator<T>(ts);
	}

	public static <T> SelectSqlCreator<T> getSelectSql(T t, String dbType) {
		return new SelectSqlCreator<T>(t, dbType);
	}

	public static <T> DeleteSqlCreator<T> getDeleteSql(T... ts) {
		return new DeleteSqlCreator<T>(ts);
	}

	public static <T> UpdateSqlCreator<T> getUpdateSql(T... ts) {
		return new UpdateSqlCreator<T>(ts);
	}
}
