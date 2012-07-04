package org.eweb4j.util;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * <p>
 * 功能：将Java对象序列化成Json格式字符串 目前支持对象类型：
 * </p>
 * <li>1.常用基本数据类型 :int long float double boolean 和 String</li> <li>2.List<?></li>
 * <li>3.Map<String,?></li> <li>4.JavaBean</li><li>任何类型数组</li>
 * 
 * <p>
 * <b>强调两点：
 * <li>1.支持对象无限嵌套，但要注意不要传入不支持的 对象类型参数，否则会进入无限递归导致堆栈溢出错误。谨记！</li>
 * <li>2.对象之间如果彼此都含有对方的引用，千万不要将两个引用实例化， 而应该让不进行序列化的引用取null，否则会无限递归造成堆栈溢出。谨记！</li>
 * </b>
 * </p>
 * 
 * @author CFuture.aw
 * 
 */
public class JsonConverter {
	/**
	 * 将任意的Java对象转换为json（匿名，单个对象）
	 * 
	 * @param <T>
	 * @param t
	 * @return
	 */
	public static <T> String convert(T t) {
		return JsonConverter.convertFromKeyValue(null, t);
	}

	/**
	 * 将任意的Java对象转换为Json（给定名字作为key，单个对象）
	 * 
	 * @param <T>
	 * @param key
	 * @param value
	 * @return
	 */
	public static <T> String convert(String key, T value) {
		return JsonConverter.convert(new String[] { key },
				new Object[] { value });
	}

	/**
	 * 将任意的Java对象转换为Json（给定名字作为key，一个或多个同类型或不同类型对象）
	 * 
	 * @param <T>
	 * @param keys
	 * @param values
	 * @return
	 */
	public static <T> String convert(String[] keys, T[] values) {
		return JsonConverter.convertWithKey(keys, values);
	}

	/**
	 * 将任意的Java对象转换为Json（默认简单类名作为key，多个不同类型对象）
	 * 
	 * @param <T>
	 * @param ts
	 * @return
	 */
	public static <T> String convertWithDefaultKey(T... ts) {
		String[] keys = null;
		if (ts != null) {
			if (ts.length == 1) {
				return JsonConverter.convert(ts[0]);
			}
			keys = new String[ts.length];
			for (int i = 0; i < keys.length; ++i) {
				keys[i] = ts[i].getClass().getSimpleName().toLowerCase();
			}
		}

		return JsonConverter.convertWithKey(keys, ts);
	}

	/**
	 * 将任意的Java对象转换为json（给定名字，支持多个对象）
	 * 
	 * @param <T>
	 * @param keys
	 * @param ts
	 * @return
	 */
	private static <T> String convertWithKey(String[] keys, T[] ts) {
		Map<String, T> map = null;
		if (keys != null && ts != null && keys.length == ts.length) {
			map = new Hashtable<String, T>();
			for (int i = 0; i < keys.length; ++i) {
				// System.out.println(Json2.convertFromKeyValue(keys[i],
				// ts[i]));
				map.put(keys[i], ts[i]);
			}
		}
		return JsonConverter.convertFromMap(map);
	}

	/**
	 * 将Map对象转换为json（匿名、非其他对象属性）
	 * 
	 * @param map
	 * @return
	 */
	private static String convertFromMap(Map<String, ?> map) {
		return JsonConverter.convertFromMap(null, map, false);
	}

	/**
	 * 将Map对象转换为json（给定名字，给定是否其他对象属性布尔值）
	 * 
	 * @param name
	 * @param map
	 * @param isProperty
	 * @return
	 */
	private static String convertFromMap(String name, Map<?, ?> map,
			boolean isProperty) {
		String json = null;
		if (map != null && !map.isEmpty()) {
			String format = "{%s}";
			StringBuilder sb = new StringBuilder();
			for (Entry<?, ?> entrySet : map.entrySet()) {

				if (sb.length() > 0) {
					sb.append(",");
				}
				json = JsonConverter.convertFromKeyValue(String
						.valueOf(entrySet.getKey()), entrySet.getValue());
				if (json != null) {
					sb.append(json);
				}
			}

			if (isProperty) {
				format = "\"%s\":{%s}";
				if (name == null || "".equals(name)) {
					name = "map";
				}
				json = String.format(format, name, sb.toString());
			} else {
				// json = String.format(format, sb.toString());
				if (name != null && !"".equals(name)) {
					format = "\"%s\":{%s}";
					json = String.format(format, name, sb.toString());
				} else {
					json = String.format(format, sb.toString());
				}
			}
		}
		return json;
	}

