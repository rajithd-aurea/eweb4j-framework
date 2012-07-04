package org.eweb4j.mvc.interceptor;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.eweb4j.cache.ActionConfigBeanCache;
import org.eweb4j.cache.InterConfigBeanCache;
import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.action.ActionExecution;
import org.eweb4j.mvc.action.RenderType;
import org.eweb4j.mvc.config.MVCConfigConstant;
import org.eweb4j.mvc.config.bean.ActionConfigBean;
import org.eweb4j.mvc.config.bean.InterConfigBean;
import org.eweb4j.mvc.config.bean.Uri;
import org.eweb4j.util.ReflectUtil;
import org.eweb4j.util.StringUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

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
			int result = 1;
			
			for (int i = 0; i < size; i++) {
				Uri u = uris.get(i);
				String type = u.getType();
				String value = u.getValue();
				int c = 1;
				
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
						c = 0;

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

					if (ActionConfigBeanCache.containsKey(uri) || (ActionConfigBeanCache.getByMatches(uri,context.getHttpMethod())) != null) {
						// 找到了action 与当前访问的uri映射
						c = 0;
					}
				} else if ("*".equals(type)) {
					// 所有都匹配
					;
				} else
					c = 0;

				if (isOR)
					result += c;
				else {
					if (c == 0)
						break;

					result *= c;
				}
				
				if (i < size - 1)		
					continue;

				if (result == 0)
					continue;

				this.doIntercept(inter);
				
				if (this.error == null){
					// 如果拦截处理之后没有任何错误信息，进入下一个URI继续处理
					continue;
				}else{
					// 否则显示错误信息, 并且退出方法
					logErr();
					
					return true;
				}
			}
		}

		return false;
	}
	
	private void doIntercept(InterConfigBean inter) throws Exception {
		Object interceptor = null;
		if ("singleton".equalsIgnoreCase(inter.getScope()))
			interceptor = SingleBeanCache.get(inter.getClazz());
		
		if (interceptor == null){
			interceptor = Class.forName(inter.getClazz()).newInstance();
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

		final String re = error;
		final String baseUrl = (String) this.context.getServletContext().getAttribute(MVCConfigConstant.BASE_URL_KEY);
		
		// 客户端重定向
		if (re.startsWith(RenderType.REDIRECT + ":")) {
			String url = re.substring((RenderType.REDIRECT + ":").length());
			String location = url;

			this.context.getResponse().sendRedirect(StringUtil.replaceChinese2Utf8(location));

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
		} else if (re.startsWith(RenderType.FORWARD + ":")) {
			String location = re.substring((RenderType.FORWARD + ":").length());
			HttpServletRequest request = this.context.getRequest();
			request.setAttribute(MVCConfigConstant.REQ_PARAM_MAP_NAME, this.context.getQueryParamMap());

			for (Iterator<Entry<String, Object>> it = context.getModel().entrySet().iterator(); it.hasNext(); ) {
				Entry<String, Object> entry = it.next();
				request.setAttribute(entry.getKey(), entry.getValue());
			}

			// 服务端跳转
			request.getRequestDispatcher(MVCConfigConstant.FORWARD_BASE_PATH + location).forward(request, this.context.getResponse());

			return;
		} else if (re.startsWith(RenderType.FREEMARKER + ":")) {
			String location = re.substring((RenderType.FREEMARKER + ":").length());
			// FreeMarker 渲染
			Configuration cfg = new Configuration();
			// 指定模板从何处加载的数据源，这里设置成一个文件目录。
			cfg.setDirectoryForTemplateLoading(new File(ConfigConstant.ROOT_PATH + MVCConfigConstant.FORWARD_BASE_PATH));
			// 指定模板如何检索数据模型
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setDefaultEncoding("utf-8");

			Template template = cfg.getTemplate(location);
			template.setEncoding("utf-8");

			template.process(context.getModel(), this.context.getWriter());

			return;
		} else{
			this.context.getWriter().print(re);
			this.context.getWriter().flush();
		}
	}

	private void logErr() {
		StringBuilder sb = new StringBuilder();
		sb.append("intercepte -> ").append(this.context.getUri());
		sb.append(" error info -> ").append(this.error);
		log.debug(sb.toString());
	}

	public String getError() {
		return error;
	}

}
