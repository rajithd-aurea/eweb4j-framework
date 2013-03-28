package org.eweb4j.mvc.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;

import org.eweb4j.cache.ActionConfigBeanCache;
import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.ioc.IOC;
import org.eweb4j.mvc.Context;
import org.eweb4j.mvc.Http;
import org.eweb4j.mvc.MIMEType;
import org.eweb4j.mvc.ParamUtil;
import org.eweb4j.mvc.action.annotation.Ioc;
import org.eweb4j.mvc.action.annotation.Singleton;
import org.eweb4j.mvc.action.annotation.Transactional;
import org.eweb4j.mvc.config.ActionClassCache;
import org.eweb4j.mvc.config.ConsumesUtil;
import org.eweb4j.mvc.config.JAXWSUtil;
import org.eweb4j.mvc.config.MVCConfigConstant;
import org.eweb4j.mvc.config.PathUtil;
import org.eweb4j.mvc.config.ProducesUtil;
import org.eweb4j.mvc.config.QueryParamUtil;
import org.eweb4j.mvc.config.bean.ActionConfigBean;
import org.eweb4j.mvc.config.bean.ResultConfigBean;
import org.eweb4j.mvc.interceptor.After;
import org.eweb4j.mvc.interceptor.Before;
import org.eweb4j.mvc.interceptor.InterExecution;
import org.eweb4j.mvc.upload.UploadFile;
import org.eweb4j.mvc.validator.ValidateExecution;
import org.eweb4j.mvc.validator.annotation.DateFormat;
import org.eweb4j.mvc.validator.annotation.Upload;
import org.eweb4j.mvc.view.JSPRendererImpl;
import org.eweb4j.mvc.view.RenderFactory;
import org.eweb4j.mvc.view.Renderer;
import org.eweb4j.orm.dao.DAO;
import org.eweb4j.orm.dao.DAOFactory;
import org.eweb4j.orm.dao.DAOImpl;
import org.eweb4j.orm.dao.cascade.CascadeDAO;
import org.eweb4j.orm.dao.delete.DeleteDAO;
import org.eweb4j.orm.dao.insert.InsertDAO;
import org.eweb4j.orm.dao.select.DivPageDAO;
import org.eweb4j.orm.dao.select.SearchDAO;
import org.eweb4j.orm.dao.select.SelectDAO;
import org.eweb4j.orm.dao.update.UpdateDAO;
import org.eweb4j.orm.jdbc.transaction.Trans;
import org.eweb4j.orm.jdbc.transaction.Transaction;
import org.eweb4j.util.ClassUtil;
import org.eweb4j.util.CommonUtil;
import org.eweb4j.util.ReflectUtil;
import org.eweb4j.util.xml.BeanXMLUtil;
import org.eweb4j.util.xml.XMLWriter;

/**
 * Action 执行器
 * 
 * @author weiwei
 * @since 1.b.8
 * 
 */
@SuppressWarnings("all")
public class ActionExecution {

	private static Log log = LogFactory.getMVCLogger(ActionExecution.class);

	// 要执行的Action方法
	private Method method;
	private Field[] fields;
	
	private Context context;

	private Object actionObject;
	private Object retn;

	private ReflectUtil ru;

	// 验证器
	private void handleValidator() throws Exception {

		this.context.setValidation(ValidateExecution.checkValidate(context.getActionConfigBean().getValidator(), context.getQueryParamMap(), context.getRequest()));
	}

	public ActionExecution(String uri, String httpMethod, Context context) {
		this.context = context;
		this.context.setUri(uri);
		this.context.setHttpMethod(httpMethod);
	}

	public boolean findAction() throws Exception {
		// URL参数
		Map<String, List<?>> pathParams = null;
		if (ActionConfigBeanCache.containsKey(this.context.getUri()) 
				|| (pathParams = ActionConfigBeanCache.getByMatches(this.context.getUri(), this.context.getHttpMethod())) != null) {

			// 处理形如" /xxx/{id}/{name} "的URI
			if (pathParams != null && pathParams.containsKey("mvcBean")) {
				// 根据Url配置的UrlParam获取参数值
				this.context.setActionConfigBean((ActionConfigBean) pathParams.get("mvcBean").get(0));
				Map<String, String[]> pathParamMap = ParamUtil.getPathParamMap(pathParams);
//				System.out.println("pp->\n\t"+pathParamMap);
				this.context.getPathParamMap().putAll(pathParamMap);
//				System.out.println("pp222->\n\t"+this.context.getPathParamMap());
				this.context.getQueryParamMap().putAll(this.context.getPathParamMap());
			} else
				this.context.setActionConfigBean(ActionConfigBeanCache.get(this.context.getUri()));

			if (this.context.getActionConfigBean() != null)
				return true;
		}

		return false;
	}

