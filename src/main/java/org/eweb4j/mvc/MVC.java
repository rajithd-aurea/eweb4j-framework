package org.eweb4j.mvc;

public class MVC {

	private static ThreadLocal<Context> threadLocal = new ThreadLocal<Context>();
	
	public static Context ctx(){
		return threadLocal.get();
	}

	public static ThreadLocal<Context> getThreadLocal() {
		return threadLocal;
	}

}
