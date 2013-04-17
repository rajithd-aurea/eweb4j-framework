package org.eweb4j.mvc.validator;

import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;


public interface ValidatorIF {
	Validation validate(ValidatorConfigBean val, Context context);
}
