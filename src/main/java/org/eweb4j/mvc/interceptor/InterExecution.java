package org.eweb4j.mvc.interceptor;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eweb4j.cache.ActionConfigBeanCache;
import org.eweb4j.cache.InterConfigBeanCache;
import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.action.ActionExecution;
import org.eweb4j.mvc.action.RenderType;
import org.eweb4j.mvc.config.MVCConfigConstant;
import org.eweb4j.mvc.config.bean.ActionConfigBean;
import org.eweb4j.mvc.config.bean.InterConfigBean;
import org.eweb4j.mvc.config.bean.Uri;
import org.eweb4j.mvc.view.JSPRendererImpl;
import org.eweb4j.mvc.view.RenderFactory;
import org.eweb4j.mvc.view.Renderer;
import org.eweb4j.util.CommonUtil;
import org.eweb4j.util.ReflectUtil;

public class InterExecution {

	private static Log log = LogFactory.getMVCLogger(InterExecution.class);

	private String interType = null;
	private Context context = null;
	private String error = null;

	public InterExecution(String interType, Context context) {
		this.interType = interType;
		this.context = context;
		
		Map<String, List<?>> map = null;
		if (ActionConfigBeanCache.containsKey(this.context.getUri()) || (map = ActionConfigBeanCache.getByMatches(this.context.getUri(), context.getHttpMethod())) != null) {
			// 找到了action 与当前访问的uri映射
			if (map.containsKey("mvcBean")) {
				ActionConfigBean acb = (ActionConfigBean) map.get("mvcBean").get(0);
				this.context.setActionConfigBean(acb);
			}
		}
	}

