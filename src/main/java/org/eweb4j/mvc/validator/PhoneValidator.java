package org.eweb4j.mvc.validator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.util.RegexList;

/**
 * 对内地电话号码的验证
 * @author cfuture.aw
 *
 */
public class PhoneValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val, Map<String, String[]> map,
			HttpServletRequest request) {
		Validation vali = new ValidatorHelper(RegexList.phone_regexp).validate(val, map,
				request);
		return vali;
	}

}
