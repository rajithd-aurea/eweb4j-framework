package org.eweb4j.mvc.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Upload {
	
	String maxRequestSize() default "4M";//允许一次上传总共的文件最大大小 default 4M
	
	String maxMemorySize() default "4K";//硬盘缓冲 default 4K
	
	String maxFileSize() default "1M";//允许每个文件最大大小 default 1M
	
	String tmpDir() default "" ;//临时目录
	
	String[] suffix();
	
	Msg msg() default @Upload.Msg;
	
	@interface Msg {
		String maxRequestSize() default "文件总大小不超过{maxRequestSize}";
		String maxMemorySize() default "磁盘缓冲大小不超过{maxMemorySize}";
		String maxFileSize() default "每个文件大小不超过{maxFileSize}";
		String suffix() default "文件类型限制{suffix}";
	}
}
