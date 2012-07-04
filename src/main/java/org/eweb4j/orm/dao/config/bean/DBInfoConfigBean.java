package org.eweb4j.orm.dao.config.bean;

import java.util.ArrayList;
import java.util.List;

import org.eweb4j.orm.DBType;
import org.eweb4j.orm.dao.config.DAOConfigConstant;
import org.eweb4j.util.xml.AttrTag;

/**
 * DAO组件用来存取配置信息 的bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class DBInfoConfigBean {

	@AttrTag
	private String dsName = DAOConfigConstant.MYDBINFO;

	private String dataBaseType = DBType.MYSQL_DB;

	private List<Property> property = new ArrayList<Property>();

	public String getDsName() {
		return dsName;
	}

	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	public List<Property> getProperty() {
		if (this.property.isEmpty() || this.property.size() == 1
				&& "".equals(this.property.get(0).getKey()) 
				&& "".equals(this.property.get(0).getValue())) {
			Property p0 = new Property();
			p0.setKey("driverClass");
			p0.setValue("com.mysql.jdbc.Driver");
			property.add(p0);

			Property p1 = new Property();
			p1.setKey("jdbcUrl");
			p1.setValue("jdbc:mysql://localhost:3306/test");
			property.add(p1);

			Property p2 = new Property();
			p2.setKey("user");
			p2.setValue("root");
			property.add(p2);

			Property p3 = new Property();
			p3.setKey("password");
			p3.setValue("root");
			property.add(p3);
		}
		
		return property;
	}

	public void setProperty(List<Property> properties) {
		this.property = properties;
	}

	public String getDataBaseType() {
		return dataBaseType;
	}

	public void setDataBaseType(String dataBaseType) {
		this.dataBaseType = dataBaseType;
	}

	@Override
	public String toString() {
		return "DBInfoConfigBean [dsName=" + dsName + ", dataBaseType="
				+ dataBaseType + ", property=" + property + "]";
	}

}
