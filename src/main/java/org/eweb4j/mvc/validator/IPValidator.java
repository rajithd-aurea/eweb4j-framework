package org.eweb4j.mvc.validator;

import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.util.RegexList;

/**
 * 对IP地址的验证
 * @author cfuture.aw
 *
 */
public class IPValidator implements ValidatorIF{

	public Validation validate(ValidatorConfigBean val, Context context) {
		return new ValidatorHelper(RegexList.ip_regexp).validate(val, context);
	}

}
