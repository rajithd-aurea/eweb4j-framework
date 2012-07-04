package test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.validator.ValidatorIF;

public class TestValidator implements ValidatorIF {

	public Validation validate(ValidatorConfigBean val,
			Map<String, String[]> map, HttpServletRequest request) {
		Map<String, String> errMap = new HashMap<String, String>();

		for (FieldConfigBean fcb : val.getField()) {
			// 比如我现在验证 "name"参数值不能出现 "中国" 字眼
			String key = fcb.getName();
			String value = map.get(key)[0];// 这个是封装了request请求传过来的参数map
			if (value.indexOf("中国") != -1)
				errMap.put(key, fcb.getMessage());

		}

		Validation validation = new Validation();
		validation.getErrors().put(val.getName(), errMap);
		
		return validation;
	}

}
