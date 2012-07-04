package org.eweb4j.mvc.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eweb4j.cache.InterConfigBeanCache;
import org.eweb4j.mvc.config.bean.InterConfigBean;
import org.eweb4j.mvc.config.bean.Uri;
import org.eweb4j.mvc.validator.Validators;
import org.eweb4j.mvc.view.CallBackJson;


/**
 * 拦截器工具，
 * 
 * @author weiwei
 * 
 */
public class InterUtil {
	public static Object findInterByURI(String uri, String interType)
			throws Exception {
		Object interceptor = null;

		for (InterConfigBean inter : InterConfigBeanCache.getList()) {
			String _interType = inter.getType();
			if (!interType.equals(_interType))
				continue;

			if (inter.getExcept().contains(uri))
				continue;

			for (Uri u : inter.getUri()) {
				String type = u.getType();
				String value = u.getValue();

				if ("start".equalsIgnoreCase(type) && uri.startsWith(value)) {
					// 以url开头
				} else if ("end".equalsIgnoreCase(type) && uri.endsWith(value)) {
					// 以url结尾
				} else if ("contains".equalsIgnoreCase(type)
						&& uri.contains(value)) {
					// 包含url
				} else if ("all".equalsIgnoreCase(type) && uri.equals(value)) {
					// 完全匹配url
				} else if ("regex".equalsIgnoreCase(type) && uri.matches(value)) {
					// 正则匹配
				} else if ("*".equals(type)) {
					// 所有都匹配
				} else {
					continue;
				}

				interceptor = Class.forName(inter.getClazz()).newInstance();
			}
		}

		return interceptor;
	}

	public static void handleInterErr(String showErrorType, String error,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (Validators.DEFAULT_LOC.equals(showErrorType)
				|| Validators.ALERT_LOC.equals(showErrorType)) {
			PrintWriter out = res.getWriter();
			out.print("<script>alert('" + error
					+ "');javascript:history.go(-1)</script><center></center>");
			out.flush();
			out.close();
		} else if (Validators.OUT_LOC.equals(showErrorType)) {
			PrintWriter out = res.getWriter();
			out.print(error);
			out.flush();
			out.close();
		} else if (Validators.DWZ_JSON_LOC.equals(showErrorType)) {
			PrintWriter out = res.getWriter();
			out.print(new CallBackJson(error));
			out.flush();
			out.close();
		} else if (Validators.JAVASCRIPT_LOC.equals(showErrorType)) {
			PrintWriter out = res.getWriter();
			out.print("<script>" + error + "</script>");
			out.flush();
			out.close();
		} else {
			// 如果是填写跳转页面的话
			req.setAttribute("interError", error);
			req.getRequestDispatcher(showErrorType).forward(req, res);
		}
	}
}
