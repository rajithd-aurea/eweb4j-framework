package org.eweb4j.mvc.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 列出哪些参数需要验证,以及哪些例外
 * @author weiwei
 * 
 * <pre>
 * public 
 * </pre>
 *
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Validate {
	String[] value();
	String[] except() default {};
}
