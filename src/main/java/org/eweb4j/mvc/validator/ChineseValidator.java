package org.eweb4j.mvc.validator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.util.RegexList;

/**
 * 对中文的验证
 * 
 * @author cfuture.aw
 * 
 */
public class ChineseValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {
		Validation vali = new ValidatorHelper(RegexList.all_chinese_regexp).validate(val,
				map, request);
		return vali;
	}

}
