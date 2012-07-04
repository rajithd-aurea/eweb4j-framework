package org.eweb4j.mvc.validator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;


public interface ValidatorIF {
	Validation validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request);
}
