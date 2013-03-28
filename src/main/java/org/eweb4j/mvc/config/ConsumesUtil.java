package org.eweb4j.mvc.config;

import java.lang.reflect.Method;

import javax.ws.rs.Consumes;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-3-28 下午01:53:55
 */
public class ConsumesUtil {

	public static String[] getConsumesValue(Method m){
		return m.getAnnotation(Consumes.class).value();
	}
	
	public static String[] getConsumesValue(Class<?> cls){
		return cls.getAnnotation(Consumes.class).value();
	}
	
}
