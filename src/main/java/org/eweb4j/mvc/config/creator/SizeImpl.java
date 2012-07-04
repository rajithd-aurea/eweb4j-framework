package org.eweb4j.mvc.config.creator;

import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.validator.Validators;
import org.eweb4j.mvc.validator.annotation.Size;
import org.eweb4j.util.StringUtil;


public class SizeImpl implements ValidatorCreator {

	private Size ann;

	public SizeImpl(Size ann) {
		this.ann = ann;
	}

	public ValidatorConfigBean create(String fieldName, ValidatorConfigBean val) {
		if (this.ann == null)
			return null;
		if (val == null || !Validators.SIZE.equals(val.getName())) {
			val = new ValidatorConfigBean();
			val.setName(Validators.SIZE);
		}

		FieldConfigBean fcb = new FieldConfigBean();
		fcb.setName(fieldName);
		fcb.setMessage(StringUtil.parsePropValue(ann.mess()));
		
		ParamConfigBean pcb = new ParamConfigBean();
		pcb.setName(Validators.MIN_SIZE_PARAM);
		pcb.setValue(String.valueOf(ann.min()));
		
		fcb.getParam().add(pcb);
		
		ParamConfigBean pcb2 = new ParamConfigBean();
		pcb2.setName(Validators.MAX_SIZE_PARAM);
		pcb2.setValue(String.valueOf(ann.max()));
		
		fcb.getParam().add(pcb2);

		val.getField().add(fcb);

		return val;
	}

}
