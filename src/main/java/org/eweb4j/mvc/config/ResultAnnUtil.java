package org.eweb4j.mvc.config;

import java.util.ArrayList;
import java.util.List;

import org.eweb4j.mvc.action.annotation.Result;
import org.eweb4j.mvc.config.bean.ResultConfigBean;
import org.eweb4j.util.StringUtil;

/**
 * MVC.Result注解 工具类
 * 
 * @author weiwei
 * 
 */
public class ResultAnnUtil {
	/**
	 * 读@Result注解的信息到配置Cache中
	 * 
	 * @param resultAnn
	 * @return
	 */
	public static List<ResultConfigBean> readResultAnn(Result resultAnn) {
		List<ResultConfigBean> rList = new ArrayList<ResultConfigBean>();
		String[] name = resultAnn.name();
		String[] type = resultAnn.type();
		String[] location = resultAnn.value();
		for (int a = 0; a < name.length; ++a) {
			ResultConfigBean r = new ResultConfigBean();

			r.setName(StringUtil.parsePropValue(name[a]));
			r.setType(StringUtil.parsePropValue(type[a]));
			r.setLocation(StringUtil.parsePropValue(location[a]));
			rList.add(r);
		}

		return rList;
	}
}
