package org.eweb4j.mvc.validator;

import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.util.RegexList;


/**
 * URL验证器
 * @author cfuture.aw
 * @since v1.a.0
 *
 */
public class URLValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val, Context context) {
		return new ValidatorHelper(RegexList.url_regexp).validate(val, context);
	}

}
