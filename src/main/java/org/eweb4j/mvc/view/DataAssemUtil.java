package org.eweb4j.mvc.view;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.eweb4j.orm.config.ORMConfigBeanUtil;
import org.eweb4j.util.ClassUtil;
import org.eweb4j.util.ReflectUtil;


/**
 * 数据组装工具类
 * 
 * @author weiwei
 * 
 */
public class DataAssemUtil {
	public static <T> ListPage assemHead(ListPage listPage,
			Map<String, String> prop) throws Exception {
		if (listPage != null && listPage.getPojos() != null)
			return assemHead(listPage, listPage.getPojos(), prop);
		else
			return assemHead(listPage, null, prop);
	}

	public static <T> ListPage assemHead(ListPage listPage, List<T> pojos,
			Map<String, String> prop) throws Exception {
		if (listPage == null || pojos == null || prop == null)
			return listPage;

		boolean flag = false;
		for (T pojo : pojos) {
			if (pojo == null)
				continue;

			Object idVal = ORMConfigBeanUtil.getIdVal(pojo);
			if (idVal == null)
				continue;

			TRData data = getData(listPage, prop, null, null, pojo, flag);
			data.setId(String.valueOf(idVal));
			flag = true;

			if (data.getDatas() == null || data.getDatas().size() == 0)
				continue;

			listPage.getTrdatas().add(data.clone());
			// System.out.println(">>>>>>>data>>>>" + data);
		}

		return listPage;
	}

	private static <T> TRData getData(ListPage listPage,
			Map<String, String> prop, TRData data, String scope, T pojo,
			boolean flag) throws IllegalAccessException,
			InvocationTargetException, Exception {
		ReflectUtil ru = new ReflectUtil(pojo);
		Field[] fields = ru.getFields();

		if (data == null)
			data = new TRData();

		for (Field field : fields) {
			String _name = field.getName();
			Method getter = ru.getGetter(_name);

			if (getter == null)
				continue;

			Object getterVal = getter.invoke(pojo);

			String name = null;
			if (scope != null)
				name = scope + "." + _name;
			else
				name = _name;

			if (getterVal != null && ClassUtil.isPojo(field.getType()))
				getData(listPage, prop, data, name, getterVal, flag);

			if (getterVal == null) {
				getterVal = " ";
			}

			String property = prop.get(name);
			// System.out.println("name-->" + name + " | property-->" +
			// property);
			if (property == null)
				continue;

			// System.out.println(name + "-->" + getterVal);

			data.getDatas().add(String.valueOf(getterVal));

			// 获取属性名字一次即可
			if (flag)
				continue;

			String[] info = property.split(",");
			THeadCell cell = new THeadCell();
			cell.setLabel(info[0]);
			if (info.length > 2)
				cell.setWidth(info[1]);
			else
				cell.setWidth("");

			listPage.getThead().add(cell);
			// System.out.println(">>>>>>>cell>>>>" + cell);
		}

		// System.out.println("trdata-->" + data);
		return data;
	}
}
