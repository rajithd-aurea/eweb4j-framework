package org.eweb4j.mvc.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-3-28 下午01:45:25
 */
public class JAXWSUtil {

	private final static String DELETE = "javax.ws.rs.DELETE";
	private final static String GET = "javax.ws.rs.GET";
	private final static String HttpMethod = "javax.ws.rs.HttpMethod";
	private final static String POST = "javax.ws.rs.POST";
	private final static String PUT = "javax.ws.rs.PUT";
	private final static String Path = "javax.ws.rs.Path";
	private final static String Produces = "javax.ws.rs.Produces";
	
	private final static String Consumes = "javax.ws.rs.Consumes";
	private final static String DefaultValue = "javax.ws.rs.DefaultValue";
	private final static String PathParam = "javax.ws.rs.PathParam";
	private final static String QueryParam = "javax.ws.rs.QueryParam";
	private final static String MediaType = "javax.ws.rs.core.MediaType";
	
	public static boolean hasMediaType(Class<?> clazz) {
		try {
//			Class cls = Thread.currentThread().getContextClassLoader().loadClass(MediaType);
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(MediaType);
			Object obj = clazz.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
		}
		return false;
	}
	
	public static boolean hasConsumes(Method m) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(Consumes);
			Object obj = m.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
		}
		return false;
	}
	
	public static boolean hasConsumes(Class<?> clazz) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(Consumes);
			Object obj = clazz.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
		}
		return false;
	}
	
	public static Annotation getPathParam(Annotation[] anns) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(PathParam);
			for (Annotation a : anns) {
				if (a == null)
					continue;

				if (a.annotationType().isAssignableFrom(cls)) {
					return a;
				}
			}
		} catch (Throwable e){
		}
		
		return null;
	}
	
	public static boolean hasPathParam(Annotation[] anns) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(PathParam);
			for (Annotation a : anns) {
				if (a == null)
					continue;

				if (!a.annotationType().isAssignableFrom(cls))
					continue;

				return true;
			}

			return false;
		} catch (Throwable e){
		}
		
		return false;
	}
	
	public static Annotation getQueryParam(Annotation[] anns) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(QueryParam);
			for (Annotation a : anns) {
				if (a == null)
					continue;

				if (a.annotationType().isAssignableFrom(cls)) {
					return a;
				}
			}
		} catch (Throwable e){
		}
		
		return null;
	}
	
	public static boolean hasQueryParam(Annotation[] anns) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(QueryParam);
			for (Annotation a : anns) {
				if (a == null)
					continue;

				if (!a.annotationType().isAssignableFrom(cls))
					continue;

				return true;
			}

			return false;
		} catch (Throwable e){
		}
		
		return false;
	}
	
	public static Annotation getDefaultValue(Annotation[] anns) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(DefaultValue);
			for (Annotation a : anns) {
				if (a == null)
					continue;

				if (a.annotationType().isAssignableFrom(cls)) {
					return a;
				}
			}
		} catch (Throwable e){
		}
		
		return null;
	}
	
	public static boolean hasDefaultValue(Annotation[] anns) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(DefaultValue);
			for (Annotation a : anns) {
				if (a == null)
					continue;

				if (!a.annotationType().isAssignableFrom(cls))
					continue;

				return true;
			}

			return false;
		} catch (Throwable e){
		}
		return false;
	}
	
	public static boolean hasPath(Class<?> clazz) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(Path);
			Object obj = clazz.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
		}
		return false;
	}
	
	public static boolean hasPath(Method m) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(Path);
			Object obj = m.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
			
		}
		return false;
	}
	
	public static boolean hasProduces(Method m) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(Produces);
			Object obj = m.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
			
		}
		return false;
	}
	
	public static boolean hasGET(Method m) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(GET);
			Object obj = m.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
			
		}
		return false;
	}
	
	public static boolean hasPOST(Method m) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(POST);
			Object obj = m.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
			
		}
		return false;
	}
	
	public static boolean hasPUT(Method m) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(PUT);
			Object obj = m.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
			
		}
		return false;
	}
	
	public static boolean hasDELETE(Method m) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(DELETE);
			Object obj = m.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
			
		}
		return false;
	}
	
	public static boolean hasGET(Class<?> c) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(GET);
			Object obj = c.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
			
		}
		return false;
	}
	
	public static boolean hasPOST(Class c) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(POST);
			Object obj = c.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
			
		}
		return false;
	}
	
	public static boolean hasPUT(Class c) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(PUT);
			Object obj = c.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
			
		}
		return false;
	}
	
	public static boolean hasDELETE(Class c) {
		try {
			Class cls = Thread.currentThread().getContextClassLoader().loadClass(DELETE);
			Object obj = c.getAnnotation(cls);
			return obj != null;
		} catch (Throwable e){
			
		}
		return false;
	}
}
