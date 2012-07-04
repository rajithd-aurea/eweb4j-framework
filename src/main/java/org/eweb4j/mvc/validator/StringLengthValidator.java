package org.eweb4j.mvc.validator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;

/**
 * 对字符串长度的验证
 * 
 * @author cfuture.aw
 * 
 */
public class StringLengthValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {

		Map<String, String> valError = new HashMap<String, String>();
		for (FieldConfigBean f : val.getField()) {
			String[] value = map.get(f.getName());
			if (value == null || value.length == 0)
				continue;

			String mess = f.getMessage();

			int min = 0;
			int max = 0;

			boolean minFlag = false;
			boolean maxFlag = false;

			for (ParamConfigBean p : f.getParam()) {
				if (Validators.MIN_LENGTH_PARAM.equals(p.getName())) {
					min = Integer.parseInt(p.getValue());
					minFlag = true;
				} else if (Validators.MAX_LENGTH_PARAM.equals(p
						.getName())) {
					max = Integer.parseInt(p.getValue());
					maxFlag = true;
				}

				if (minFlag && maxFlag)
					break;
			}

			mess = mess.replace("{min}", min + "").replace("{max}", max + "");

			for (String v : value) {
				if (v.length() < min) {
					valError.put(f.getName(), mess);
					break;
				} else if (v.length() > max) {
					valError.put(f.getName(), mess);
					break;
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
