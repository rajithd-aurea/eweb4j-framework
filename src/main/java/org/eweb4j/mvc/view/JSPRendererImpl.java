package org.eweb4j.mvc.view;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.config.MVCConfigConstant;

public class JSPRendererImpl extends Renderer{

	private Context context;

	public void setContext(Context context){
		this.context = context;
	}
	
	public String render(Map<String, Object> datas) {
		
		return null;
	}

	public String render(String name, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	public String render() {
		
		return null;
	}

	public void render(Writer writer, Map<String, Object> datas) {
		try {
			HttpServletRequest req = context.getRequest();
			HttpServletResponse res = context.getResponse();
			
			if (datas != null) {
				for (Iterator<Entry<String, Object>> it = datas.entrySet().iterator(); it.hasNext(); ) {
					Entry<String, Object> entry = it.next();
					req.setAttribute(entry.getKey(), entry.getValue());
				}
			}

			if(layout != null){
				for (Iterator<Entry<String, String>> it = this.paths.entrySet().iterator(); it.hasNext(); ){
					Entry<String, String> e = it.next();
					String paramName = e.getKey();
					String path = e.getValue();
					BufferedResponse my_res = new BufferedResponse(res);
					context.getServletContext().getRequestDispatcher(MVCConfigConstant.FORWARD_BASE_PATH + "/" + path).include(req, my_res);
					String screenContent = my_res.getScreenContent();
					req.setAttribute(paramName, screenContent);
				}
				
				context.getServletContext().getRequestDispatcher(MVCConfigConstant.FORWARD_BASE_PATH + "/" + layout).forward(req, res);
			} else {
				String defaultPath = paths.get(MVCConfigConstant.LAYOUT_SCREEN_CONTENT_KEY);
				context.getServletContext().getRequestDispatcher(MVCConfigConstant.FORWARD_BASE_PATH + "/" + defaultPath).forward(req, res);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void render(Writer writer) {
		render(writer, new HashMap<String, Object>());
	}

}
