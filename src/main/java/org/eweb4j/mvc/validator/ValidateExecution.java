package org.eweb4j.mvc.validator;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.util.StringUtil;

public class ValidateExecution {

	private static Log log = LogFactory.getMVCLogger(ValidateExecution.class);

	public static Validation checkValidate(List<ValidatorConfigBean> valList, Map<String, String[]> paramMap, HttpServletRequest req) {

		Validation validation = new Validation();

		if (valList == null || valList.isEmpty())
			return validation;

		// 启动验证器
		for (int i = valList.size() - 1; i > -1; i--) {
			ValidatorConfigBean val = valList.get(i);
			ValidatorIF validator = null;
			if (!"".equals(val.getName())) {
				// 根据name属性实例化相关的验证器，如果找不到，就根据class属性实例化
				validator = ValidatorFactory.getValidator(val.getName());
				if (validator == null)
					try {
						validator = (ValidatorIF) Class.forName(val.getClazz()).newInstance();
					} catch (Exception e) {
						log.error(StringUtil.getExceptionString(e));
					}
			}

			if (validator == null)
				continue;

			Map<String, Map<String, String>> err = validator.validate(val, paramMap, req).getErrors();
			for (Entry<String, Map<String,String>> en : err.entrySet()){
				String key = en.getKey();
				Map<String,String> value = en.getValue();
				if (validation.getErrors().containsKey(key))
					validation.getErrors().get(key).putAll(value);
				else
					validation.getErrors().put(key, value);
			}
		}

		return validation;
	}
}
