package org.eweb4j.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;

public class ClassUtil {
	
	public static Class<?> getPojoClass(Method method) {
		Type type = method.getGenericReturnType();
		String clsName = type.toString();
		if (clsName.contains("<") && clsName.contains(">")) {
			int s = clsName.indexOf("<") + 1;
			int e = clsName.indexOf(">");

			String str = clsName.substring(s, e);
			return ClassUtil.getPojoClass(str);
		} else
			return method.getReturnType();
	}

	public static Class<?> getPojoClass(String className) {
		Class<?> cls;
		try {
			cls = Class.forName(className);

			if (isPojo(cls))
				return cls;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println(Class.forName("org.eweb4j.config.bean.Prop"));
	}

	public static boolean isPojo(Class<?> cls) {
		if (cls == null)
			return false;

		if (Collection.class.isAssignableFrom(cls)) {
		} else {
			if (Integer.class.isAssignableFrom(cls)
					|| int.class.isAssignableFrom(cls)) {
			} else if (Integer[].class.isAssignableFrom(cls)
					|| int[].class.isAssignableFrom(cls)) {

			} else if (Long.class.isAssignableFrom(cls)
					|| long.class.isAssignableFrom(cls)) {

			} else if (Long[].class.isAssignableFrom(cls)
					|| long[].class.isAssignableFrom(cls)) {

			} else if (Float.class.isAssignableFrom(cls)
					|| float.class.isAssignableFrom(cls)) {

			} else if (Float[].class.isAssignableFrom(cls)
					|| float[].class.isAssignableFrom(cls)) {

			} else if (Double.class.isAssignableFrom(cls)
					|| double.class.isAssignableFrom(cls)) {

			} else if (Double[].class.isAssignableFrom(cls)
					|| double[].class.isAssignableFrom(cls)) {

			} else if (String.class.isAssignableFrom(cls)) {
			} else if (String[].class.isAssignableFrom(cls)) {
			} else if (Date.class.isAssignableFrom(cls)) {

			} else if (Date[].class.isAssignableFrom(cls)) {

			} else {
				try {
					cls.newInstance();
					return true;
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}

		return false;
	}

	/**
	 * 
	 * @param <T>
	 * @param fieldName
	 * @param v
	 * @param vs
	 * @param pojo
	 * @return
	 * @throws Exception
	 */
	public static <T> T injectFieldValue(T pojo, String fieldName, String[] vs)
			throws Exception {

		if (pojo == null)
			return null;

		if (vs == null)
			return pojo;

		ReflectUtil ru = new ReflectUtil(pojo);
		Field f = ru.getField(fieldName);
		if (f == null)
			return pojo;

		Method m = ru.getSetter(fieldName);
		if (m == null)
			return pojo;

		Class<?> clazz = f.getType();

		if (Object[].class.isAssignableFrom(clazz)) {
			Object obj = getParamVals(clazz, vs);
			if (obj != null)
				m.invoke(pojo, new Object[] { obj });
		} else {
			Object obj = getParamVal(clazz, vs[0]);
			if (obj != null)
				m.invoke(pojo, obj);
		}
		return pojo;
	}

	public static Object getParamVal(Class<?> fieldType, String fieldVal) {
		Class<?> clazz = fieldType;
		String v = fieldVal;
		if (v == null)
			return null;

		try {
			if (int.class.isAssignableFrom(clazz)
					|| Integer.class.isAssignableFrom(clazz)) {
				if ("".equals(v.trim()))
					v = "0";

				return Integer.parseInt(v);
			} else if (long.class.isAssignableFrom(clazz)
					|| Long.class.isAssignableFrom(clazz)) {
				if ("".equals(v.trim()))
					v = "0";

				return Long.parseLong(v);
			} else if (double.class.isAssignableFrom(clazz)
					|| Double.class.isAssignableFrom(clazz)) {
				if ("".equals(v.trim()))
					v = "0.0";

				return Double.parseDouble(v);
			} else if (float.class.isAssignableFrom(clazz)
					|| Float.class.isAssignableFrom(clazz)) {
				if ("".equals(v.trim()))
					v = "0.0";

				return Float.parseFloat(v);
			} else if (boolean.class.isAssignableFrom(clazz)
					|| Boolean.class.isAssignableFrom(clazz)) {
				if ("".equals(v.trim()))
					v = "false";

				return Boolean.parseBoolean(v);
			} else if (String.class.isAssignableFrom(clazz)) {
				return v;
			} 
		} catch (Exception e) {
			return 0;
		} catch (Error e) {
			return 0;
		}

		return null;
	}

	public static Object getParamVals(Class<?> fieldType, String[] fieldVals) {
		Class<?> clazz = fieldType;
		String[] vs = fieldVals;
		if (vs == null)
			return null;
		try {
			int length = vs.length;
			if (Integer[].class.isAssignableFrom(clazz)) {
				Integer[] args = new Integer[length];
				for (int i = 0; i < vs.length; i++) {
					if ("".equals(vs[i].trim()))
						vs[i] = "0";
					args[i] = Integer.parseInt(vs[i]);
				}

				return args;
			} else if (int[].class.isAssignableFrom(clazz)) {
				int[] args = new int[length];
				for (int i = 0; i < vs.length; i++) {
					if ("".equals(vs[i].trim()))
						vs[i] = "0";
					args[i] = Integer.parseInt(vs[i]);
				}

				return args;
			} else if (Long[].class.isAssignableFrom(clazz)) {
				Long[] args = new Long[vs.length];
				for (int i = 0; i < vs.length; i++) {
					if ("".equals(vs[i].trim()))
						vs[i] = "0";
					args[i] = Long.parseLong(vs[i]);
				}

				return args;
			} else if (long[].class.isAssignableFrom(clazz)) {
				long[] args = new long[vs.length];
				for (int i = 0; i < vs.length; i++) {
					if ("".equals(vs[i].trim()))
						vs[i] = "0";
					args[i] = Long.parseLong(vs[i]);
				}

				return args;
			} else if (Double[].class.isAssignableFrom(clazz)) {
				Double[] args = new Double[vs.length];
				for (int i = 0; i < vs.length; i++) {
					if ("".equals(vs[i].trim()))
						vs[i] = "0.0";
					args[i] = Double.parseDouble(vs[i]);
				}

				return args;
			} else if (double[].class.isAssignableFrom(clazz)) {
				double[] args = new double[vs.length];
				for (int i = 0; i < vs.length; i++) {
					if ("".equals(vs[i].trim()))
						vs[i] = "0.0";
					args[i] = Double.parseDouble(vs[i]);
				}

				return args;
			} else if (Float[].class.isAssignableFrom(clazz)) {
				Float[] args = new Float[vs.length];
				for (int i = 0; i < vs.length; i++) {
					if ("".equals(vs[i].trim()))
						vs[i] = "0.0";

					args[i] = Float.parseFloat(vs[i]);
				}

				return args;
			} else if (float[].class.isAssignableFrom(clazz)) {
				float[] args = new float[vs.length];
				for (int i = 0; i < vs.length; i++) {
					if ("".equals(vs[i].trim()))
						vs[i] = "0.0";
					args[i] = Float.parseFloat(vs[i]);
				}

				return args;
			} else if (Boolean[].class.isAssignableFrom(clazz)) {
				Boolean[] args = new Boolean[vs.length];
				for (int i = 0; i < vs.length; i++) {
					if ("".equals(vs[i].trim()))
						vs[i] = "false";

					args[i] = Boolean.parseBoolean(vs[i]);
				}

				return args;
			} else if (boolean[].class.isAssignableFrom(clazz)) {
				boolean[] args = new boolean[vs.length];
				for (int i = 0; i < vs.length; i++) {
					if ("".equals(vs[i].trim()))
						vs[i] = "false";
					args[i] = Boolean.parseBoolean(vs[i]);
				}

				return args;
			} else if (String[].class.isAssignableFrom(clazz)) {
				return vs;
			}
		} catch (Exception e) {
			return null;
		} catch (Error e) {
			return null;
		}
		return null;
	}

	public static Class<?> getGenericType(Field f){
		Class<?> cls = f.getType();
		if (!Collection.class.isAssignableFrom(cls))
			return cls;
		
		ParameterizedType pt = (ParameterizedType) f.getGenericType();
		Type type = pt.getActualTypeArguments()[0];

		Class<?> targetCls = ClassUtil.getPojoClass(type.toString().replace("class ", ""));
		if (targetCls == null)
			return cls;

		return targetCls;

	}
	
	public static boolean isListClass(Field f) {
		Class<?> cls = f.getType();
		if (!Collection.class.isAssignableFrom(cls))
			return false;
		ParameterizedType pt = (ParameterizedType) f.getGenericType();
		Type type = pt.getActualTypeArguments()[0];

		Class<?> targetCls = ClassUtil.getPojoClass(type.toString().replace("class ", ""));
		if (targetCls == null)
			return false;

		return true;
	}

	public static boolean isListString(Field f) {
		Class<?> cls = f.getType();

		if (!Collection.class.isAssignableFrom(cls))
			return false;

		ParameterizedType pt = (ParameterizedType) f.getGenericType();
		Type type = pt.getActualTypeArguments()[0];

		if ("class java.lang.String".equals(type.toString()))
			return true;

		return false;
	}

}
