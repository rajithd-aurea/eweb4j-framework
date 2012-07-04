package org.eweb4j.mvc.config.creator;

import java.lang.annotation.Annotation;

import org.eweb4j.mvc.validator.annotation.Chinese;
import org.eweb4j.mvc.validator.annotation.DateFormat;
import org.eweb4j.mvc.validator.annotation.Email;
import org.eweb4j.mvc.validator.annotation.Enumer;
import org.eweb4j.mvc.validator.annotation.Equals;
import org.eweb4j.mvc.validator.annotation.Forbid;
import org.eweb4j.mvc.validator.annotation.IDCard;
import org.eweb4j.mvc.validator.annotation.IP;
import org.eweb4j.mvc.validator.annotation.Int;
import org.eweb4j.mvc.validator.annotation.Length;
import org.eweb4j.mvc.validator.annotation.MyValidator;
import org.eweb4j.mvc.validator.annotation.Phone;
import org.eweb4j.mvc.validator.annotation.QQ;
import org.eweb4j.mvc.validator.annotation.Regex;
import org.eweb4j.mvc.validator.annotation.Required;
import org.eweb4j.mvc.validator.annotation.Size;
import org.eweb4j.mvc.validator.annotation.Url;
import org.eweb4j.mvc.validator.annotation.Zip;


/**
 * 应用工厂模式,创建不同的验证器处理
 * 
 * @author weiwei
 * 
 */
public class ValidatorFactory {
	public static ValidatorCreator get(Annotation ann) {
		Class<?> cls = ann.annotationType();
		String clsName = cls.getName();
		if (Chinese.class.getName().equals(clsName))
			return new ChineseImpl((Chinese) ann);
		else if (DateFormat.class.getName().equals(clsName))
			return new DateImpl((DateFormat) ann);
		else if (Email.class.getName().equals(clsName))
			return new EmailImpl((Email) ann);
		else if (Enumer.class.getName().equals(clsName))
			return new EnumImpl((Enumer) ann);
		else if (Equals.class.getName().equals(clsName))
			return new EqualsImpl((Equals) ann);
		else if (Forbid.class.getName().equals(clsName))
			return new ForbidImpl((Forbid) ann);
		else if (IDCard.class.getName().equals(clsName))
			return new IDCardImpl((IDCard) ann);
		else if (Int.class.getName().equals(clsName))
			return new IntImpl((Int) ann);
		else if (IP.class.getName().equals(clsName))
			return new IpImpl((IP) ann);
		else if (Length.class.getName().equals(clsName))
			return new LengthImpl((Length) ann);
		else if (Phone.class.getName().equals(clsName))
			return new PhoneImpl((Phone) ann);
		else if (QQ.class.getName().equals(clsName))
			return new QQImpl((QQ) ann);
		else if (Regex.class.getName().equals(clsName))
			return new RegexImpl((Regex) ann);
		else if (Required.class.getName().equals(clsName))
			return new RequiredImpl((Required) ann);
		else if (Size.class.getName().equals(clsName))
			return new SizeImpl((Size) ann);
		else if (Url.class.getName().equals(clsName))
			return new UrlImpl((Url) ann);
		else if (Zip.class.getName().equals(clsName))
			return new ZipImpl((Zip) ann);
		else if (MyValidator.class.getName().equals(clsName))
			return new MyValidatorImpl((MyValidator) ann);

		return null;
	}
}
