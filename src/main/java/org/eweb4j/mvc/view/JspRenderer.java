package org.eweb4j.mvc.view;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.util.JsonConverter;


public class JspRenderer implements Renderer {
	private HttpServletRequest req;
	private String format = "pojo";

	public JspRenderer(HttpServletRequest req,String format) {
		this.req = req;
		this.format = format;
	}
	
	public JspRenderer(HttpServletRequest req) {
		this.req = req;
	}

	public <T> void render(String[] names,T... ts) {
		for (int i = 0; i < ts.length; i++){
			if ("pojo".equalsIgnoreCase(format)){
				this.req.setAttribute(names[i], ts[i]);
			}else if ("json".equals(format)){
				this.req.setAttribute(names[i], JsonConverter.convert(ts[i]));
			}else if ("xml".equals(format)){
				//暂不支持
			}
		}
	}
	
	public <T> void render(String name, T t){
		if ("pojo".equalsIgnoreCase(format)){
			this.req.setAttribute(name, t);
		}else if ("json".equals(format)){
			this.req.setAttribute(name, JsonConverter.convert(t));
		}else if ("xml".equals(format)){
			//暂不支持
		}
	}

	public HttpServletRequest getReq() {
		return req;
	}

	public void setReq(HttpServletRequest req) {
		this.req = req;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}
