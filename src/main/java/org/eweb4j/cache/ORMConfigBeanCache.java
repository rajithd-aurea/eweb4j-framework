package org.eweb4j.cache;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.orm.config.bean.ORMConfigBean;


/**
 * ORM组件缓存
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class ORMConfigBeanCache {
	private static Log log = LogFactory.getConfigLogger(ORMConfigBeanCache.class);
	
	private static final HashMap<Object, ORMConfigBean> ht = new HashMap<Object, ORMConfigBean>();

	public static boolean containsKey(String beanID) {
		return ht.containsKey(beanID);
	}

	public static boolean containsKey(Class<?> clazz) {
		return ht.containsKey(clazz);
	}

	public static void add(String beanID, ORMConfigBean o) {
		if (beanID != null && o != null) {
			String info = null;
			if (!ht.containsKey(beanID)) {
				ht.put(beanID, o);
				info = "ORMConfigBeanCache:add...finished..." + o;
				log.debug(info);
			}
		}
	}

	public static void add(Class<?> clazz, ORMConfigBean o) {
		if (clazz != null && o != null) {
			String info = null;
			if (!ht.containsKey(clazz)) {
				ht.put(clazz, o);
				info = "ORMConfigBeanCache:add...finished..." + o;
				log.debug(info);
			}
		}
	}

	public static ORMConfigBean get(String beanID) {

		return ht.get(beanID);
	}

	public static ORMConfigBean get(Class<?> clazz) {

		return ht.get(clazz);
	}

	public static void clear() {
		if (!ht.isEmpty())
			ht.clear();

	}
	
	public static Set<?> keySet(){
		return ht.keySet();
	}
	
	public static Set<Entry<Object, ORMConfigBean>> entrySet(){
		return ht.entrySet();
	}
}
