package org.eweb4j.mvc.config.creator;

import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.validator.Validators;
import org.eweb4j.mvc.validator.annotation.Int;
import org.eweb4j.util.StringUtil;


public class IntImpl implements ValidatorCreator {

	private Int ann;

	public IntImpl(Int ann) {
		this.ann = ann;
	}

	public ValidatorConfigBean create(String fieldName, ValidatorConfigBean val) {
		if (this.ann == null)
			return null;
		if (val == null || !Validators.INT.equals(val.getName())) {
			val = new ValidatorConfigBean();
			val.setName(Validators.INT);
		}

		FieldConfigBean fcb = new FieldConfigBean();
		fcb.setName(fieldName);
		fcb.setMessage(StringUtil.parsePropValue(ann.mess()));

		val.getField().add(fcb);

		return val;
	}

}
