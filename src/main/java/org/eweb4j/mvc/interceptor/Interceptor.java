package org.eweb4j.mvc.interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Interceptor {

	String name() default "";
	String type() default "before";
	String policy() default "and";
	Uri[] uri() ;
	String[] except() default {};
	int priority() default 0;//数字越小，优先级越高
	String method() default "doIntercept";
}
