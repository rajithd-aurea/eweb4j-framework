package org.eweb4j.orm.jdbc;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.orm.dao.config.DAOConfigConstant;

/**
 * 数据源的缓存池
 * 
 * 注意：实际上存入的都是 “数据源包装器”
 * 
 * @author weiwei
 * 
 */
public class DataSourceWrapCache {

	private static Log log = LogFactory.getORMLogger(DataSourceWrapCache.class);

	private static Hashtable<String, DataSource> ht = new Hashtable<String, DataSource>();

	public static void put(String key, DataSource ds) {
		ht.put(key, ds);
	}

	public static DataSource get(String key) {
		if (key == null || key.trim().length() == 0)
			key =DAOConfigConstant.MYDBINFO;
		
		DataSource ds = ht.get(key);
		if (ds == null) {
			log.error("can not create DataSource");
			throw new RuntimeException("can not create DataSource");
		}
		return ds;
	}

	public static DataSource get() {
		return get(null);
	}

	public static boolean containsKey(String key) {
		return ht.containsKey(key);
	}

	public static boolean contains(DataSource ds) {
		return ht.containsValue(ds);
	}

	public static boolean containsValue(DataSource ds) {
		return ht.containsValue(ds);
	}

	public static void clear() {
		ht.clear();
	}

	public static void remove(String dsName) {
		ht.remove(dsName);
	}

	public static Set<Entry<String, DataSource>> entrySet() {
		return ht.entrySet();
	}
}