	private Object initPojo() throws Exception {
		Class<?> clazz = ActionClassCache.get(this.context.getActionConfigBean().getClazz());
		Annotation singletonAnn = clazz.getAnnotation(Singleton.class);
		if (singletonAnn != null) {
			this.actionObject = SingleBeanCache.get(clazz.getName());
			if (this.actionObject == null) {
				this.actionObject = clazz.newInstance();
				SingleBeanCache.add(clazz.getName(), this.actionObject);
			}
		} else
			this.actionObject = clazz.newInstance();

		ru = new ReflectUtil(this.actionObject);

		return this.actionObject;
	}

	// IOC，注入对象到pojo
	private void injectIocBean() throws Exception {
		fields = ru.getFields();
		if (fields == null)
			return;

		for (Field f : fields) {
			Class<?> type = f.getType();

			Ioc ioc = f.getAnnotation(Ioc.class);
			if (ioc == null)
				continue;
			String beanId = "";
			if (ioc.value().trim().length() == 0)
				beanId = type.getSimpleName();
			else
				beanId = CommonUtil.parsePropValue(ioc.value());

			Method setter = ru.getSetter(f.getName());
			if (setter == null)
				continue;

			setter.invoke(this.actionObject, IOC.getBean(beanId));
		}
	}

	private void exeActionLog() {
		StringBuilder sb = new StringBuilder();
		sb.append("execute action -> ").append(this.context.getUri() + " of " + this.context.getActionConfigBean()).append("; ");

		log.debug(sb.toString());
	}

