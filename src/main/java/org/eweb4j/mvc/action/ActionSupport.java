package org.eweb4j.mvc.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eweb4j.mvc.config.bean.ActionConfigBean;
import org.eweb4j.mvc.config.bean.ParamConfigBean;


/**
 * MVC组件Action抽象类，实现初始化方法，将request和response两个对象从过滤器中传进来
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public abstract class ActionSupport implements IAction {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ActionConfigBean configBean;// 包含配置内容的bean
	protected Map<String, String> param;// 参数常量
	private PrintWriter out;

	public ActionSupport init(HttpServletRequest request,
			HttpServletResponse response, ActionConfigBean configBean)
			throws Exception {
		this.request = request;
		this.response = response;
		this.configBean = configBean;
		this.param = new HashMap<String, String>();
		if (this.configBean != null) {
			List<ParamConfigBean> pList = this.configBean.getParam();
			if (pList != null) {
				for (ParamConfigBean p : pList) {
					this.param.put(p.getName(), p.getValue());
				}
			}
		}
		return this;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ActionConfigBean getConfigBean() {
		return configBean;
	}

	public void setConfigBean(ActionConfigBean configBean) {
		this.configBean = configBean;
	}

	public Map<String, String> getParam() {
		return param;
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	public PrintWriter getOut() {
		if (out == null) {
			try {
				out = response.getWriter();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}
}
