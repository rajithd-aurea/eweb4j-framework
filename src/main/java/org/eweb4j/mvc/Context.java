package org.eweb4j.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eweb4j.mvc.action.ActionProp;
import org.eweb4j.mvc.action.QueryParams;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.bean.ActionConfigBean;
import org.eweb4j.mvc.upload.UploadFile;

/**
 * EWeb4J MVC Action 上下文
 * @author weiwei
 *
 */
public class Context {
	
	/**
	 * 一次Action执行的验证器验证结果封装
	 */
	private Validation validation;
	
	/**
	 * application
	 */
	private ServletContext servletContext;
	
	/**
	 * http request
	 */
	private HttpServletRequest request;
	
	/**
	 * http response
	 */
	private HttpServletResponse response;
	
	/**
	 * http session
	 */
	private HttpSession session;
	
	/**
	 * response output stream
	 */
	private PrintWriter writer;
	
	/**
	 * reponse output stream
	 */
	private ServletOutputStream out;
	
	/**
	 * http request method
	 */
	private String httpMethod;
	
	/**
	 * a properties which named action class name
	 */
	private ActionProp actionProp;
	
	/**
	 * http query params
	 */
	private Map<String, String[]> queryParamMap;
	
	/**
	 * http query params for uri variable
	 */
	private Map<String, String[]> pathParamMap;
	
	private Map<String, List<UploadFile>> uploadMap = new HashMap<String, List<UploadFile>>();
	
	/**
	 * a pojo for action configuration info such as uri, validator etc.
	 */
	private ActionConfigBean actionConfigBean;
	
	/**
	 * http request uri
	 */
	private String uri;
	
	/**
	 * a pojo which contains QueryParamMap and PathParamMap
	 */
	private QueryParams queryParams;
	
	/**
	 * a map for data of view render
	 */
	private Map<String, Object> model = new HashMap<String,Object>();

	public Context() {
	}

	public Context(ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			PrintWriter writer, ServletOutputStream out, String httpMethod) {
		super();
		this.servletContext = servletContext;
		this.request = request;
		this.response = response;
		this.session = session;
		this.writer = writer;
		this.out = out;
		this.httpMethod = httpMethod;
	}

	public Context(ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			PrintWriter writer, ServletOutputStream out, String httpMethod,
			ActionProp actionProp, Map<String, String[]> queryParamMap,
			Map<String, String[]> pathParamMap, ActionConfigBean mvcBean,
			String uri, QueryParams queryParams) {
		super();
		this.servletContext = servletContext;
		this.request = request;
		this.response = response;
		this.session = session;
		this.writer = writer;
		this.out = out;
		this.httpMethod = httpMethod;
		this.actionProp = actionProp;
		this.queryParamMap = queryParamMap;
		this.pathParamMap = pathParamMap;
		this.actionConfigBean = mvcBean;
		this.uri = uri;
		this.queryParams = queryParams;
	}

	public Map<String, List<UploadFile>> getUploadMap() {
		return uploadMap;
	}

	public void setUploadMap(Map<String, List<UploadFile>> uploadMap) {
		this.uploadMap = uploadMap;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpSession getSession() {
		if (session == null)
			session = request.getSession();

		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public PrintWriter getWriter() throws IOException {
		if (writer == null)
			writer = response.getWriter();

		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public ServletOutputStream getOut() throws IOException {
		if (out == null)
			out = response.getOutputStream();

		return out;
	}

	public void setOut(ServletOutputStream out) {
		this.out = out;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public ActionProp getActionProp() {
		return actionProp;
	}

	public void setActionProp(ActionProp actionProp) {
		this.actionProp = actionProp;
	}

	public Map<String, String[]> getQueryParamMap() {
		if (queryParamMap == null)
			queryParamMap = new HashMap<String, String[]>();

		return queryParamMap;
	}

	public void setQueryParamMap(Map<String, String[]> queryParamMap) {
		this.queryParamMap = queryParamMap;
	}

	public Map<String, String[]> getPathParamMap() {
		if (pathParamMap == null)
			pathParamMap = new HashMap<String, String[]>();

		return pathParamMap;
	}

	public void setPathParamMap(Map<String, String[]> pathParamMap) {
		this.pathParamMap = pathParamMap;
	}

	public ActionConfigBean getActionConfigBean() {
		return actionConfigBean;
	}

	public void setActionConfigBean(ActionConfigBean actionConfigBean) {
		this.actionConfigBean = actionConfigBean;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public QueryParams getQueryParams() {
		if (queryParams == null) {
			queryParams = new QueryParams();
			queryParams.setMap(queryParamMap);
		}
		queryParams.putAll(pathParamMap);

		return queryParams;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	public void setQueryParams(QueryParams queryParams) {
		this.queryParams = queryParams;
	}

	public Validation getValidation() {
		return validation;
	}

	public void setValidation(Validation validation) {
		this.validation = validation;
	}

}