	/**
	 * 将List对象转换为json（给定名字、给定是否是其他对象的属性布尔值）
	 * 
	 * @param name
	 * @param list
	 * @param isProperty
	 * @return
	 */
	private static String convertFromList(String name, List<?> list,
			boolean isProperty) {
		String json = null;
		if (list != null && !list.isEmpty()) {
			String format = "[%s]";

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				json = JsonConverter.convertFromKeyValue(null, list.get(i));
				if (json != null) {
					sb.append(json);
				}
			}
			if (isProperty) {
				format = "\"%s\":[%s]";
				if (name == null || "".equals(name)) {
					name = "list";
				}
				json = String.format(format, name, sb.toString());
			} else {
				if (name != null && !"".equals(name)) {
					format = "\"%s\":[%s]";
					json = String.format(format, name, sb.toString());
				} else {
					json = String.format(format, sb.toString());
				}
			}

		}
		return json;
	}

	private static <T> String convertFromArray(String name, T[] array,
			boolean isProperty) {
		String json = null;
		if (array != null && array.length > 0) {
			String format = "[%s]";

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < array.length; i++) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				json = JsonConverter.convertFromKeyValue(null, array[i]);
				if (json != null) {
					sb.append(json);
				}
			}
			if (isProperty) {
				format = "\"%s\":[%s]";
				if (name == null || "".equals(name)) {
					name = "array";
				}
				json = String.format(format, name, sb.toString());
			} else {
				if (name != null && !"".equals(name)) {
					format = "\"%s\":[%s]";
					json = String.format(format, name, sb.toString());
				} else {
					json = String.format(format, sb.toString());
				}
			}

		}
		return json;
	}

	/**
	 * set
	 * 
	 * @param <T>
	 * @param name
	 * @param set
	 * @param isProperty
	 * @return
	 */
	private static <T> String convertFromSet(String name, Set<?> set,
			boolean isProperty) {
		String json = null;
		if (set != null && !set.isEmpty()) {
			String format = "[%s]";

			StringBuilder sb = new StringBuilder();
			for (Iterator<?> it = set.iterator(); it.hasNext();) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				json = JsonConverter.convertFromKeyValue(null, it.next());
				if (json != null) {
					sb.append(json);
				}
			}
			if (isProperty) {
				format = "\"%s\":[%s]";
				if (name == null || "".equals(name)) {
					name = "set";
				}
				json = String.format(format, name, sb.toString());
			} else {
				if (name != null && !"".equals(name)) {
					format = "\"%s\":[%s]";
					json = String.format(format, name, sb.toString());
				} else {
					json = String.format(format, sb.toString());
				}
			}

		}
		return json;
	}

	/**
	 * 将键值对key-value转换成json（非其他对象属性）
	 * 
	 * @param <T>
	 * @param key
	 * @param value
	 * @return
	 */
	private static <T> String convertFromKeyValue(String key, T value) {
		return JsonConverter.convertFromKeyValue(key, value, false);
	}

	/**
	 * 将键值对key-value转换为json（给定是否为其他对象属性）
	 * 
	 * @param <T>
	 * @param key
	 * @param value
	 * @return
	 */
	private static <T> String convertFromKeyValue(String key, T value,
			boolean isProperty) {
		String json = null;
		if (value != null) {
			Class<?> cls = value.getClass();
			String format = String.class.isAssignableFrom(cls) ? "\"%s\":\"%s\""
					: "\"%s\":%s";
			if (String.class.isAssignableFrom(cls)
					|| Integer.class.isAssignableFrom(cls)
					|| Long.class.isAssignableFrom(cls)
					|| Float.class.isAssignableFrom(cls)
					|| Double.class.isAssignableFrom(cls)
					|| Boolean.class.isAssignableFrom(cls)
					|| int.class.isAssignableFrom(cls)
					|| long.class.isAssignableFrom(cls)
					|| float.class.isAssignableFrom(cls)
					|| double.class.isAssignableFrom(cls)
					|| boolean.class.isAssignableFrom(cls)) {
				// 基本数据类型，key-value
				if (key == null || "".equals(key.trim())) {
					format = String.class.isAssignableFrom(cls) ? "\"%s\""
							: "%s";
					json = String.format(format, String.valueOf(value));
				} else {
					json = String.format(format, key, String.valueOf(value));
				}
			} else if (Object[].class.isAssignableFrom(cls)) {
				// 对象数组，使用下标来遍历对象
				json = JsonConverter.convertFromArray(key, (Object[]) value,
						isProperty);
			} else if (List.class.isAssignableFrom(cls)) {
				// 对象数组，使用下标来遍历对象
				json = JsonConverter.convertFromList(key, (List<?>) value,
						isProperty);
			} else if (Set.class.isAssignableFrom(cls)) {
				// 对象数组，使用下标来遍历对象
				json = JsonConverter.convertFromSet(key, (Set<?>) value,
						isProperty);
			} else if (Map.class.isAssignableFrom(cls)) {
				json = JsonConverter.convertFromMap(key, (Map<?, ?>) value,
						isProperty);
			} else {
				// 当不满足上面所有类型时，认为是自定义类型
				// 反射获取属性，将属性递归
				ReflectUtil ru = new ReflectUtil(value);
				StringBuilder sb = new StringBuilder("{");
				format = "\"%s\":%s";
				for (String name : ru.getFieldsName()) {
					Method m = ru.getMethod("get"
							+ StringUtil.toUpCaseFirst(name));
					if (m != null) {
						try {
							json = JsonConverter.convertFromKeyValue(name, m
									.invoke(value), true);
							if (json != null) {
								if (sb.length() > 1) {
									sb.append(",");
								}
								sb.append(json);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				sb.append("}");
				// 如果key没有给定具体的值，则认为是匿名对象（自定义类型）
				if (key == null || "".equals(key)) {
					format = "%s";
					json = String.format(format, sb.toString());
				} else {
					json = String.format(format, key, sb.toString());
				}
			}
		}

		return json;
	}
}
