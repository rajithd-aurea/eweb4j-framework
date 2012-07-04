package org.eweb4j.orm.jdbc;

import java.lang.reflect.Method;
import java.util.List;

import javax.sql.DataSource;

import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.orm.dao.config.bean.DBInfoConfigBean;
import org.eweb4j.orm.dao.config.bean.Property;
import org.eweb4j.util.ReflectUtil;


/**
 * 创建真正的标准的数据源
 * 
 * @author weiwei
 * 
 */
public class DataSourceCreator {

	public static DataSource create(final DBInfoConfigBean dbInfo)
			throws Exception {

		ConfigBean cb = (ConfigBean) SingleBeanCache
				.get(ConfigConstant.CONFIGBEAN_ID);
		Class<?> cls = Class.forName(cb.getOrm().getDataSource());
		DataSource ds = (DataSource) cls.newInstance();

		List<Property> properties = dbInfo.getProperty();

		// 通过反射将配置信息注入到数据源对象中
		for (Property property : properties) {
			String name = property.getKey();
			String value = property.getValue();
			ReflectUtil ru2 = new ReflectUtil(ds);
			Method m2 = ru2.getSetter(name);
			if (m2 == null)
				continue;

			Class<?> clazz = m2.getParameterTypes()[0];
			if (int.class.isAssignableFrom(clazz)
					|| Integer.class.isAssignableFrom(clazz)
					|| long.class.isAssignableFrom(clazz)
					|| Long.class.isAssignableFrom(clazz))
				m2.invoke(ds, Integer.parseInt(value));
			else if (float.class.isAssignableFrom(clazz)
					|| Float.class.isAssignableFrom(clazz)
					|| double.class.isAssignableFrom(clazz)
					|| Double.class.isAssignableFrom(clazz))
				m2.invoke(ds, Float.parseFloat(value));
			else if (boolean.class.isAssignableFrom(clazz)
					|| Boolean.class.isAssignableFrom(clazz))
				m2.invoke(ds, Boolean.parseBoolean(value));
			else
				m2.invoke(ds, value);

		}

		return ds;
	}
}
