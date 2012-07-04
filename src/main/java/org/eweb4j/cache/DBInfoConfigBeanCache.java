package org.eweb4j.cache;

import java.util.HashMap;

import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.orm.dao.config.DAOConfigConstant;
import org.eweb4j.orm.dao.config.bean.DBInfoConfigBean;

/**
 * 全局单件对象缓存
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class DBInfoConfigBeanCache {

	private static Log log = LogFactory
			.getConfigLogger(DBInfoConfigBeanCache.class);

	private static final HashMap<String, DBInfoConfigBean> ht = new HashMap<String, DBInfoConfigBean>();

	public static boolean containsKey(String beanID) {
		return ht.containsKey(beanID);
	}

	public static void add(String beanID, DBInfoConfigBean o) {
		if (beanID != null && o != null) {
			String info = null;
			if (!ht.containsKey(beanID)) {
				ht.put(beanID, o);
				info = "DBInfoConfigBeanCache:add...finished..." + o;
			}

			log.debug(info);
		}
	}

	public static DBInfoConfigBean get(String beanID) {
		if (beanID == null || beanID.trim().length() == 0)
			return get();

		DBInfoConfigBean dcb = ht.get(beanID);
		return dcb;
	}

	public static DBInfoConfigBean get() {
		return ht.get(DAOConfigConstant.MYDBINFO);
	}

	public static void clear() {
		if (!ht.isEmpty()) {
			ht.clear();
		}
	}
}
