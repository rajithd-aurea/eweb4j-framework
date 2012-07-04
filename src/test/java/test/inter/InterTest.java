package test.inter;

import org.eweb4j.mvc.Context;


public class InterTest {

	
	public String intecept(Context context) {
		System.out.println("interceptor------->" + context.getUri() + "httpMethod");
		return null;
	}

}
