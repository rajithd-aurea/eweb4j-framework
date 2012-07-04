package org.eweb4j.mvc.validator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;


/**
 * 对日期的验证
 * 
 * @author cfuture.aw
 * 
 */
public class DateValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {
		Map<String, String> valError = new HashMap<String, String>();
		for (FieldConfigBean f : val.getField()) {
			String[] value = map.get(f.getName());
			if (value == null || value.length == 0)
				continue;
			String mess = f.getMessage();
			
			param: for (ParamConfigBean p : f.getParam()) {
				if (!Validators.DATE_FORMAT_PARAM.equalsIgnoreCase(p.getName()))
					continue;

				String paramValue = p.getValue();
				mess = mess.replace("{format}", paramValue);
				
				DateFormat df = new SimpleDateFormat(paramValue);
				for (String v : value) {
					try {
						df.parse(v);
					} catch (Exception e) {
						valError.put(f.getName(), mess);
						
						break param;
					}
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
