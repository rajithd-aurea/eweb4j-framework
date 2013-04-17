package org.eweb4j.mvc.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eweb4j.cache.ActionConfigBeanCache;
import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.config.ScanPackage;
import org.eweb4j.mvc.ActionMethod;
import org.eweb4j.mvc.Http;
import org.eweb4j.mvc.action.RenderType;
import org.eweb4j.mvc.action.annotation.ActionLevel;
import org.eweb4j.mvc.action.annotation.Controller;
import org.eweb4j.mvc.action.annotation.Result;
import org.eweb4j.mvc.action.annotation.ShowValMess;
import org.eweb4j.mvc.action.annotation.Singleton;
import org.eweb4j.mvc.config.bean.ActionConfigBean;
import org.eweb4j.mvc.config.bean.ResultConfigBean;
import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.config.creator.ValidatorUtil;
import org.eweb4j.mvc.validator.annotation.Validate;
import org.eweb4j.util.CommonUtil;
import org.eweb4j.util.ReflectUtil;

public class ActionAnnotationConfig extends ScanPackage {

	public final static Log log = LogFactory.getMVCLogger(ActionAnnotationConfig.class);
	
	public ActionAnnotationConfig() {
		super();
	}

	/**
	 * handle action class
	 * 
	 * @param clsName
	 * @throws Exception
	 */
	public boolean handleClass(String clsName) {

		 //log.debug("handleClass -> " + clsName);

		Class<?> cls = null;
		try {
			cls = Thread.currentThread().getContextClassLoader().loadClass(clsName);

			if (cls == null)
				return false;

			String simpleName = cls.getSimpleName();
			Controller controlAnn = cls.getAnnotation(Controller.class);
			if (controlAnn == null && !simpleName.endsWith("Controller") && !simpleName.endsWith("Action") && !simpleName.endsWith("Control"))
				return false;

			String moduleName = CommonUtil.toLowCaseFirst(simpleName.replace("Controller", "").replace("Control", ""));
			if (simpleName.endsWith("Action")) 
				moduleName = "";
			
			if ("application".equals(moduleName))
				moduleName = "/";
			
//			boolean hasPath = JAXWSUtil.hasPath(cls);
//			if (hasPath){
//				moduleName = PathUtil.getPathValue(cls);
//			}
			Object obj = null;
			try {
				if (cls.getAnnotation(Singleton.class) != null) {
					obj = SingleBeanCache.get(cls.getName());
					if (obj == null) {
						obj = cls.newInstance();
						SingleBeanCache.add(cls.getName(), obj);
					}
				} else
					obj = cls.newInstance();

			} catch (Error er) {
				// er.printStackTrace();
				log.warn("the action class new instance failued -> " + clsName + " | " + er.toString());
				return false;
			} catch (Exception e) {
				// e.printStackTrace();
				log.warn("the action class new instance failued -> " + clsName + " | " + e.toString());
				return false;
			}

			ReflectUtil ru = new ReflectUtil(obj);
			Method[] ms = ru.getMethods();
			if (ms == null)
				return false;

			// 扫描方法的注解信息
			for (Method m : ms) {
				if (m.getModifiers() != 1)
					continue;
				boolean hasPath = JAXWSUtil.hasPath(m);
				//log.debug("scan method->"+m+" has path->"+hasPath);
				if (!hasPath) {
					String methodName = m.getName();
					Method getter = ru.getGetter(methodName.replace("get", ""));
					Method setter = ru.getSetter(methodName.replace("set", ""));
					// 默认下setter和getter不作为action方法
					if (getter != null || setter != null)
						continue;
				}

				handleActionConfigInfo(ru, cls, m, moduleName);
			}
		} catch (Error e) {
			return false;
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * 处理Action配置信息
	 * 
	 * @param ru
	 * @param controller
	 * @param method
	 * @param moduleName
	 */
	private void handleActionConfigInfo(ReflectUtil ru, Class<?> controller, Method method, String moduleName) {
		//log.debug("now parse the method...... moduleName->" + moduleName);
		// 这一步解析约定好的方法名doXxxAtXxx
		final ActionConfigBean action = parseUriMappingSuffix(moduleName, method);
		//log.debug("parse uri mapping by default rule -> " + action);
		if (action == null)
			return;

		action.setClazz(controller.getName());
		action.setMethod(method.getName());

		/* 解析出验证器错误信息输出类型 */
//		action.setShowValErrorType(parseShowValErrType(controller, method));

		/* 解析出Http Method */
		String httpMethod = parseHttpMethodByAnnotation(controller, method);
		// 如果方法上的注解不为空则覆盖约定好的
		if (httpMethod != null && httpMethod.trim().length() > 0)
			action.setHttpMethod(httpMethod);
		
		//log.debug("parse httpMethod -> " + httpMethod);
		
		/* 解析出最终的uriMapping */
		String uriMapping = parseUriMapping(controller, moduleName, action.getUriMapping());
		//log.debug("parse uriMapping -> " + uriMapping);
		if (uriMapping != null && uriMapping.trim().length() > 0)
			action.setUriMapping(uriMapping);

		// 解析@Result注解
		Result resultAnn = method.getAnnotation(Result.class);
		if (resultAnn != null)
			action.getResult().addAll(ResultAnnUtil.readResultAnn(resultAnn));

		/* 解析@ActionLevel */
		int level = parseActionLevel(controller, method);
		action.setLevel(String.valueOf(level));

		/* 解析@Produces */
		List<String> pcbs = parseProduces(method);
		if (pcbs != null)
			action.getProduces().addAll(pcbs);

		/* 解析@Validator和各种验证器 */
		List<ValidatorConfigBean> vals = parseValidators(ru, method);
		if (vals != null)
			action.getValidator().addAll(vals);

		/* 解析最终的actionKey (包括合并http method、正则等) */
		String actionConfigKey = parseFullUriMapping(controller, method, action.getHttpMethod(), action.getUriMapping());
		//log.debug("parse actionConfigKey -> " + actionConfigKey);
		if (actionConfigKey == null)
			return;

		// 将读取成功的配置信息放入缓存供框架运行期使用
		//log.debug("action cache add key -> "+actionConfigKey + ", action->"  + action);
		ActionConfigBeanCache.add(actionConfigKey, action);
		ActionClassCache.add(action.getClazz(), controller);
	}

	/**
	 * 解析 URI Mapping 后部分
	 * 
	 * @param moduleName
	 * @param m
	 * @return
	 */
	private ActionConfigBean parseUriMappingSuffix(String moduleName, Method m) {
		ActionConfigBean acb = new ActionConfigBean();
		String methodName = m.getName();
		String fullName = m.toString();
		boolean hasPath = JAXWSUtil.hasPath(m);
		//log.debug("has path of method "+m+" ->" + hasPath);
		String uriMapping = null;
		if (methodName.startsWith(ActionMethod.PREFIX)) {
			uriMapping = methodName.substring(ActionMethod.PREFIX.length());
			// doUriBindParam1AndParam2JoinUriAtPostOrGet
			String at = null;
			int indexOfAt = methodName.indexOf(ActionMethod.AT);
			if (indexOfAt != -1) {
				at = methodName.substring(indexOfAt + ActionMethod.AT.length());
				if (methodName.startsWith(ActionMethod.PREFIX))
					uriMapping = uriMapping.substring(0,
							uriMapping.indexOf(ActionMethod.AT));
				String[] httpMethods = at.split(ActionMethod.OR);
				StringBuilder sb = new StringBuilder();
				for (String httpMethod : httpMethods) {
					if (sb.length() > 0)
						sb.append("|");

					sb.append(httpMethod.toUpperCase());
				}
				if (sb.length() > 0) {
					acb.setHttpMethod(sb.toString());
				}
			}
			String join = "";
			String bind;
			int indexOfBind = methodName.indexOf(ActionMethod.BIND);
			if (indexOfBind != -1) {
				if (indexOfAt != -1 && indexOfAt > indexOfBind) {
					bind = methodName
							.substring(
									indexOfBind + ActionMethod.BIND.length(),
									indexOfAt);
				} else {
					bind = methodName.substring(indexOfBind
							+ ActionMethod.BIND.length());
				}

				uriMapping = uriMapping.substring(0,
						uriMapping.indexOf(ActionMethod.BIND));

				int indexOfJoin = bind.indexOf(ActionMethod.JOIN);
				if (indexOfJoin != -1) {
					String[] joins = bind.split(ActionMethod.JOIN);
					if (joins.length > 1) {
						bind = joins[0];
						join = joins[1];
					}
				}

				String[] pathParams = bind.split(ActionMethod.AND);
				StringBuilder pathParamSB = new StringBuilder();
				for (int i = 0; i < pathParams.length; i++) {
					pathParams[i] = CommonUtil.toLowCaseFirst(pathParams[i]);
					pathParamSB.append("/{").append(pathParams[i]).append("}");
				}

				if (pathParamSB.length() > 0)
					uriMapping = uriMapping + pathParamSB.toString();

				acb.setPathParams(pathParams);
			}

			uriMapping = CommonUtil.toLowCaseFirst(uriMapping);
			uriMapping = CommonUtil.hump2ohter(uriMapping, "-");

			if (join.length() > 0) {
				join = CommonUtil.toLowCaseFirst(join);
				join = CommonUtil.hump2ohter(join, "-");
				uriMapping = uriMapping + "/" + join;
			}

		} else if (!hasPath) {
			//log.debug("parse action by default rule......");
			/* 8 个默认方法 */
			ActionConfigBean defaultAcb = parseDefaultActionConfig(methodName, moduleName);
			//log.debug("parse action by default rule......--->"+defaultAcb);
			if (defaultAcb != null) {
				acb.setHttpMethod(defaultAcb.getHttpMethod());
				acb.getResult().addAll(defaultAcb.getResult());

				uriMapping = defaultAcb.getUriMapping();
			} else {
				String info = fullName + " does not starts with '" + ActionMethod.PREFIX + "' so that can not be a valid action uri mapping";
				//log.debug(info);
				return null;
			}
		}
		
		if (hasPath) {
			uriMapping = CommonUtil.parsePropValue(org.eweb4j.mvc.config.PathUtil.getPathValue(m));
		}
		
		//log.debug("parse uri mapping first -> " + uriMapping + ", hasPath->" + hasPath);
		
		acb.setUriMapping(uriMapping);
		return acb;
	}

	/**
	 * 解析默认的Action配置
	 * 
	 * @param methodReqMapVal
	 * @param moduleName
	 * @return
	 */
	private static ActionConfigBean parseDefaultActionConfig(String methodName, String modelName) {
		String moduleName = null;
		if (modelName.equals("/")){
			moduleName = "";
		}else{
			moduleName = modelName.endsWith("/") ? modelName.substring(0, modelName.lastIndexOf("/")) : modelName;
		}
		
		String uriMapping = null;
		String httpMethod = null;
		ActionConfigBean acb = new ActionConfigBean();

		if (ActionMethod.INDEX.equals(methodName)) {
			uriMapping = "/";
			httpMethod = Http.Method.GET;
			ResultConfigBean rcb = new ResultConfigBean();
			rcb.setName("jsp");
			rcb.setLocation(moduleName + "/view/index.jsp");
			acb.getResult().add(rcb);
			ResultConfigBean rcb2 = new ResultConfigBean();
			rcb2.setName("html");
			rcb2.setType(RenderType.FREEMARKER);
			rcb2.setLocation(moduleName + "/view/index.html");
			acb.getResult().add(rcb2);
		} else if (ActionMethod.CREATE.equals(methodName)) {
			uriMapping = "/";
			httpMethod = Http.Method.POST;

			ResultConfigBean rcb = new ResultConfigBean();
			rcb.setName(ActionMethod.INDEX);
			rcb.setLocation(moduleName);
			rcb.setType(RenderType.ACTION);
			acb.getResult().add(rcb);
		} else if (ActionMethod.UPDATE.equals(methodName)) {
			uriMapping = "/{id}";
			httpMethod = Http.Method.PUT;
			ResultConfigBean rcb = new ResultConfigBean();
			rcb.setName(ActionMethod.INDEX);
			rcb.setLocation(moduleName);
			rcb.setType(RenderType.ACTION);
			acb.getResult().add(rcb);
		} else if (ActionMethod.SHOW.equals(methodName)) {
			uriMapping = "/{id}";
			httpMethod = Http.Method.GET;

			ResultConfigBean rcb = new ResultConfigBean();
			rcb.setName("jsp");
			rcb.setLocation(moduleName + "/view/show.jsp");
			acb.getResult().add(rcb);
			ResultConfigBean rcb2 = new ResultConfigBean();
			rcb2.setName("html");
			rcb2.setType(RenderType.FREEMARKER);
			rcb2.setLocation(moduleName + "/view/show.html");
			acb.getResult().add(rcb2);
		} else if (ActionMethod.EDIT.equals(methodName)) {
			uriMapping = "/{id}/edit";
			httpMethod = Http.Method.GET;
			ResultConfigBean rcb = new ResultConfigBean();
			rcb.setName("jsp");
			rcb.setLocation(moduleName + "/view/edit.jsp");
			acb.getResult().add(rcb);
			ResultConfigBean rcb2 = new ResultConfigBean();
			rcb2.setName("html");
			rcb2.setType(RenderType.FREEMARKER);
			rcb2.setLocation(moduleName + "/view/edit.html");
			acb.getResult().add(rcb2);
		} else if (ActionMethod.DESTROY.equals(methodName)) {
			uriMapping = "/{id}";
			httpMethod = Http.Method.DELETE;

			ResultConfigBean rcb = new ResultConfigBean();
			rcb.setName(ActionMethod.INDEX);
			rcb.setLocation(moduleName);
			rcb.setType(RenderType.ACTION);
			acb.getResult().add(rcb);
		} else if (ActionMethod.NEW.equals(methodName)) {
			uriMapping = "/new";
			httpMethod = Http.Method.GET;
			ResultConfigBean rcb = new ResultConfigBean();
			rcb.setName("jsp");
			rcb.setLocation(moduleName + "/view/new.jsp");
			acb.getResult().add(rcb);
			ResultConfigBean rcb2 = new ResultConfigBean();
			rcb2.setName("html");
			rcb2.setType(RenderType.FREEMARKER);
			rcb2.setLocation(moduleName + "/view/new.html");
			acb.getResult().add(rcb2);
		} else {
			acb = null;
		}

		if (acb != null) {
			acb.setHttpMethod(httpMethod);
			acb.setUriMapping(uriMapping);
		}

		return acb;
	}

	private static String parseHttpMethodByClazz(Class<?> cls) {
		final String GET = Http.Method.GET;
		final String POST = Http.Method.POST;
		final String PUT = Http.Method.PUT;
		final String DELETE = Http.Method.DELETE;

		String[] methods = new String[4];
		methods[0] = JAXWSUtil.hasGET(cls) ? GET : "";
		methods[1] = JAXWSUtil.hasPOST(cls) ? POST : "";
		methods[2] = JAXWSUtil.hasDELETE(cls) ? DELETE : "";
		methods[3] = JAXWSUtil.hasPUT(cls) ? PUT : "";

		StringBuilder m_sb = new StringBuilder();
		for (String s : methods) {
			if (m_sb.length() > 0 && s.length() > 0)
				m_sb.append("|");

			m_sb.append(s);
		}

		if (m_sb.length() == 0)
			return null;

		return m_sb.toString();
	}

	private static String parseFullUriMapping(Class<?> cls, Method m,
			final String httpMethod, final String uriMapping) {
		// Action全名，框架用，包括对“{xxx}”url参数的正则化，HttpRequestMethod
		String actionFullName = ActionUrlUtil.mathersUrlMapping(m, uriMapping,
				cls);
		if (actionFullName == null)
			return null;

		if (actionFullName.endsWith("/")) {
			actionFullName.substring(0, actionFullName.length() - 1);
		}

		actionFullName = actionFullName + ActionMethod.CON + httpMethod;

		return actionFullName;
	}

	private static List<ValidatorConfigBean> parseValidators(ReflectUtil ru,
			Method m) {
		List<ValidatorConfigBean> vals = new ArrayList<ValidatorConfigBean>();

		// 读取@Validate注解，拿到需要被验证的参数名称
		Validate validate = m.getAnnotation(Validate.class);
		if (validate == null)
			return vals;

		String[] fields = validate.value();
		String[] excepts = validate.except();
		if (fields != null) {
			// 读取Action object 的属性 验证信息
			List<ValidatorConfigBean> fieldVal = ValidatorUtil.readValidator(fields, excepts, null, ru, null, null);
			if (fieldVal != null)
				vals.addAll(fieldVal);
		}

		return vals;
	}

	private static List<String> parseProduces(Method m) {
		// 读取@Produces注解
//		Produces producesAnn = m.getAnnotation(Produces.class);
		boolean hasProduces = JAXWSUtil.hasProduces(m);
		List<String> pcbs = null;
		if (hasProduces) {
			pcbs = new ArrayList<String>();
			String producesStr = CommonUtil.parsePropValue(ProducesUtil.getProducesValue(m)[0]);
			pcbs.add(producesStr);
		}

		return pcbs;
	}

	private static int parseActionLevel(Class<?> cls, Method m) {
		// 读取@ActionLevel注解
		ActionLevel actionLevel = cls.getAnnotation(ActionLevel.class);
		if (actionLevel == null)
			actionLevel = m.getAnnotation(ActionLevel.class);
		int level = 1;
		if (actionLevel != null)
			level = actionLevel.value();
		return level;
	}

	private static String parseShowValErrType(Class<?> cls, Method m) {
		ShowValMess cls_vm = cls.getAnnotation(ShowValMess.class);
		String clsShowValErr = cls_vm == null ? "alert" : cls_vm.value();

		clsShowValErr = CommonUtil.parsePropValue(clsShowValErr);

		String methodShowValErr = clsShowValErr.trim().length() == 0 ? "alert" : clsShowValErr;// 验证器验证信息输出方式默认”alert“

		ShowValMess m_vm = m.getAnnotation(ShowValMess.class);
		methodShowValErr = m_vm == null ? methodShowValErr : m_vm.value();

		return methodShowValErr;
	}

	private static String parseHttpMethodByAnnotation(Class<?> cls, Method m) {
		String methodHttpMethod = parseHttpMethodByMethodAnnotation(m);
		if (methodHttpMethod != null)
			return methodHttpMethod;

		return parseHttpMethodByClazz(cls);
	}

	private static String parseHttpMethodByMethodAnnotation(Method m) {
		String[] _methods = new String[4];
		_methods[0] = JAXWSUtil.hasGET(m) ? Http.Method.GET : "";
		_methods[1] = JAXWSUtil.hasPOST(m) ? Http.Method.POST : "";
		_methods[2] = JAXWSUtil.hasDELETE(m) ? Http.Method.DELETE : "";
		_methods[3] = JAXWSUtil.hasPUT(m) ? Http.Method.PUT : "";

		StringBuilder _sb = new StringBuilder();
		for (String s : _methods) {
			if (s.trim().length() == 0)
				continue;

			if (_sb.length() > 0 && s.length() > 0)
				_sb.append("|");

			_sb.append(s);
		}

		return _sb.toString();
	}
	
	/**
	 * 解析 Uri Mapping 的前部分
	 * 
	 * @param cls
	 * @param moduleName
	 * @return
	 */
	private static String parseUriMappingPrefix(Class<?> cls, String moduleName) {
		boolean hasPath = JAXWSUtil.hasPath(cls);
		//log.debug("has path of cls->" + cls + " is " + hasPath);
		String clazzUriMapping = hasPath ?  PathUtil.getPathValue(cls) : moduleName;
		//log.debug("cls uri->" + clazzUriMapping);
		clazzUriMapping = CommonUtil.parsePropValue(clazzUriMapping);

		return clazzUriMapping;
	}

	/**
	 * 解析
	 * 
	 * @param cls
	 * @param moduleName
	 * @param methodUriMapping
	 * @return
	 */
	private static String parseUriMapping(Class<?> cls, String moduleName, String uriMappingSuffix) {
		String uriPrefix = parseUriMappingPrefix(cls, moduleName);
		if (uriPrefix.length() > 0 && !uriMappingSuffix.startsWith("/"))
			uriPrefix = uriPrefix + "/";

		String uriMapping = uriPrefix + uriMappingSuffix;
		if (uriMapping.startsWith("/"))
			uriMapping = uriMapping.substring(1);
		if (uriMapping.endsWith("/"))
			uriMapping = uriMapping.substring(0, uriMapping.length() - 1);
		//log.debug("parse class uri mapping after resolve->"+uriMapping);
		return uriMapping;
	}

	@Override
	protected void onOk() throws Exception {
		// TODO Auto-generated method stub

	}

}
