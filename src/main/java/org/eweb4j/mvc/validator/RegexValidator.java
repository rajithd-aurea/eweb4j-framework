package org.eweb4j.mvc.validator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;


public class RegexValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {
		Map<String, String> valError = new HashMap<String, String>();

		for (FieldConfigBean f : val.getField()) {
			String key = f.getName();
			String[] value = map.get(key);
			
			if (value == null || value.length == 0)
				continue;
			
			String mess = f.getMessage();
			
			param: for (ParamConfigBean param : f.getParam()) {
				String pName = param.getName();

				if (!Validators.REGEX_PARAM.equals(pName))
					continue;

				String pValue = param.getValue();
				for (String v : value) {
					if (!v.matches(pValue)) {
						valError.put(key, mess.replace("{pattern}", pValue));
						break param;
					}
				}
			}

			request.setAttribute(key, value);

		}

		Validation validation = new Validation();
		if (!valError.isEmpty())
			validation.getErrors().put(val.getName(), valError);
		
		return validation;
	}
}
