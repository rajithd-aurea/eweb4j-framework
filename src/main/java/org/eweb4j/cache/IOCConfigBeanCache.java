package org.eweb4j.cache;

import java.util.Hashtable;

import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.ioc.config.bean.IOCConfigBean;


/**
 * IOC组件缓存
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class IOCConfigBeanCache {
	private static Log log = LogFactory.getConfigLogger(IOCConfigBeanCache.class);
	
	private static final Hashtable<Object, IOCConfigBean> ht = new Hashtable<Object, IOCConfigBean>();

	public static boolean containsKey(String beanID) {
		if (beanID == null)
			return false;
		
		return ht.containsKey(beanID);
	}

	public static boolean containsKey(Class<?> clazz) {
		if (clazz == null)
			return false;
		
		return ht.containsKey(clazz);
	}

	public static void add(String beanID, IOCConfigBean o) {
		String info = null;
		if (!ht.containsKey(beanID)) {
			ht.put(beanID, o);
			info = "IOCConfigBeanCache:add...finished..." + o;
			log.debug(info);
		}
	}

	public static void add(Class<?> clazz, IOCConfigBean o) {
		String info = null;
		if (!ht.containsKey(clazz)) {
			ht.put(clazz, o);
			info = "IOCConfigBeanCache:add...finished..." + o;
			log.debug(info);
		}
	}

	public static IOCConfigBean get(String beanID) {
		IOCConfigBean o = null;
		if (ht.containsKey(beanID))
			o = ht.get(beanID);

		return o;
	}

	public static IOCConfigBean get(Class<?> clazz) {
		IOCConfigBean o = null;
		if (ht.containsKey(clazz))
			o = ht.get(clazz);

		return o;
	}

	public static void clear() {
		if (!ht.isEmpty())
			ht.clear();
	}
}
