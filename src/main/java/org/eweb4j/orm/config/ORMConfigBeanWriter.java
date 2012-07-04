package org.eweb4j.orm.config;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.eweb4j.config.ConfigConstant;
import org.eweb4j.orm.annotation.Pk;
import org.eweb4j.orm.config.bean.ORMConfigBean;
import org.eweb4j.orm.config.bean.Property;
import org.eweb4j.util.ReflectUtil;
import org.eweb4j.util.xml.BeanXMLUtil;


/**
 * 将ORM配置信息写入xml文件
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class ORMConfigBeanWriter {
	/**
	 * 将Class集合写入到目标xml文件中
	 * 
	 * @param filePath
	 *            文件相对路径
	 * @param list
	 * @return
	 */
	public static String write(String filePath, List<Class<?>> list) {
		String error = null;
		if (filePath != null && list != null && !list.isEmpty()) {
			Class<?>[] clss = new Class<?>[list.size()];
			for (int i = 0; i < list.size(); ++i) {
				clss[i] = list.get(i);
			}

			error = write(filePath, clss);
		}

		return error;
	}

	/**
	 * 将某个Class写入到目标xml文件中
	 * 
	 * @param filePath
	 *            文件相对路径
	 * @param clazzs
	 * @return
	 */
	public static String write(String filePath, Class<?>... clazzs) {
		String error = null;
		File file = new File(ConfigConstant.CONFIG_BASE_PATH + filePath);

		if (filePath != null && clazzs != null && clazzs.length > 0) {
			List<ORMConfigBean> ormList = new ArrayList<ORMConfigBean>();
			try {
				for (int i = 0; i < clazzs.length; ++i) {
					Class<?> clazz = clazzs[i];
					String clsName = clazz.getSimpleName();
					Entity entity = clazz.getAnnotation(Entity.class);
					if (entity == null && !clsName.endsWith("PO")
							&& !clsName.endsWith("POJO") && !clsName.endsWith("Entity")
							&& !clsName.endsWith("Model")) {
						
						return null;
					}
					
					Table tableAnn = clazz.getAnnotation(Table.class);
					String table = tableAnn == null ? "" :tableAnn.name();
					table = "".equals(table.trim()) ? clsName
							: table;
					ORMConfigBean ormBean = new ORMConfigBean();
					ormBean.setClazz(clazz.getName());
					ormBean.setId(clazz.getSimpleName());
					ormBean.setTable(table);
					ReflectUtil ru;

					ru = new ReflectUtil(clazz);
					List<Property> pList = new ArrayList<Property>();
					for (Field f : ru.getFields()) {
						String name = f.getName();
						Method getter = ru.getGetter(name);
						Column colAnn = getter.getAnnotation(Column.class);
						if (colAnn == null) {
							colAnn = f.getAnnotation(Column.class);
							if (colAnn == null)
								continue;
						}

						Id idAnn = f.getAnnotation(Id.class);
						if (idAnn == null){
							idAnn = getter.getAnnotation(Id.class);
							if (idAnn == null)
								continue;
						}
						
						Property p = new Property();
						p.setAutoIncrement("1");
						p.setPk("1");
						
						Pk pkAnn = f.getAnnotation(Pk.class);
						
						if (pkAnn != null) {
							p.setPk("1");
						}
						
						String column = colAnn.name();
						column = "".equals(column.trim()) ? name : column;
						p.setName(name);
						p.setColumn(column);
						p.setType(f.getType().getName());
						pList.add(p);
					}
					ormBean.setProperty(pList);
					ormList.add(ormBean);
				}

				BeanXMLUtil.getBeanXMLWriter(file, ormList).write();
			} catch (Exception e) {
				error = e.getMessage();
				e.printStackTrace();
			}

		} else {
			error = "Class参数不能为空";
		}

		return error;
	}

	/**
	 * 将某个包下的所有Class类写入到目标xml文件中
	 * 
	 * @param filePath
	 *            文件相对路径
	 * @param packageName
	 *            包名
	 * @return
	 */
	public static String write(String filePath, String packageName) {
		String error = null;
		String result;
		List<Class<?>> list = new ArrayList<Class<?>>();
		StringBuilder sb = new StringBuilder(ConfigConstant.CONFIG_BASE_PATH);
		sb.append(File.separator).append("classes");
		for (String t : packageName.split("\\.")) {
			sb.append(new StringBuilder(File.separator).append(t).toString());
		}
		File file = new File(sb.toString());
		File listFile[] = file.listFiles();
		for (File f : listFile) {
			if (f.getName().endsWith(".class")) {
				result = new StringBuilder(packageName)
						.append(".")
						.append(f.getName())
						.toString()
						.substring(
								0,
								new StringBuilder(packageName).append(".")
										.append(f.getName()).toString()
										.lastIndexOf("."));
				try {
					list.add(Class.forName(result));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		error = write(filePath, list);
		return error;
	}
}
