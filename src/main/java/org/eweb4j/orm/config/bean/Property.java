package org.eweb4j.orm.config.bean;

import org.eweb4j.util.xml.AttrTag;
import org.eweb4j.util.xml.Readonly;
import org.eweb4j.util.xml.Skip;

/**
 * ORM组件用来存取配置信息的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class Property {
	/**
	 * POJO属性名字
	 */
	@AttrTag
	private String name = "";
	/**
	 * 数据类型
	 */
	@AttrTag
	private String type = "";
	/**
	 * 数据表字段名
	 */
	@AttrTag
	private String column = "";
	
	@AttrTag
	private String unique = "";

	/**
	 * 关联哪个POJO
	 */
	@AttrTag
	@Readonly
	private String relBean = "";

	/**
	 * 关联POJO的哪个属性
	 */
	@AttrTag
	@Readonly
	private String relProperty = "";

	@AttrTag
	private String pk = "";

	@AttrTag
	@Readonly
	private String notNull = "";

	@AttrTag
	private String autoIncrement = "";

	@AttrTag
	private String size = "";
	
	@Skip
	private Class<?> relClass = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getRelClass() {
		return relClass;
	}

	public void setRelClass(Class<?> relClass) {
		this.relClass = relClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getRelBean() {
		return relBean;
	}

	public void setRelBean(String relBean) {
		this.relBean = relBean;
	}

	public String getRelProperty() {
		return relProperty;
	}

	public void setRelProperty(String relProperty) {
		this.relProperty = relProperty;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getNotNull() {
		return notNull;
	}

	public void setNotNull(String notNull) {
		this.notNull = notNull;
	}

	public String getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(String autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUnique() {
		return unique;
	}

	public void setUnique(String unique) {
		this.unique = unique;
	}

	@Override
	public String toString() {
		return "Property [name=" + name + ", type=" + type + ", column="
				+ column + ", unique=" + unique + ", relBean=" + relBean
				+ ", relProperty=" + relProperty + ", pk=" + pk + ", notNull="
				+ notNull + ", autoIncrement=" + autoIncrement + ", size="
				+ size + ", relClass=" + relClass + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((autoIncrement == null) ? 0 : autoIncrement.hashCode());
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((notNull == null) ? 0 : notNull.hashCode());
		result = prime * result + ((pk == null) ? 0 : pk.hashCode());
		result = prime * result + ((relBean == null) ? 0 : relBean.hashCode());
		result = prime * result
				+ ((relClass == null) ? 0 : relClass.hashCode());
		result = prime * result
				+ ((relProperty == null) ? 0 : relProperty.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((unique == null) ? 0 : unique.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Property other = (Property) obj;
		if (autoIncrement == null) {
			if (other.autoIncrement != null)
				return false;
		} else if (!autoIncrement.equals(other.autoIncrement))
			return false;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (notNull == null) {
			if (other.notNull != null)
				return false;
		} else if (!notNull.equals(other.notNull))
			return false;
		if (pk == null) {
			if (other.pk != null)
				return false;
		} else if (!pk.equals(other.pk))
			return false;
		if (relBean == null) {
			if (other.relBean != null)
				return false;
		} else if (!relBean.equals(other.relBean))
			return false;
		if (relClass == null) {
			if (other.relClass != null)
				return false;
		} else if (!relClass.equals(other.relClass))
			return false;
		if (relProperty == null) {
			if (other.relProperty != null)
				return false;
		} else if (!relProperty.equals(other.relProperty))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (unique == null) {
			if (other.unique != null)
				return false;
		} else if (!unique.equals(other.unique))
			return false;
		return true;
	}
}