	private Object[] assemParams(Class<?>[] paramTypes, Annotation[][] paramAnns)throws Exception {
		Object[] params = new Object[paramTypes.length];
		int pathParamIndex = 0;
		for (int i = 0; i < paramTypes.length; ++i) {
			Annotation[] anns = paramAnns[i];
			Class<?> paramClass = paramTypes[i];
			String[] paramValue = null;
			// ------------------------------------------------------
			// 通过给定class 获取对应的ActionContextObj
			if (Context.class.isAssignableFrom(paramClass)) {
				params[i] = this.context;
				continue;
			}

			if (HttpServletRequest.class.isAssignableFrom(paramClass)) {
				params[i] = this.context.getRequest();
				continue;
			}

			if (HttpServletResponse.class.isAssignableFrom(paramClass)) {
				params[i] = this.context.getResponse();
				continue;
			}

			if (PrintWriter.class.isAssignableFrom(paramClass)) {
				params[i] = this.context.getWriter();
				continue;
			}

			if (ServletOutputStream.class.isAssignableFrom(paramClass)) {
				params[i] = this.context.getOut();
				continue;
			}

			if (HttpSession.class.isAssignableFrom(paramClass)) {
				params[i] = this.context.getSession();
				continue;
			}

			if (ActionProp.class.isAssignableFrom(paramClass)) {
				if (this.context.getActionProp() == null)
					this.context.setActionProp(new ActionProp(this.actionObject.getClass().getName()));

				params[i] = this.context.getActionProp();
				continue;
			}

			if (Validation.class.isAssignableFrom(paramClass)) {
				params[i] = this.context.getValidation();
				continue;
			}

			if (QueryParams.class.isAssignableFrom(paramClass)) {
				params[i] = this.context.getQueryParams();
				continue;
			}

			if (DAO.class.isAssignableFrom(paramClass)) {
				params[i] = new DAOImpl("");
				continue;
			}

			if (InsertDAO.class.isAssignableFrom(paramClass)) {
				params[i] = DAOFactory.getInsertDAO();
				continue;
			}

			if (DeleteDAO.class.isAssignableFrom(paramClass)) {
				params[i] = DAOFactory.getDeleteDAO();
				continue;
			}

			if (UpdateDAO.class.isAssignableFrom(paramClass)) {
				params[i] = DAOFactory.getUpdateDAO();
				continue;
			}

			if (SelectDAO.class.isAssignableFrom(paramClass)) {
				params[i] = DAOFactory.getSelectDAO();
				continue;
			}

			if (DivPageDAO.class.isAssignableFrom(paramClass)) {
				params[i] = DAOFactory.getDivPageDAO();
				continue;
			}

			if (SearchDAO.class.isAssignableFrom(paramClass)) {
				params[i] = DAOFactory.getSearchDAO();
				continue;
			}

			if (CascadeDAO.class.isAssignableFrom(paramClass)) {
				params[i] = DAOFactory.getCascadeDAO();
				continue;
			}

//			PathParam pathParamAnn = this.getPathParamAnn(anns);
			Annotation pathParam = JAXWSUtil.getPathParam(anns);
            if (pathParam != null) {
                paramValue = this.getPathParamValue(PathUtil.getPathParamValue(pathParam));
                params[i] = ClassUtil.getParamVal(paramClass, paramValue[0]);
                continue;
            }

//			QueryParam queryParamAnn = this.getQueryParamAnn(anns);
            Annotation queryParamAnn = JAXWSUtil.getQueryParam(anns);
            if (queryParamAnn == null && Map.class.isAssignableFrom(paramClass)) {
                params[i] = this.context.getModel();
                continue;
            }

            //若参数有@QueryParam注解
            if (queryParamAnn != null) {
            	final String fieldName = QueryParamUtil.getQueryParamValue(queryParamAnn);
				if (File.class.isAssignableFrom(paramClass)) {
					if (!this.context.getUploadMap().containsKey(fieldName))
						continue;
					List<UploadFile> list = this.context.getUploadMap().get(fieldName);
					if (list == null || list.isEmpty())
						continue;
					
					UploadFile uploadFile = list.get(0);
					File file = uploadFile.getTmpFile();
					params[i] = file;
					
					continue;
				}
				
				if (File[].class.isAssignableFrom(paramClass)){
					if (!this.context.getUploadMap().containsKey(fieldName))
						continue;
					List<UploadFile> list = this.context.getUploadMap().get(fieldName);
					if (list == null || list.isEmpty())
						continue;
					File[] files = new File[list.size()];
					for (int j = 0; j < files.length; j++)
						files[j] = list.get(j).getTmpFile();
					
					params[i] = files;
				}
				
				if (UploadFile.class.isAssignableFrom(paramClass)) {
					if (!this.context.getUploadMap().containsKey(fieldName))
						continue;
					List<UploadFile> list = this.context.getUploadMap().get(fieldName);
					if (list == null || list.isEmpty())
						continue;
					
					UploadFile uploadFile = list.get(0);
					params[i] = uploadFile;
					
					continue;
				}
				
				if (UploadFile[].class.isAssignableFrom(paramClass)){
					if (!this.context.getUploadMap().containsKey(fieldName))
						continue;
					List<UploadFile> list = this.context.getUploadMap().get(fieldName);
					if (list == null || list.isEmpty())
						continue;
					
					params[i] = list.toArray(new UploadFile[]{});
				}
				
				// 根据参数名称获取http对应的参数值
				String defaultValue = null;
//				DefaultValue defaultValueAnn = this.getDefaultValueAnn(anns);
				Annotation defaultValueAnn = JAXWSUtil.getDefaultValue(anns);
				if (defaultValueAnn != null)
					defaultValue = QueryParamUtil.getDefaultValue(defaultValueAnn);
	
				paramValue = this.getQueryParamValue(fieldName, defaultValue);
				
				// 处理Date日期类型的参数
				if (java.util.Date.class.isAssignableFrom(paramClass)) {
					params[i] = this.getDateParam(anns, paramValue[0]);
					continue;
				}
	
				// 处理POJO类型的参数
				String startName = fieldName;
				if (ClassUtil.isPojo(paramClass)) {
					params[i] = this.injectParam2Pojo(paramClass, startName);
					continue;
				}
	
				// 处理Map类型的参数
				if (Map.class.isAssignableFrom(paramClass)) {
					params[i] = this.injectParam2Map(startName);
					continue;
				}
	
				//处理数组类型的参数
				if (paramClass.isArray())
					params[i] = ClassUtil.getParamVals(paramClass, paramValue);
				else
					params[i] = ClassUtil.getParamVal(paramClass, paramValue[0]);
            } else {
				// 如果是基本数据类型，则按照排序进行注入
            	String[] pathParams = this.context.getActionConfigBean().getPathParams();
            	if (pathParams == null){
            		log.warn("QueryParam not found and PathParam not found too");
            		continue;
            	}
            	
				paramValue = this.getPathParamValue(pathParams[pathParamIndex]);
				params[i] = ClassUtil.getParamVal(paramClass, paramValue[0]);
				pathParamIndex++;
				continue;
			}
		}

		return params;
	}

	private Method getFirstMethd(Method[] methods) {
		Method m = methods[0];
		if (methods.length == 1)
			return m;

		// 如果含有两个或以上同名的方法,优先拿到被@Path注解的第一个方法
		for (Method mm : methods) {
//			Path p = mm.getAnnotation(Path.class);
			boolean hasPath = JAXWSUtil.hasPath(mm);
			if (!hasPath)
				continue;

			m = mm;
			break;
		}

		return m;
	}

	private String[] getQueryParamValue(String paramName, String defaultValue) {
		String[] paramValue = this.context.getQueryParamMap().get(paramName);

		return getParamValue(paramValue, defaultValue);
	}

