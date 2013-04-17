package org.eweb4j.mvc.validator;

import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.util.RegexList;

/**
 * 对数字的验证
 * @author cfuture.aw
 *
 */
public class IntegerValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val, Context context) {
		return new ValidatorHelper(RegexList.integer_regexp).validate(val, context);
	}
}
