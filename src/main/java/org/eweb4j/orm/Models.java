package org.eweb4j.orm;

public class Models {
	public static <T> ModelHelper<T> inst(T t){
		return new ModelHelper<T>(t);
	}
}
