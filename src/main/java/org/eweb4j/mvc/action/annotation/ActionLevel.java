package org.eweb4j.mvc.action.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author weiwei Action 的优先级，数字越大，级别越高。 级别高的会把级别低的替代掉。
 * 
 *         前提：出现相同Action URI mapping的时候
 */

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ActionLevel {
	int value() default 1;
}