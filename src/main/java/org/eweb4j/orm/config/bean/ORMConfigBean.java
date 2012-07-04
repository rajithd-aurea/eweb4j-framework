package org.eweb4j.orm.config.bean;

import java.util.ArrayList;
import java.util.List;

import org.eweb4j.util.xml.AttrTag;

/**
 * ORM组件用来存取配置信息的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class ORMConfigBean {
	@AttrTag
	private String id;
	
	@AttrTag
	private String table;

	@AttrTag
	private String clazz;

	private List<Property> property = new ArrayList<Property>();

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public List<Property> getProperty() {
		return property;
	}

	public void setProperty(List<Property> property) {
		this.property = property;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "ORMConfigBean [id=" + id + ", table=" + table + ", clazz="
				+ clazz + ", property=" + property + "]";
	}

}
