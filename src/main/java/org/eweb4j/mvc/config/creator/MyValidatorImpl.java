package org.eweb4j.mvc.config.creator;

import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.validator.annotation.MyValidator;
import org.eweb4j.util.StringUtil;


public class MyValidatorImpl implements ValidatorCreator {

	private MyValidator ann;

	public MyValidatorImpl(MyValidator ann) {
		this.ann = ann;
	}

	public ValidatorConfigBean create(String fieldName, ValidatorConfigBean val) {
		if (this.ann == null)
			return null;
		Class<?> clazz = this.ann.clazz();
		String valName = clazz.getSimpleName();
		if (val == null || !valName.equals(val.getName())) {
			val = new ValidatorConfigBean();
			val.setName(valName);
			val.setClazz(clazz.getName());
		}

		FieldConfigBean fcb = new FieldConfigBean();
		fcb.setName(fieldName);
		fcb.setMessage(StringUtil.parsePropValue(ann.mess()));
		String[] params = ann.params();
		String[] values = ann.values();
		if (params.length > 0 && values.length > 0 && params.length == values.length){
			for (int i = 0; i < params.length; i++){
				ParamConfigBean pcb = new ParamConfigBean();
				pcb.setName(StringUtil.parsePropValue(params[i]));
				pcb.setValue(StringUtil.parsePropValue(values[i]));
				fcb.getParam().add(pcb);
			}
		}
		
		val.getField().add(fcb);

		return val;
	}

}
