package org.eweb4j.mvc.config.creator;

import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.validator.Validators;
import org.eweb4j.mvc.validator.annotation.Regex;
import org.eweb4j.util.StringUtil;


public class RegexImpl implements ValidatorCreator {

	private Regex ann;

	public RegexImpl(Regex ann) {
		this.ann = ann;
	}

	public ValidatorConfigBean create(String fieldName, ValidatorConfigBean val) {
		if (this.ann == null)
			return null;
		if (val == null || !Validators.REGEX.equals(val.getName())) {
			val = new ValidatorConfigBean();
			val.setName(Validators.REGEX);
		}

		FieldConfigBean fcb = new FieldConfigBean();
		fcb.setName(fieldName);
		fcb.setMessage(StringUtil.parsePropValue(ann.mess()));
		ParamConfigBean pcb = new ParamConfigBean();
		pcb.setName(Validators.REGEX_PARAM);
		pcb.setValue(StringUtil.parsePropValue(ann.pattern()));
		fcb.getParam().add(pcb);

		val.getField().add(fcb);

		return val;
	}

}
