package org.eweb4j.config.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-18 上午11:27:53
 */
public class Listeners {

	List<ListenerBean> listener = new ArrayList<ListenerBean>();

	public List<ListenerBean> getListener() {
		return this.listener;
	}

	public void setListener(List<ListenerBean> listener) {
		this.listener = listener;
	}
	
}
