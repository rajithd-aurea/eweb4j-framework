package org.eweb4j.mvc.validator;

/**
 * 验证器工厂
 * @author weiwei
 *
 */
public class ValidatorFactory {
	public static ValidatorIF getValidator(String type){
		if (Validators.REQUIRED.equalsIgnoreCase(type)){
			//必填字段
			return new RequriedValidator();
		}else if (Validators.EMAIL.equalsIgnoreCase(type)){
			//字符串长度
			return new EmailValidator();
		}else if (Validators.EQUALS.equalsIgnoreCase(type)){
			//比较值
			return new EqualsValidator();
		}else if (Validators.INT.equalsIgnoreCase(type)){
			//数字
			return new IntegerValidator();
		}else if (Validators.DATE.equalsIgnoreCase(type)){
			//日期
			return new DateValidator();
		}else if (Validators.URL.equalsIgnoreCase(type)){
			//URL
			return new URLValidator();
		}else if (Validators.ID_CARD.equalsIgnoreCase(type)){
			//身份证
			return new IDCardValidator();
		}else if (Validators.ZIP.equalsIgnoreCase(type)){
			//邮编
			return new ZIPValidator();
		}else if (Validators.PHONE.equalsIgnoreCase(type)){
			//电话号码
			return new PhoneValidator();
		}else if (Validators.QQ.equalsIgnoreCase(type)){
			//QQ
			return new QQValidator();
		}else if (Validators.CHINESE.equalsIgnoreCase(type)){
			//中文
			return new ChineseValidator();
		}else if (Validators.IP.equalsIgnoreCase(type)){
			//IP
			return new IPValidator();
		}else if (Validators.LENGTH.equalsIgnoreCase(type)){
			//字符串长度
			return new StringLengthValidator();
		}else if (Validators.SIZE.equalsIgnoreCase(type)){
			//数字大小
			return new IntegerSizeValidator();
		}else if (Validators.FORBID.equalsIgnoreCase(type)){
			//数字大小
			return new ForbidWordValidator();
		}else if (Validators.ENUM.equalsIgnoreCase(type)){
			//枚举类型
			return new EnumValidator();
		}
			
		return null;
	}
}
