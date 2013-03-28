package org.eweb4j.mvc.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-3-28 下午01:53:55
 */
public class PathUtil {

	public static String getPathValue(Method m){
		return m.getAnnotation(Path.class).value();
	}
	
	public static String getPathValue(Class<?> cls){
		return cls.getAnnotation(Path.class).value();
	}
	
	public static String getPathParamValue(Annotation ann){
		return ((PathParam)ann).value();
	}
	
}
