package org.eweb4j.mvc.view;

import javax.servlet.http.HttpServletRequest;

public class RenderUtil {
	public static Renderer getJSPRenderer(HttpServletRequest req) {
		return new JspRenderer(req);
	}
}
