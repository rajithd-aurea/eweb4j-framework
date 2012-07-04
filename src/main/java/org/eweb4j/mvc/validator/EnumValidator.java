package org.eweb4j.mvc.validator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;


public class EnumValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {
		Map<String, String> valError = new HashMap<String, String>();
		for (FieldConfigBean f : val.getField()) {
			String[] value = map.get(f.getName());

			if (value == null || value.length == 0)
				continue;
			String mess = f.getMessage();

			param: for (ParamConfigBean p : f.getParam()) {
				if (Validators.ENUM_WORD_PARAM.equalsIgnoreCase(p
						.getName())) {
					String[] enumWord = p.getValue().split("#");
					if (enumWord == null || enumWord.length == 0)
						continue;
					boolean flag = false;
					for (String v : value) {
						for (String word : enumWord) {
							if (v.equals(word)){
								flag = true;
								break;
							}
						}
						if (flag)
							continue;

						mess = mess.replace("{words}", p.getValue());
						
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
