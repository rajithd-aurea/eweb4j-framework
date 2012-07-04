package org.eweb4j.mvc.validator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
	public Validation validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {
		Map<String, String> valError = new HashMap<String, String>();
		for (FieldConfigBean f : val.getField()) {
			String[] value = map.get(f.getName());
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

			request.setAttribute(f.getName(), value);
		}

		Validation validation = new Validation();
		if (!valError.isEmpty())
			validation.getErrors().put(val.getName(), valError);
		
		return validation;
	}
}
