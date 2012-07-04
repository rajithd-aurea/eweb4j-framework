package org.eweb4j.orm.config;

import java.util.ArrayList;
import java.util.List;

import org.eweb4j.orm.config.bean.ORMConfigBean;
import org.eweb4j.orm.config.bean.Property;

/**
 * ORM组件存取配置信息bean的创建
 * @author cfuture.aw
 * @since v1.a.0
 */
public class ORMConfigBeanCreator {
	public static ORMConfigBean getORMBean(){
		ORMConfigBean result = new ORMConfigBean();
		List<Property> pList = new ArrayList<Property>();
		Property p = new Property();
		pList.add(p);
		result.setProperty(pList);
		return result;
	}
}
