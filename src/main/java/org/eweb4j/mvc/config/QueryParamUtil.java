package org.eweb4j.mvc.config;

import java.lang.annotation.Annotation;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-3-28 下午01:53:55
 */
public class QueryParamUtil {
	
	public static String getQueryParamValue(Annotation ann){
		return ((QueryParam)ann).value();
	}
	
	public static String getDefaultValue(Annotation ann){
		return ((DefaultValue)ann).value();
	}
	
}