	private String[] getPathParamValue(String pathParamName) {
		String[] paramValue = this.context.getPathParamMap().get(pathParamName);

		return getParamValue(paramValue, null);
	}

	private String[] getParamValue(String[] paramValue, String defaultValue) {
		if (paramValue == null || paramValue.length == 0 || paramValue[0] == null) {
			if (defaultValue != null)
				paramValue = new String[] { defaultValue };
			else
				paramValue = new String[] { "" };
		}

		return paramValue;
	}

	private Date getDateParam(Annotation[] anns, String paramValue)
			throws Exception {
		DateFormat dateAnn = null;
		for (Annotation a : anns) {
			if (a == null)
				continue;

			if (!a.annotationType().isAssignableFrom(DateFormat.class))
				continue;

			dateAnn = (DateFormat) a;
			break;
		}

		String pattern = "yyyy-MM-dd HH:mm:ss";
		if (dateAnn != null)
			pattern = dateAnn.value();

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(paramValue);
	}

	private Object injectParam2Pojo(Class<?> paramClass, String startName)
			throws Exception {
		Object paramObj = paramClass.newInstance();
		ReflectUtil ru = new ReflectUtil(paramObj);
		this.injectActionCxt2Pojo(ru);
		// 注入mvc action 请求参数
		ParamUtil.injectParam(this.context, ru, startName);
		return paramObj;
	}

	private Object injectParam2Map(String startName) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		ReflectUtil ru = new ReflectUtil(map);
		this.injectActionCxt2Pojo(ru);
		// 注入mvc action 请求参数
		ParamUtil.injectParam(this.context, ru, startName);

		return map;
	}

//	private PathParam getPathParamAnn(Annotation[] anns) {
//		for (Annotation a : anns) {
//			if (a == null)
//				continue;
//
//			if (!a.annotationType().isAssignableFrom(PathParam.class))
//				continue;
//
//			return (PathParam) a;
//		}
//
//		return null;
//	}

