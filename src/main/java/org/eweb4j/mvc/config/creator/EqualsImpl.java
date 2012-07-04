package org.eweb4j.mvc.config.creator;

import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.validator.Validators;
import org.eweb4j.mvc.validator.annotation.Equals;
import org.eweb4j.util.StringUtil;


public class EqualsImpl implements ValidatorCreator {

	private Equals ann;

	public EqualsImpl(Equals ann) {
		this.ann = ann;
	}

	public ValidatorConfigBean create(String fieldName, ValidatorConfigBean val) {
		if (this.ann == null)
			return null;
		if (val == null || !Validators.EQUALS.equals(val.getName())) {
			val = new ValidatorConfigBean();
			val.setName(Validators.EQUALS);
		}

		FieldConfigBean fcb = new FieldConfigBean();
		fcb.setName(fieldName);
		fcb.setMessage(StringUtil.parsePropValue(ann.mess()));
		ParamConfigBean pcb = new ParamConfigBean();
		pcb.setName(Validators.EQUALS_TO_PARAM);
		
		String value = StringUtil.parsePropValue(ann.to());
		value = fieldName.substring(0, fieldName.lastIndexOf(".")+1) + value;
		pcb.setValue(value);
		
		fcb.getParam().add(pcb);

		val.getField().add(fcb);

		return val;
	}
	
}
