package org.eweb4j.mvc.config.creator;

import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.validator.Validators;
import org.eweb4j.mvc.validator.annotation.Required;
import org.eweb4j.util.StringUtil;


public class RequiredImpl implements ValidatorCreator {

	private Required ann;

	public RequiredImpl(Required ann) {
		this.ann = ann;
	}

	public ValidatorConfigBean create(String fieldName, ValidatorConfigBean val) {
		if (this.ann == null)
			return null;
		if (val == null || !Validators.REQUIRED.equals(val.getName())) {
			val = new ValidatorConfigBean();
			val.setName(Validators.REQUIRED);
		}

		FieldConfigBean fcb = new FieldConfigBean();
		fcb.setName(fieldName);
		fcb.setMessage(StringUtil.parsePropValue(ann.mess()));

		val.getField().add(fcb);

		return val;
	}

}