//	private QueryParam getQueryParamAnn(Annotation[] anns) {
//		for (Annotation a : anns) {
//			if (a == null)
//				continue;
//
//			if (!a.annotationType().isAssignableFrom(QueryParam.class))
//				continue;
//
//			return (QueryParam) a;
//		}
//
//		return null;
//	}
//	
//	private DefaultValue getDefaultValueAnn(Annotation[] anns) {
//		for (Annotation a : anns) {
//			if (a == null)
//				continue;
//
//			if (!a.annotationType().isAssignableFrom(DefaultValue.class))
//				continue;
//
//			return (DefaultValue) a;
//		}
//
//		return null;
//	}

	private void handleDownload(File file) throws Exception{
		if (file.exists()){
			HttpServletResponse response = this.context.getResponse();
			String filename = URLEncoder.encode(file.getName(), "utf-8");
            response.reset();
            response.setContentType("application/x-msdownload");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            int fileLength = (int) file.length();
            response.setContentLength(fileLength);
            /*如果文件长度大于0*/
            if (fileLength != 0) {
                /*创建输入流*/
                InputStream inStream = new FileInputStream(file);
                byte[] buf = new byte[4096];
                /*创建输出流*/
                ServletOutputStream servletOS = response.getOutputStream();
                int readLength;
                while (((readLength = inStream.read(buf)) != -1)) {
                    servletOS.write(buf, 0, readLength);
                }
                inStream.close();
                servletOS.flush();
                servletOS.close();
            }
		}
	}
	
	private void handleResult() throws Exception {

		this.exeActionLog();
		if (retn == null)
			return;
		String baseUrl = (String) this.context.getServletContext().getAttribute(MVCConfigConstant.BASE_URL_KEY);
		if (File.class.isAssignableFrom(retn.getClass())){
			File file = (File)retn;
			this.handleDownload(file);
			return ;
		}else if(File[].class.isAssignableFrom(retn.getClass())){
			File[] files = (File[])retn;
			
			String fileName = CommonUtil.getNowTime("yyyyMMddHHmmss")+ "_" + "download.zip";
			
			HttpServletResponse resp = this.context.getResponse();
			resp.reset();
			resp.setContentType("application/zip");  
            resp.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	          
	        ServletOutputStream outputStream = resp.getOutputStream();  
	        ZipOutputStream zip = new ZipOutputStream(outputStream);  
	       
	        for (File file :files){
	        	byte[] b = new byte[1024];  
	 	        int len;  
	        	zip.putNextEntry(new ZipEntry(file.getName()));  
	        	FileInputStream fis = new FileInputStream(file);  
	 	        while ((len = fis.read(b)) != -1) {  
	 	            zip.write(b, 0, len);  
	 	        }  
	 	        
	 	        fis.close();
	        }
	          
	        zip.flush();  
	        zip.close();  
	        outputStream.flush();  
			
			return;
		}
		
		if (!String.class.isAssignableFrom(retn.getClass())) {
			String mimeType = null;
//			Produces prod = this.method.getAnnotation(Produces.class);
			boolean hasProduces = JAXWSUtil.hasProduces(method);
			if (hasProduces){
				String[] mimeTypes = ProducesUtil.getProducesValue(method);
				if (mimeTypes != null && mimeTypes.length > 0)
					mimeType = mimeTypes[0];
			}
			
			if (mimeType == null || mimeType.trim().length() == 0)
				mimeType = this.context.getRequest().getParameter(MVCConfigConstant.HTTP_HEADER_ACCEPT_PARAM);
			
			if (mimeType == null || mimeType.trim().length() == 0){
				String contentType = this.context.getRequest().getContentType();
				if (contentType != null){
					this.context.getResponse().setContentType(contentType);
					mimeType = contentType.split(";")[0];
				}
			}
			
			if (this.context.getWriter() == null)
				this.context.setWriter(this.context.getResponse().getWriter());
			
			if (MIMEType.JSON.equals(mimeType) || "json".equalsIgnoreCase(mimeType)) {
				this.context.getResponse().setContentType(MIMEType.JSON);
				this.context.getWriter().print(CommonUtil.toJson(retn));
			} else if (MIMEType.XML.equals(mimeType) || "xml".equalsIgnoreCase(mimeType)) {
				Class<?> cls = retn.getClass();
				if (Collection.class.isAssignableFrom(cls)) {
					Class<?> _cls = ClassUtil.getPojoClass(this.method);
					if (_cls != null)
						cls = _cls;
				}

				XMLWriter writer = BeanXMLUtil.getBeanXMLWriter(retn);
				writer.setCheckStatck(true);
				writer.setSubNameAuto(true);
				writer.setClass(cls);
				writer.setRootElementName(null);
				this.context.getResponse().setContentType(MIMEType.XML);
				this.context.getWriter().print(writer.toXml());
			} else {
				this.context.getWriter().print("暂时不支持JSON 、XML以外的表述形式");
			}
			
			this.context.getWriter().flush();

			return;
		}

		List<String> produces = this.context.getActionConfigBean().getProduces();
		if (produces != null && produces.size() > 0)
			for (String produce : produces) {
                this.context.getResponse().setContentType(produce);
				break;
			}

		String re = String.valueOf(retn);

		for (Field f : fields) {
			Method getter = ru.getGetter(f.getName());
			if (getter == null)
				continue;

			String name = f.getName();
			if (this.context.getModel().containsKey(name))
				continue;
			
			this.context.getModel().put(name, getter.invoke(actionObject));
		}
		
		this.context.getModel().put(MVCConfigConstant.BASE_URL_KEY, this.context.getServletContext().getAttribute(MVCConfigConstant.BASE_URL_KEY));
		this.context.getModel().put(MVCConfigConstant.APPLICATION_SCOPE_KEY, this.context.getServletContext());
		this.context.getModel().put(MVCConfigConstant.SESSION_SCOPE_KEY, this.context.getSession());
		
		this.context.getModel().put(MVCConfigConstant.REQ_PARAM_MAP_NAME, this.context.getQueryParamMap());
		
		// 客户端重定向
		if (re.startsWith(RenderType.REDIRECT + ":")) {
			String url = re.substring((RenderType.REDIRECT + ":").length());
			String location = url;

			this.context.getResponse().sendRedirect(CommonUtil.replaceChinese2Utf8(location));

			return;
		} else if (re.startsWith(RenderType.ACTION + ":")) {
			String path = re.substring((RenderType.ACTION + ":").length());
			// ACTION 重定向
			handleActionRedirect(context, path, baseUrl);

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
		} else {
			List<ResultConfigBean> results = this.context.getActionConfigBean().getResult();

			if (results == null || results.size() == 0) {
				this.context.getWriter().print(retn);
				this.context.getWriter().flush();
				return;
			}

			boolean isOut = true;
			for (ResultConfigBean r : results) {
				if (!"_props_".equals(r.getName()) && !r.getName().equals(re)
						&& !"".equals(re)){
					continue;
				}
				
				isOut = false;
				String type = r.getType();
				String location = r.getLocation();
				if (RenderType.REDIRECT.equalsIgnoreCase(type)) {
					this.context.getResponse().sendRedirect(CommonUtil.replaceChinese2Utf8(location));
					
					return ;
				} else if (RenderType.FORWARD.equalsIgnoreCase(type)) {
					HttpServletRequest request = this.context.getRequest();
					request.setAttribute(MVCConfigConstant.REQ_PARAM_MAP_NAME, this.context.getQueryParamMap());

					fields = ru.getFields();
					if (fields == null)
						return;

					for (Iterator<Entry<String, Object>> it = this.context.getModel().entrySet()
							.iterator(); it.hasNext();) {
						Entry<String, Object> entry = it.next();
						request.setAttribute(entry.getKey(), entry.getValue());
					}
					// 服务端跳转
					request.getRequestDispatcher(MVCConfigConstant.FORWARD_BASE_PATH + location).forward(request, this.context.getResponse());
					
					return ;
				} else if (RenderType.FREEMARKER.equalsIgnoreCase(type)) {
//					// FreeMarker 渲染
//					Configuration cfg = new Configuration();
//					// 指定模板从何处加载的数据源，这里设置成一个文件目录。
//					cfg.setDirectoryForTemplateLoading(new File(ConfigConstant.ROOT_PATH + MVCConfigConstant.FORWARD_BASE_PATH));
//					// 指定模板如何检索数据模型
//					cfg.setObjectWrapper(new DefaultObjectWrapper());
//					cfg.setDefaultEncoding("utf-8");
//
//					Template template = cfg.getTemplate(location);
//					template.setEncoding("utf-8");
//
//					template.process(this.context.getModel(), this.context.getWriter());
					
					return ;
				} else if (RenderType.ACTION.equalsIgnoreCase(type)) {
					// ACTION 重定向
					handleActionRedirect(context, location, baseUrl);

					return ;
				} else if (RenderType.OUT.equalsIgnoreCase(type)
						|| location.trim().length() == 0) {
					this.context.getWriter().print(location);
					this.context.getWriter().flush();
					
					return ;
				} 
			}
			
			if (isOut){
				this.context.getWriter().print(retn);
				this.context.getWriter().flush();
			}
		}

	}

	public static void handleActionRedirect(Context context, String path, String baseUrl)
			throws IOException {
		String method;
		String location;
		if (!path.contains("@")) {
			method = Http.Method.GET;
			location = path;
		} else {
			int lastIndex = path.indexOf("@") + 1;
			method = path.substring(lastIndex);
			location = path.substring(0, lastIndex - 1);
		}
		StringBuilder sb = new StringBuilder("");
		String param = null;
		if (path.contains("?")) {
			int lastIndex2 = path.indexOf("?") + 1;
			param = path.substring(lastIndex2);
			String[] params = param.split("&");
			for (String para : params) {
				String[] p = para.split("=");
				sb.append(String.format("<input name='%s' value='%s' />", p[0], p[1]));
			}

			method = method.substring(0, method.indexOf("?"));
		}

		if (Http.Method.GET.equalsIgnoreCase(method)) {
			String pa = param == null ? "" : "?" + CommonUtil.replaceChinese2Utf8(param);
			context.getResponse().sendRedirect(baseUrl + location + pa);
			return;
		}

		String _method = "";
		if (Http.Method.PUT.equalsIgnoreCase(method) || Http.Method.DELETE.equalsIgnoreCase(method)) {
			_method = new String(method);
			method = Http.Method.POST;
		}

		String action = baseUrl + location;
		// 构造一个Form表单模拟客户端发送新的Action请求
		String format = "<form id='ACTION_REDIRECT_FORM' action='%s' method='%s' ><input name='_method' value='%s' />%s<input type='submit' /></form>";
		String form = String.format(format, action, method, _method, sb.toString());
		String js = "<script>document.getElementById('ACTION_REDIRECT_FORM').submit();</script>";
		context.getWriter().print(form + js);
	}

	public void validateUpload() throws Exception{
		String tmpDir = null;
		long memoryMax = 0;
		long allSizeMax = 0;
		long oneSizeMax = 0;
		String[] suffixArray = null;
		Upload _upload = method.getAnnotation(Upload.class);
		if (_upload != null){
			if (_upload.tmpDir().trim().length() > 0)
				tmpDir = _upload.tmpDir();
			
			if (_upload.maxMemorySize().trim().length() > 0)
				memoryMax =  CommonUtil.strToInt(CommonUtil.parseFileSize(_upload.maxMemorySize())+"");
			
			if (_upload.maxRequestSize().trim().length() > 0)
				allSizeMax = CommonUtil.parseFileSize(_upload.maxRequestSize());
			
			if (_upload.maxFileSize().trim().length() > 0)
				oneSizeMax = CommonUtil.parseFileSize(_upload.maxFileSize());
			
			if (_upload.suffix().length > 0)
				suffixArray = _upload.suffix();
		}
		
		long countAllSize = 0;
		for (Iterator<Entry<String, List<UploadFile>>> it = this.context.getUploadMap().entrySet().iterator(); it.hasNext(); ){
			Entry<String, List<UploadFile>> e = it.next();
			String fieldName = e.getKey();
			List<UploadFile> files = e.getValue(); 
			for (UploadFile file : files) {
				String fileName = file.getFileName();
				String fileContentType = file.getContentType();	
				if (suffixArray != null && suffixArray.length > 0) {
					boolean isOk = false;
					for (String suffix : suffixArray){
						if (fileName.endsWith("."+suffix)){
							isOk = true;
							break;
						}
					}
					
					if (!isOk){
						String err = "your upload file "+fileName+" type invalid ! only allow " + Arrays.asList(suffixArray);
						Map<String,String> errMap = new HashMap<String,String>();
						errMap.put(fieldName, err);
						context.getValidation().getErrors().put("upload", errMap);
						return ;
					}
				}
				long fileSize = file.getSize();
				if (fileSize > oneSizeMax){
					Map<String,String> errMap = new HashMap<String,String>();
					String err = "your upload file "+fileName+" size overflow, only allow less than " + oneSizeMax;
					errMap.put(fieldName, err);
					context.getValidation().getErrors().put("upload", errMap);
					return ;
				}
				
				countAllSize += fileSize;
				if (countAllSize > allSizeMax){
					Map<String,String> errMap = new HashMap<String,String>();
					String err = "your upload files all size overflow, only allow less than " + allSizeMax;
					errMap.put(fieldName, err);
					context.getValidation().getErrors().put("upload", errMap);
					return ;
				}
			}
		}
	}
	
	/**
	 * 执行Action
	 * 
	 * @param methodName
	 * @throws Exception
	 */
	private void excuteMethod(String methodName) throws Exception{
		boolean isTrans = false;
		if (method.getAnnotation(Transactional.class) != null)
			isTrans = true;
		else if (actionObject.getClass().getAnnotation(Transactional.class) != null)
			isTrans = true;

		Class<?>[] paramTypes = method.getParameterTypes();

		// 无参数运行方法
		if (paramTypes == null || paramTypes.length == 0) {
			// 无参数运行方法
			if (isTrans)
				Transaction.execute(new Trans() {
					@Override
					public void run(Object... args) throws Exception {
						retn = method.invoke(actionObject);
					}
				}, "");
			else
				retn = method.invoke(actionObject);
		} else {
			
			Annotation[][] paramAnns = method.getParameterAnnotations();
			// 拿到方法所需要的参数
			final Object[] params = assemParams(paramTypes, paramAnns);

			// 带参数运行方法
			if (isTrans)
				Transaction.execute(new Trans() {
					@Override
					public void run(Object... args) throws Exception {
						retn = method.invoke(actionObject, params);
					}
				}, "");
			else
				retn = method.invoke(actionObject, params);
		}
	}

	private void injectActionCxt2Pojo(ReflectUtil ru) throws Exception {
		HttpServletRequest req = this.context.getRequest();
		HttpServletResponse res = this.context.getResponse();
		PrintWriter out = this.context.getWriter();
		HttpSession session = this.context.getSession();
		ActionProp actionProp = this.context.getActionProp();
		QueryParams queryParams = this.context.getQueryParams();
		for (String n : ru.getFieldsName()) {
			Method m = ru.getSetter(n);
			if (m == null)
				continue;

			Class<?> clazz = m.getParameterTypes()[0];
			if (Context.class.isAssignableFrom(clazz)) {
				m.invoke(ru.getObject(), this.context);
			} else if (HttpServletRequest.class.isAssignableFrom(clazz)) {
				m.invoke(ru.getObject(), req);
			} else if (HttpServletResponse.class.isAssignableFrom(clazz)) {
				m.invoke(ru.getObject(), res);
			} else if (PrintWriter.class.isAssignableFrom(clazz)) {
				m.invoke(ru.getObject(), out);
			} else if (ServletOutputStream.class.isAssignableFrom(clazz)) {
				m.invoke(ru.getObject(), this.context.getOut());
			} else if (HttpSession.class.isAssignableFrom(clazz)) {
				m.invoke(ru.getObject(), session);
			} else if (ActionProp.class.isAssignableFrom(clazz)) {
				if (actionProp == null)
					actionProp = new ActionProp(clazz.getName());

				this.context.setActionProp(actionProp);
				m.invoke(ru.getObject(), actionProp);
			} else if (QueryParams.class.isAssignableFrom(clazz)) {
				m.invoke(ru.getObject(), queryParams);
			} else if (Validation.class.isAssignableFrom(clazz)) {
				m.invoke(ru.getObject(), this.context.getValidation());
			} else {
				/* 如果找不到注入的类型，则尝试从IOC容器获取 */
				Object obj = IOC.getBean(n);
				if (obj != null)
					m.invoke(ru.getObject(), obj);
			}
		}
	}

	/**
	 * 执行Action
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {
		// 实例化pojo
		initPojo();

		// IOC注入对象到pojo中
		injectIocBean();
		
		String methodName = this.context.getActionConfigBean().getMethod();
		Method[] methods = ru.getMethods(methodName);
		if (methods == null || methods.length == 0)
			return;

		method = this.getFirstMethd(methods);
		if (method == null)
			return;
		
//		Consumes cons = method.getAnnotation(Consumes.class);
		boolean hasConsumes = JAXWSUtil.hasConsumes(method);
		if (hasConsumes) {
			String[] cts = ConsumesUtil.getConsumesValue(method);
			for (String ct : cts){
				if ("json".equals(ct) || MIMEType.JSON.equals(ct)){
					String[] jss = this.context.getQueryParamMap().get("_json_data_");
					if (jss != null) {
						for (String json : jss){
							Map<String, Object> map;
							try {
								map = CommonUtil.parse(json, Map.class);
							}catch (Exception e){
								e.printStackTrace();
								break;
							}
							for (Iterator<Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext(); ){
								Entry<String, Object> e = it.next();
								String key = e.getKey();
								String val = String.valueOf(e.getValue());
								//继续解析，如果解析成功说明是多级的json格式，最后将其平展开来
								try {
									Map<String, Object> subMap = CommonUtil.parse(val, Map.class);
									for (Iterator<Entry<String, Object>> subIt = subMap.entrySet().iterator(); subIt.hasNext(); ){
										Entry<String, Object> subE = subIt.next();
										String subKey = subE.getKey();
										String subVal = String.valueOf(subE.getValue());
										String newKey = key+"."+subKey;
										
										try {
											Map<String, Object> subSubMap = CommonUtil.parse(subVal, Map.class);
											for (Iterator<Entry<String, Object>> subSubIt = subSubMap.entrySet().iterator(); subSubIt.hasNext(); ){
												Entry<String, Object> subSubE = subSubIt.next();
												String subSubKey = subSubE.getKey();
												String subSubVal = String.valueOf(subSubE.getValue());
												String newSubKey = newKey+"."+subSubKey;
												
												assemJsonData(newSubKey, subSubVal);
											}
										}catch (Exception ex2){
											assemJsonData(newKey, subVal);
										}
										
									}
								}catch (Exception ex){
									assemJsonData(key, val);
								}
							}
						}
					}
				}
			}
		}
		
		// 注入框架mvc action 上下文环境
		this.injectActionCxt2Pojo(this.ru);
		
//		if (IAction.class.isAssignableFrom(this.actionObject.getClass())) {
//			// struts2风格
//			IAction action = (IAction) actionObject;
//			action.init(this.context);
//			retn = action.execute();
//			// 对Action执行返回结果的处理
//			this.handleResult();
//			
//			return ;
//		}
		
		// 执行验证器
		this.handleValidator();
		try{
			// upload validation
			this.validateUpload();
			
			// 注入mvc action 请求参数
			ParamUtil.injectParam(this.context, this.ru, null);
	
			/* 方法体内的前置拦截器执行  */
			Before before = method.getAnnotation(Before.class);
			if (before != null){
				InterExecution before_interExe = new InterExecution("before", context);
				before_interExe.execute(before.value());
				if (before_interExe.getError() != null){
					before_interExe.showErr();
					return ;
				}
			}
			
			// execute the action method
			excuteMethod(methodName);
			
			/* 方法体内的后置拦截器执行  */
			After after = method.getAnnotation(After.class);
			if (after != null){
				// 后置拦截器
				InterExecution after_interExe = new InterExecution("after", context);
				after_interExe.execute(after.value());
				if (after_interExe.getError() != null){
					after_interExe.showErr();
					return ;
				}
			}
			
			/* 外部配置的后置拦截器后执行 */
			InterExecution after_interExe = new InterExecution("after", this.context);// 7.后置拦截器
			if (after_interExe.findAndExecuteInter()) {
				after_interExe.showErr();
				return;
			}
			
			// 对Action执行返回结果的处理
			this.handleResult();
		}catch(Exception e){
			throw e;
		}
		
	}

	private void assemJsonData(String key, String val) {
		List<String> vals = new ArrayList<String>();
		String[] _vals = this.context.getQueryParamMap().get(key);
		if (_vals != null){
			vals.addAll(Arrays.asList(_vals));
		}
		vals.add(val);
		this.context.getQueryParamMap().put(key, vals.toArray(new String[]{}));
	}
}
