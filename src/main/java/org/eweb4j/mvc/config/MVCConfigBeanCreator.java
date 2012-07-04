package org.eweb4j.mvc.config;

import java.util.ArrayList;
import java.util.List;

import org.eweb4j.mvc.config.bean.ActionConfigBean;
import org.eweb4j.mvc.config.bean.FieldConfigBean;
import org.eweb4j.mvc.config.bean.InterConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;
import org.eweb4j.mvc.config.bean.ResultConfigBean;
import org.eweb4j.mvc.config.bean.Uri;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;

/**
 * 创建MVC存取配置信息bean实例
 * @author cfuture.aw
 * @since v1.a.0
 *
 */
public class MVCConfigBeanCreator {
	public static ActionConfigBean getActionBean() {
		ActionConfigBean mvcBean = null;
		mvcBean = new ActionConfigBean();
		List<ResultConfigBean> rlist = new ArrayList<ResultConfigBean>();
		ResultConfigBean result = new ResultConfigBean();
		rlist.add(result);
		mvcBean.setResult(rlist);
		
		List<ValidatorConfigBean> vlist = new ArrayList<ValidatorConfigBean>();
		ValidatorConfigBean validator = new ValidatorConfigBean();
		List<FieldConfigBean> fieldList = new ArrayList<FieldConfigBean>();
		FieldConfigBean field = new FieldConfigBean();
		List<ParamConfigBean> paramList = new ArrayList<ParamConfigBean>();
		ParamConfigBean param = new ParamConfigBean();
		paramList.add(param);
		field.setParam(paramList);
		fieldList.add(field);
		validator.setField(fieldList);
		
		
		
		vlist.add(validator);
		mvcBean.setValidator(vlist);
		mvcBean.setParam(paramList);
		
		return mvcBean;
	}
	
	public static InterConfigBean getInterBean(){
		InterConfigBean icb = new InterConfigBean();
		List<Uri> urls = new ArrayList<Uri>();
		Uri url = new Uri();
		urls.add(url);
		icb.setUri(urls);
		List<String> excepts = new ArrayList<String>();
		excepts.add("");
		icb.setExcept(excepts);
		return icb;
	}
}
