package org.eweb4j.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.mvc.ActionMethod;
import org.eweb4j.mvc.UrlParamHandler;
import org.eweb4j.mvc.config.bean.ActionConfigBean;
import org.eweb4j.util.RegexList;

/**
 * MVC组件缓存
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public final class ActionConfigBeanCache {

	private static Log log = LogFactory
			.getConfigLogger(ActionConfigBeanCache.class);

	/**
	 * This is a map for save the action_config_bean
	 */
	private static final HashMap<Object, ActionConfigBean> ACTION_CFG_BEAN = new HashMap<Object, ActionConfigBean>();

	/**
	 * as it is a utils class, it could not has a public constructor
	 */
	private ActionConfigBeanCache() {
	}

	public static boolean containsKey(String beanID) {
		return ACTION_CFG_BEAN.containsKey(beanID);
	}

	public static Map<String, List<?>> getByMatches(final String aUri,
			final String reqMethod) {
		String uri = new String(aUri);
		Map<String, List<?>> result = null;
		for (Iterator<Entry<Object, ActionConfigBean>> it = ACTION_CFG_BEAN
				.entrySet().iterator(); it.hasNext();) {

			Entry<Object, ActionConfigBean> entry = it.next();
			Object beanID = entry.getKey();
			ActionConfigBean mvcBean = entry.getValue();
			if (!String.class.isAssignableFrom(beanID.getClass()))
				continue;

			// 如果是String
			String regex = String.valueOf(beanID).replace(
					ActionMethod.CON + mvcBean.getHttpMethod(), "");
			if (regex.contains("{") || regex.contains("}"))
				continue;

			if (aUri.endsWith("/"))
				uri = aUri.substring(0, aUri.length() - 1);

			if (regex.endsWith("/"))
				regex = regex.substring(0, regex.length() - 1);

			String[] methods = mvcBean.getHttpMethod().split("\\|");
			boolean checkMethod = false;
			for (String m : methods) {
				if (m.trim().equalsIgnoreCase(reqMethod)) {
					checkMethod = true;
					break;
				}
			}
			if (regex != null && checkMethod && uri.matches(regex)) {
				result = new HashMap<String, List<?>>();
				// 1.hello/{name}/test/{id}
				// 2.{"hello/","{name}","/test/{id}"}
				String urlMapping = mvcBean.getUriMapping();
				// 如果urlMapping的开头是“/”要去掉
				if (urlMapping.startsWith("/"))
					urlMapping = urlMapping.substring(1);

				String pattern = RegexList.path_var_regexp;
				Pattern p = Pattern.compile(pattern);
				Matcher m = p.matcher(urlMapping);

				List<String> urlParamNames = new ArrayList<String>();
				List<String> urlParamValues = new ArrayList<String>();
				while (m.find()) {
					String g = m.group();
					// System.out.println("g->" + g);
					String[] regexSplit = urlMapping.split(pattern);
					String paramVal = UrlParamHandler.matchersUrlParam(uri,
							regexSplit);

					// System.out.println("pV->" + paramVal);
					if (paramVal != null) {
						urlParamValues.add(paramVal);
						urlParamNames.add(g);
					}
				}

				if (urlParamNames.size() > 0 && urlParamValues.size() > 0) {
					result.put("urlParamNames", urlParamNames);
					result.put("urlParamValues", urlParamValues);
				}

				List<ActionConfigBean> mvcBeanList = new ArrayList<ActionConfigBean>();
				mvcBeanList.add(mvcBean);
				result.put("mvcBean", mvcBeanList);

				break;
			}

		}

		return result;
	}

	public static boolean containsKey(Class<?> clazz) {
		return ACTION_CFG_BEAN.containsKey(clazz);
	}

	public static void add(String beanID, ActionConfigBean o) {
		if (beanID != null && o != null) {
			String info = null;

			if (!ACTION_CFG_BEAN.containsKey(beanID)) {
				ACTION_CFG_BEAN.put(beanID, o);
				info = "add...finished..." + beanID + "|" + o;
			} else {
				ActionConfigBean actionBean = ACTION_CFG_BEAN.get(beanID);
				if (actionBean != null) {
					String level1 = actionBean.getLevel();
					String level2 = o.getLevel();
					if (level1 == null || level1.trim().length() == 0)
						level1 = "1";

					if (level2 == null || level2.trim().length() == 0)
						level2 = "1";

					int level_1 = 1;
					int level_2 = 1;

					if (level1.matches(RegexList.integer_regexp))
						level_1 = Integer.parseInt(level1);

					if (level2.matches(RegexList.integer_regexp))
						level_2 = Integer.parseInt(level2);

					if (level_2 > level_1) {
						ACTION_CFG_BEAN.remove(beanID);
						ACTION_CFG_BEAN.put(beanID, o);
						info = " " + actionBean.getClazz() + "#"
								+ actionBean.getMethod() + "#uri-mapping:"
								+ beanID + "  level[" + level_1
								+ "]is lower than" + o.getClazz() + "."
								+ o.getMethod() + "#uri-mapping:" + beanID
								+ "level[" + level_2 + "]，so replaced。";
					} else {
						info = "add...fail repeat action..." + beanID;
					}
				} else {
					ACTION_CFG_BEAN.put(beanID, o);
					info = "add...finished..." + beanID + "|" + o;
				}
			}

			log.debug(info);
		}
	}

	public static void add(Class<?> clazz, ActionConfigBean o) {
		if (clazz != null && o != null) {
			String info = null;
			if (!ACTION_CFG_BEAN.containsKey(clazz)) {
				ACTION_CFG_BEAN.put(clazz, o);
				info = "add...finished..." + o;
			} else {
				ActionConfigBean actionBean = ACTION_CFG_BEAN.get(clazz);
				if (actionBean != null) {
					String level1 = actionBean.getLevel();
					String level2 = o.getLevel();
					if (level1 == null || level1.trim().length() == 0)
						level1 = "1";

					if (level2 == null || level2.trim().length() == 0)
						level2 = "1";

					int level_1 = 1;
					int level_2 = 1;

					if (level1.matches(RegexList.integer_regexp))
						level_1 = Integer.parseInt(level1);

					if (level2.matches(RegexList.integer_regexp))
						level_2 = Integer.parseInt(level1);

					if (level_2 > level_1) {
						ACTION_CFG_BEAN.remove(clazz);
						ACTION_CFG_BEAN.put(clazz, o);
					} else {
						info = "add...fail repeat action..." + clazz;
					}

				} else {
					ACTION_CFG_BEAN.put(clazz, o);
					info = "add...finished..." + o;
				}
			}

			log.debug(info);
		}
	}

	public static ActionConfigBean get(String beanID) {
		ActionConfigBean o = null;
		if (beanID != null)
			if (ACTION_CFG_BEAN.containsKey(beanID))
				o = ACTION_CFG_BEAN.get(beanID);

		return o;
	}

	public static ActionConfigBean get(Class<?> clazz) {
		ActionConfigBean o = null;
		if (clazz != null)
			if (ACTION_CFG_BEAN.containsKey(clazz))
				o = ACTION_CFG_BEAN.get(clazz);

		return o;
	}

	public static void clear() {
		if (!ACTION_CFG_BEAN.isEmpty())
			ACTION_CFG_BEAN.clear();

	}

	public static HashMap<Object, ActionConfigBean> getAll() {
		return ACTION_CFG_BEAN;
	}
}
