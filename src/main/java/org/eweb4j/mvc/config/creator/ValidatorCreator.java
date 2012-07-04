package org.eweb4j.mvc.config.creator;

import org.eweb4j.mvc.config.bean.ValidatorConfigBean;

public interface ValidatorCreator {
	ValidatorConfigBean create(String fieldName, ValidatorConfigBean val);
}
