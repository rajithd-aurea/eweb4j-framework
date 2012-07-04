package org.eweb4j.mvc.view;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询表单
 * 
 * @author weiwei
 * 
 */
public class SearchForm {
	private String action;
	private String keyword;
	private Map<String, Object> params = new HashMap<String, Object>();

	public SearchForm(String action, String keyword) {
		this.action = action;
		this.keyword = keyword;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

}
