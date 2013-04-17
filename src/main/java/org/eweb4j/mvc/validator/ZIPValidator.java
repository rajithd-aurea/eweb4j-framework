package org.eweb4j.mvc.validator;

import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.util.RegexList;


/**
 * 对邮政编码的验证
 * 
 * @author cfuture.aw
 * 
 */
public class ZIPValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val, Context context) {
		return new ValidatorHelper(RegexList.ZIP_regexp).validate(val, context);
	}

}
