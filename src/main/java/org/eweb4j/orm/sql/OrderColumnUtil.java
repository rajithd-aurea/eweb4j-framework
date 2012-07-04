package org.eweb4j.orm.sql;

import org.eweb4j.orm.DBType;

public class OrderColumnUtil {
	public static String getOrderColumn(String orderColumn, String dbType) {

		if (DBType.MYSQL_DB.equalsIgnoreCase(dbType)) {
			return orderColumn + "+0";

		} else if (DBType.MSSQL2000_DB.equalsIgnoreCase(dbType)
				|| DBType.MSSQL2005_DB.equalsIgnoreCase(dbType)) {
			return "CONVERT(INT," + orderColumn + ")";

		} else if (DBType.ORACLE_DB.equalsIgnoreCase(dbType)) {
			return "to_number(" + orderColumn + ")";
		}

		return orderColumn;
	}
}
