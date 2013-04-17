package org.eweb4j.mvc.validator;

import java.util.HashMap;
import java.util.Map;

import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;

/**
 * 对必填项的验证
 * 
 * @author cfuture.aw
 * 
 */
public class RequriedValidator implements ValidatorIF {
	public Validation validate(ValidatorConfigBean val, Context context) {
		Map<String, String> valError = new HashMap<String, String>();
		for (FieldConfigBean f : val.getField()) {
			String[] value = context.getQueryParamMap().get(f.getName());
			String mess = f.getMessage();

			if (value == null || value.length == 0)
				valError.put(f.getName(), mess);
			else
				for (String v : value) {
					if (v == null || v.trim().length() == 0){
						valError.put(f.getName(), mess);
						break ;
					}
				}

			context.getRequest().setAttribute(f.getName(), value);
		}

		Validation validation = new Validation();
		if (!valError.isEmpty())
			validation.getErrors().put(val.getName(), valError);
		
		return validation;
	}
}
