package org.eweb4j.config.bean;

import org.eweb4j.util.xml.AttrTag;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-18 上午11:28:45
 */
public class ListenerBean {

	@AttrTag
	private String clazz;

	public String getClazz() {
		return this.clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
}
