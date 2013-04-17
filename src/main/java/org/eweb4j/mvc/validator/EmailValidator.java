package org.eweb4j.mvc.validator;

import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.util.RegexList;

/**
 * 对邮箱地址的验证
 * @author cfuture.aw
 *
 */
public class EmailValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val, Context context) {
		return new ValidatorHelper(RegexList.email_regexp).validate(val, context);
	}

}
