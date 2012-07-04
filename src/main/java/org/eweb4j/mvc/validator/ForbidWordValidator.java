package org.eweb4j.mvc.validator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;


public class ForbidWordValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {
		Map<String, String> valError = new HashMap<String, String>();
		for (FieldConfigBean f : val.getField()) {
			String[] value = map.get(f.getName());

			if (value == null || value.length == 0)
				continue;
			String mess = f.getMessage();
			param: for (ParamConfigBean p : f.getParam()) {
				if (Validators.FORBID_WORD_PARAM.equalsIgnoreCase(p
						.getName())) {
					String[] forbidWord = p.getValue().split("#");
					if (forbidWord == null || forbidWord.length == 0)
						continue;
					
					boolean flag = false;
					for (String v : value) {
						for (String word : forbidWord) {
							if (v.indexOf(word) != -1){
								flag = true;
								break;
							}
						}
						if (!flag)
							continue;

						valError.put(f.getName(), mess.replace("{words}",p.getValue()));
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
