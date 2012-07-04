package org.eweb4j.mvc.validator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

	public Validation validate(ValidatorConfigBean val, Map<String, String[]> map,
			HttpServletRequest request) {
		Validation vali = new ValidatorHelper(RegexList.url_regexp).validate(val, map,
				request);
		return vali;
	}

}
