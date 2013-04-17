package org.eweb4j.mvc.validator;

import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.util.RegexList;


/**
 * 对身份证号码的验证
 * 
 * @author cfuture.aw
 * 
 */
public class IDCardValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val, Context context) {
		return new ValidatorHelper(RegexList.ID_card_regexp).validate(val, context);
	}

}
