package org.eweb4j.mvc.config;

import java.lang.reflect.Method;

import javax.ws.rs.Produces;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-3-28 下午01:53:55
 */
public class ProducesUtil {

	public static String[] getProducesValue(Method m){
		return m.getAnnotation(Produces.class).value();
	}
	
	public static String[] getProducesValue(Class<?> cls){
		return cls.getAnnotation(Produces.class).value();
	}
	
}
