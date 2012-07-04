package org.eweb4j.mvc;

import org.eweb4j.mvc.validator.annotation.Validate;

public interface Crud {
	String index();

	String create();

	@Validate("id")
	String update();

	@Validate("id")
	String destroy();

	@Validate("id")
	String show();

	@Validate("id")
	String edit();

	String editNew();
}
