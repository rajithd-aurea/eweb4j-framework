package org.eweb4j.mvc.config.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.HttpMethod;

import org.eweb4j.util.xml.AttrTag;
import org.eweb4j.util.xml.Skip;

/**
 * MVC组件用来存取配置信息的Bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class ActionConfigBean {
	@AttrTag
	private String level = "1";

	private List<String> produces = new ArrayList<String>();

	private List<String> consumes = new ArrayList<String>();

	@Skip
	private String[] pathParams;

	@AttrTag
	private String httpMethod = HttpMethod.GET;

	@AttrTag
	private String uriMapping;

	@AttrTag
	private String method;

	@AttrTag
	private String clazz;

	@AttrTag
	private String showValErrorType;

	private List<ParamConfigBean> param = new ArrayList<ParamConfigBean>();
	private List<ResultConfigBean> result = new ArrayList<ResultConfigBean>();
	private List<ValidatorConfigBean> validator = new ArrayList<ValidatorConfigBean>();

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public List<String> getProduces() {
		return produces;
	}

	public void setProduces(List<String> produces) {
		this.produces = produces;
	}

	public List<String> getConsumes() {
		return consumes;
	}

	public void setConsumes(List<String> consumes) {
		this.consumes = consumes;
	}

	public List<ParamConfigBean> getParam() {
		return param;
	}

	public void setParam(List<ParamConfigBean> param) {
		this.param = param;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String[] getPathParams() {
		return pathParams;
	}

	public void setPathParams(String[] pathParams) {
		this.pathParams = pathParams;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getShowValErrorType() {
		return showValErrorType;
	}

	public void setShowValErrorType(String showValErrorType) {
		this.showValErrorType = showValErrorType;
	}

	public List<ValidatorConfigBean> getValidator() {
		return validator;
	}

	public void setValidator(List<ValidatorConfigBean> validator) {
		this.validator = validator;
	}

	public List<ResultConfigBean> getResult() {
		return result;
	}

	public void setResult(List<ResultConfigBean> result) {
		this.result = result;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getUriMapping() {
		return uriMapping;
	}

	public void setUriMapping(String uriMapping) {
		this.uriMapping = uriMapping;
	}

	@Override
	public String toString() {
		return "ActionConfigBean [level=" + level + ", produces=" + produces
				+ ", consumes=" + consumes + ", pathParams="
				+ Arrays.toString(pathParams) + ", httpMethod=" + httpMethod
				+ ", uriMapping=" + uriMapping + ", method=" + method
				+ ", clazz=" + clazz + ", showValErrorType=" + showValErrorType
				+ ", param=" + param + ", result=" + result + ", validator="
				+ validator + "]";
	}

}
