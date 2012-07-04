package org.eweb4j.mvc.action;

import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.config.MVCConfigConstant;


/**
 * MVC组件Action接口
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public interface IAction {
	public static String SUCCESS = MVCConfigConstant.SUCCESS_RESULT;
	public static String ERROR = MVCConfigConstant.ERROR_RESULT;
	public static String INPUT = MVCConfigConstant.INPUT_RESULT;

	IAction init(Context context) throws Exception;

	String execute();
}
