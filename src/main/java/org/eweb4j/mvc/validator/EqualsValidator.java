package org.eweb4j.mvc.validator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;


/**
 * 对相同值的验证
 * 
 * @author cfuture.weiwei
 * @since 1.b.8
 * 
 */
public class EqualsValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {
		Map<String, String> valError = new HashMap<String, String>();
		for (FieldConfigBean f : val.getField()) {
			String[] value = map.get(f.getName());
			if (value == null || value.length == 0)
				continue;
			String mess = f.getMessage();

			boolean flag = false;
			param: for (ParamConfigBean p : f.getParam()) {
				if (!Validators.EQUALS_TO_PARAM.equalsIgnoreCase(p.getName()))
					continue;

				String paramValue = p.getValue();
				String[] others = map.get(paramValue);

				if (others == null || others.length == 0) {
					flag = true;
					break param;
				}

				for (String v : value) {
					if (!v.equals(others[0])) {
						flag = true;
						break param;
					}
				}
			}

			if (flag)
				valError.put(f.getName(), mess);

			request.setAttribute(f.getName(), value);
		}
		
		Validation validation = new Validation();
		if (!valError.isEmpty())
			validation.getErrors().put(val.getName(), valError);
		
		return validation;
	}

}
