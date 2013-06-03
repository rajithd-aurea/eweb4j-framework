package org.eweb4j.mvc.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.upload.UploadFile;

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
			List<UploadFile> files = context.getUploadMap().get(f.getName());
			String mess = f.getMessage();

			if ((value == null || value.length == 0) && (files == null || files.isEmpty()))
				valError.put(f.getName(), mess);
			else {
				boolean isBlank = false;
				if (value != null)
					for (String v : value) {
						if (v == null || v.trim().length() == 0){
							isBlank = true;
							break ;
						}
					}
				
				if (!isBlank){
					if (files != null)
						for (UploadFile file : files) {
							if (file == null || file.getFileName() == null || file.getFileName().trim().length() == 0 || file.getTmpFile() == null) {
								isBlank = true;
								break;
							}
						}
				}
				
				if (isBlank)
					valError.put(f.getName(), mess);
			}

			context.getRequest().setAttribute(f.getName(), value);
		}

		Validation validation = new Validation();
		if (!valError.isEmpty())
			validation.getErrors().put(val.getName(), valError);
		
		return validation;
	}
}
