package org.eweb4j.mvc.config.creator;

import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.validator.Validators;
import org.eweb4j.mvc.validator.annotation.Forbid;
import org.eweb4j.util.StringUtil;


public class ForbidImpl implements ValidatorCreator {

	private Forbid ann;

	public ForbidImpl(Forbid ann) {
		this.ann = ann;
	}

	public ValidatorConfigBean create(String fieldName, ValidatorConfigBean val) {
		if (this.ann == null)
			return null;
		if (val == null || !Validators.FORBID.equals(val.getName())) {
			val = new ValidatorConfigBean();
			val.setName(Validators.FORBID);
		}

		FieldConfigBean fcb = new FieldConfigBean();
		fcb.setName(fieldName);
		fcb.setMessage(StringUtil.parsePropValue(ann.mess()));
		ParamConfigBean pcb = new ParamConfigBean();
		pcb.setName(Validators.FORBID_WORD_PARAM);
		StringBuilder sb = new StringBuilder();
		for (String s : ann.words()){
			if (sb.length() > 0)
				sb.append("#");
			
			sb.append(StringUtil.parsePropValue(s));
		}
		
		pcb.setValue(sb.toString());
		fcb.getParam().add(pcb);

		val.getField().add(fcb);

		return val;
	}

}