	/**
	 * 找
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean findAndExecuteInter() throws Exception {
		
		List<InterConfigBean> list = InterConfigBeanCache.getList();
		
		// 按优先级从高到低排序
		Collections.sort(list, new InterPriorityComparator());
		
		final int listSize = list.size();
		for (int index = 0; index < listSize;index++) {
			InterConfigBean inter = list.get(index);
			String _interType = inter.getType();
			if (!interType.equals(_interType))
				continue;
			String uri = this.context.getUri();
			if (uri.length() == 0)
				uri = " ";
			
			if (inter.getExcept().contains(uri))
				continue;
			
			String policy = inter.getPolicy();
			boolean isOR = "or".equalsIgnoreCase(policy) ? true : false;

			List<Uri> uris = inter.getUri();
			final int size = uris.size();
			boolean canProcess = false;// 默认不能处理
			
			for (int i = 0; i < size; i++) {
				Uri u = uris.get(i);
				String type = u.getType();
				String value = u.getValue();
				
				boolean found = true;// 默认找到
				
				if ("start".equalsIgnoreCase(type) && uri.startsWith(value))
					// 以url开头
					;
				else if ("end".equalsIgnoreCase(type) && uri.endsWith(value))
					// 以url结尾
					;
				else if ("contains".equalsIgnoreCase(type)&& uri.contains(value))
					// 包含url
					;
				else if ("all".equalsIgnoreCase(type) && uri.equals(value))
					// 完全匹配url
					;
				else if ("regex".equalsIgnoreCase(type) && uri.matches(value))
					// 正则匹配
					;
				else if ("actions".equalsIgnoreCase(type)) {
					if (!findActionUriMapping())
						found = false;

				} else if ("!start".equalsIgnoreCase(type) && !uri.startsWith(value))
					// 不以url开头
					;
				else if ("!end".equalsIgnoreCase(type) && !uri.endsWith(value))
					// 不以url结尾
					;
				else if ("!contains".equalsIgnoreCase(type)
						&& !uri.contains(value))
					// 不包含url
					;
				else if ("!all".equalsIgnoreCase(type) && !uri.equals(value))
					// 不完全匹配url
					;
				else if ("!regex".equalsIgnoreCase(type) && !uri.matches(value))
					// 不正则匹配
					;
				else if ("!actions".equalsIgnoreCase(type)) {

					if (ActionConfigBeanCache.containsKey(uri) || (ActionConfigBeanCache.getByMatches(uri,context.getHttpMethod())) != null) 
						found = false;
				} else if ("*".equals(type)) {
					// 所有都匹配
					;
				} else
					found = false;

//				log.debug("uri -> " +value+ " found -> " + found);
				
				// 如果是 或者 
				if (isOR){
					// 如果找到一个规则符合，就可以执行了。
					if (found){
						canProcess = true;
						break;
					}
				}else {
					// 如果是 并且
					if (!found){
						//只要找到一个规则不符合，就退出，且不处理
						canProcess = false;
						break;
					}else{
						canProcess = true;
					}
				}

			}// find uri match
			
			if (!canProcess)
				continue;
			
			this.doIntercept(inter);
			
			if (this.error == null){
				// 如果拦截处理之后没有任何错误信息，进入下一个拦截器继续处理
				continue;
			}else{
				// 否则显示错误信息, 并且退出方法
				log.debug("do interceptor -> " + inter.getClazz() + " error -> " + error);
				return true;
			}
		}

		return false;
	}
	
	private void doIntercept(InterConfigBean inter) throws Exception {
		Object interceptor = null;
		if ("singleton".equalsIgnoreCase(inter.getScope()))
			interceptor = SingleBeanCache.get(inter.getClazz());
		
		if (interceptor == null){
			interceptor = Thread.currentThread().getContextClassLoader().loadClass(inter.getClazz()).newInstance();
			if ("singleton".equalsIgnoreCase(inter.getScope()))
				SingleBeanCache.add(inter.getClazz(), interceptor);
		}
		
		ReflectUtil ru = new ReflectUtil(interceptor);
		Method intercept = ru.getMethod(inter.getMethod());
		if (intercept == null){
			this.error = null ;
			return ;
		}
		
		Method setter = ru.getSetter("Context");
		if (setter != null)
			setter.invoke(interceptor, this.context);
		
		Object err = null;
		
		Class<?>[] paramCls = intercept.getParameterTypes();
		if (paramCls.length == 1 && paramCls[0].isAssignableFrom(Context.class))
			err = intercept.invoke(interceptor, this.context);
		else
			err = intercept.invoke(interceptor);
		
		if (err == null){
			this.error = null;
			return ;
		}
		
		this.error = String.valueOf(err);
	}
	
	public void execute(Class<?> _interceptor) throws Exception{
		Object interceptor = _interceptor.newInstance();
		for (InterConfigBean inter : InterConfigBeanCache.getList()) {
			if (inter.getClazz().equals(interceptor.getClass().getName())){
				
				this.doIntercept(inter);
				return ;
			}
		}
	}

	private boolean findActionUriMapping() {
		boolean result = false;
		Map<String, List<?>> map = null;
		if (ActionConfigBeanCache.containsKey(this.context.getUri()) || (map = ActionConfigBeanCache.getByMatches(this.context.getUri(), context.getHttpMethod())) != null) {
			// 找到了action 与当前访问的uri映射
			if (map.containsKey("mvcBean")) {
				ActionConfigBean acb = (ActionConfigBean) map.get("mvcBean").get(0);
				this.context.setActionConfigBean(acb);
				result = true;
			}
		}

		return result;
	}

	/**
	 * 显示拦截器执行之后的错误信息
	 * 
	 * @param error
	 * @throws Exception
	 */
	public void showErr() throws Exception {

		String re = error;
		final String baseUrl = (String) this.context.getServletContext().getAttribute(MVCConfigConstant.BASE_URL_KEY);
		
		// 客户端重定向
		if (re.startsWith(RenderType.REDIRECT + ":")) {
			String url = re.substring((RenderType.REDIRECT + ":").length());
			String location = url;

			this.context.getResponse().sendRedirect(CommonUtil.replaceChinese2Utf8(location));

			return;
		} else if (re.startsWith(RenderType.ACTION + ":")) {
			String path = re.substring((RenderType.ACTION + ":").length());
			// ACTION 重定向
			ActionExecution.handleActionRedirect(context, path, baseUrl);

			return;
		} else if (re.startsWith(RenderType.OUT + ":")) {
			String location = re.substring((RenderType.OUT + ":").length());
			this.context.getWriter().print(location);
			this.context.getWriter().flush();

			return;
		} else if (re.startsWith(RenderType.FORWARD + ":") 
				|| re.startsWith(RenderType.JSP + ":")
				|| re.endsWith("."+RenderType.JSP)) {
			String[] str = re.split("@");
			re = str[0];
			String location = re;
			if (re.startsWith(RenderType.FORWARD + ":"))
				location = re.substring((RenderType.FORWARD + ":").length());
			else if (re.startsWith(RenderType.JSP + ":"))
				location = re.substring((RenderType.JSP + ":").length());
			
			//渲染JSP
	        JSPRendererImpl render = new JSPRendererImpl();
	        render.setContext(context);
	        if (str.length > 1)
	        	render.layout(str[1]);
	        
	        render.target(location).render(context.getWriter(), context.getModel());
			
			return;
		} else if (re.startsWith(RenderType.FREEMARKER + ":") 
				|| re.startsWith(RenderType.FREEMARKER2 + ":")
				|| re.endsWith("."+RenderType.FREEMARKER2)) {
			String[] str = re.split("@");
			re = str[0];
			String location = re;
			if (re.startsWith(RenderType.FREEMARKER + ":"))
				location = re.substring((RenderType.FREEMARKER + ":").length());
			else if (re.startsWith(RenderType.FREEMARKER2 + ":"))
				location = re.substring((RenderType.FREEMARKER2 + ":").length());
			
			//渲染Freemarker
	        Renderer render = RenderFactory.create(RenderType.FREEMARKER).target(location);
	        if (str.length > 1)
	        	render.layout(str[1]);
	        
	        render.render(context.getWriter(), context.getModel());
			
	        this.context.getWriter().flush();
			return;
		}else if (re.startsWith(RenderType.VELOCITY + ":") 
				|| re.startsWith(RenderType.VELOCITY2 + ":")
				|| re.endsWith("."+RenderType.VELOCITY2)) {
			String[] str = re.split("@");
			re = str[0];
			String location = re;
			if (re.startsWith(RenderType.VELOCITY + ":"))
				location = re.substring((RenderType.VELOCITY + ":").length());
			else if (re.startsWith(RenderType.VELOCITY2 + ":"))
				location = re.substring((RenderType.VELOCITY2 + ":").length());
			
			//渲染Velocity
	        Renderer render = RenderFactory.create(RenderType.VELOCITY).target(location);
	        if (str.length > 1)
	        	render.layout(str[1]);
	        
	        render.render(context.getWriter(),context.getModel());
			
	        this.context.getWriter().flush();
			return;
		} else{
			this.context.getWriter().print(re);
			this.context.getWriter().flush();
		}
	}

	public String getError() {
		return error;
	}

}
