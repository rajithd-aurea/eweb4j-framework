package org.eweb4j.orm;
/**
 * Db operator for Active Record Model Helper
 * @author weiwei l.weiwei@163.com
 * @date 2013-1-22 下午03:51:22
 */
public class Db {
	
	public static <T> ModelHelper<T> ar(T t){
		return new ModelHelper<T>(t);
	}
	
	public static <T> ModelHelper<T> ar(Class<T> cls){
		return Models.inst(cls);
	}
}
