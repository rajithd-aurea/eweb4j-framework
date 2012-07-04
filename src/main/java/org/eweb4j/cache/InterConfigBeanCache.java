package org.eweb4j.cache;

import java.util.ArrayList;
import java.util.List;

import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.mvc.config.bean.InterConfigBean;

/**
 * 拦截器缓存
 * @author CFurure.aw
 *
 */
public class InterConfigBeanCache {
	
	private static Log log = LogFactory.getConfigLogger(InterConfigBeanCache.class);
	
	private static final List<InterConfigBean> ht = new ArrayList<InterConfigBean>();

	public static List<InterConfigBean> getList() {
		return ht;
	}

	public static void add(InterConfigBean o) {
		if (o != null) {
			String info = null;
			if (!ht.contains(o)) {
				ht.add(o);
				info = "add...finished..." + o;
				log.debug(info);
			}
		}
	}

	public static void clear() {
		if (!ht.isEmpty()) {
			ht.clear();
		}
	}
}
