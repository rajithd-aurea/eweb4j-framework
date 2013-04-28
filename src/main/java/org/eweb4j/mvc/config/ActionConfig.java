package org.eweb4j.mvc.config;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.eweb4j.cache.ActionConfigBeanCache;
import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.CheckConfigBean;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.mvc.config.bean.ActionConfigBean;
import org.eweb4j.util.FileUtil;
import org.eweb4j.util.CommonUtil;
import org.eweb4j.util.xml.BeanXMLUtil;
import org.eweb4j.util.xml.XMLReader;
import org.eweb4j.util.xml.XMLWriter;

/**
 * Action类配置信息的读取、错误检查、自动修复
 * 
 * @author weiwei
 * 
 */
public class ActionConfig {

	private static Log log = LogFactory.getMVCLogger(ActionConfig.class);

	/** 涉及到文件IO，需要同步保证正确性 */
	public synchronized static String check() {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigBean.class.getName());

		if (cb == null)
			return null;

		// eweb4j-start-config.xml中<mvc>节点里的actionXmlFile
		List<String> xmlFilePaths = cb.getMvc().getActionXmlFiles().getPath();
		for (String filePath : xmlFilePaths) {
			if (filePath == null || filePath.length() == 0)
				continue;

			// 解决中文文件夹名
			File configFile = new File(ConfigConstant.CONFIG_BASE_PATH + filePath);
			try {
				XMLReader reader = BeanXMLUtil.getBeanXMLReader(configFile);
				reader.setBeanName("action");
				reader.setClass("action", ActionConfigBean.class);
				List<ActionConfigBean> mvcList = reader.read();

				if (mvcList == null || mvcList.isEmpty()) {
					error = rebuildXmlFile(configFile, ConfigErrCons.CANNOT_READ_CONFIG_INFO);
				} else {
					for (Iterator<ActionConfigBean> it = mvcList.iterator(); it.hasNext();) {
						ActionConfigBean mvc = it.next();

						// 检查MVC.Action配置是否有错误
						String error1 = CheckConfigBean.checkMVCAction(mvc,
								filePath);
						if (error1 != null)
							if (error != null)
								error += error1;
							else
								error = error1;

						// 检查MVC.Action中的Result部分配置是否有错误
						String error2 = CheckConfigBean.checkMVCResultPart(mvc.getResult(), mvc.getUriMapping(), filePath);
						if (error2 != null)
							if (error != null)
								error += error2;
							else
								error = error2;

						// 检查MVC.Action.Validator中的部分配置是否有错误
						String error4 = CheckConfigBean.checkMVCValidator(mvc.getValidator(), mvc.getUriMapping(),filePath);
						if (error4 != null)
							if (error != null)
								error += error4;
							else
								error = error4;
					}

					// 如果没有任何错误，将Action的配置信息放入缓存，供框架运行期使用
					if (error == null)
						for (Iterator<ActionConfigBean> it = mvcList.iterator(); it
								.hasNext();) {
							ActionConfigBean mvc = it.next();
							if (!"".equals(mvc.getClazz()))
								if (!"".equals(mvc.getUriMapping()))
									ActionConfigBeanCache.add(mvc.getUriMapping(), mvc);
						}
				}
			} catch (Exception e) {
				e.printStackTrace();
				error = rebuildXmlFile(configFile,CommonUtil.getExceptionString(e));
			}
		}

		// 如果有错误，清空缓存
		if (error != null)
			ActionConfigBeanCache.clear();

		return error;
	}

	private static String rebuildXmlFile(File configFile, String err) {
		String error;
		try {
			// 保存为备份文件
			File tf = new File(configFile.getAbsolutePath() + ".back" + CommonUtil.getNowTime("_MMddHHmmss"));
			FileUtil.copy(configFile, tf);
			log.debug("backup file->" + tf.getAbsolutePath());

			XMLWriter writer = BeanXMLUtil.getBeanXMLWriter(configFile, MVCConfigBeanCreator.getActionBean());
			writer.setBeanName("action");
			writer.setClass("action", ActionConfigBean.class);
			writer.write();

			StringBuilder tsb = new StringBuilder(ConfigErrCons.CONFIG_INFO_READ_ERROR);
			tsb.append(err);
			error = tsb.toString();
			// log-----
			log.error(error);
		} catch (Throwable e1) {
			StringBuilder sb2 = new StringBuilder(ConfigErrCons.CANNOT_REPAIR_CONFIG_FILE);
			error = sb2.toString();
			log.error(error, e1);
		}
		return error;
	}

	public static void setLAYOUT_SCREEN_CONTENT_KEY(String key){
		if (key == null || key.trim().length() == 0)
			return;
		
		MVCConfigConstant.LAYOUT_SCREEN_CONTENT_KEY = key;
	}
	
	public static void setBASE_URL_KEY(String key) {
		if (key == null || key.trim().length() == 0)
			return;

		MVCConfigConstant.BASE_URL_KEY = key;
	}

	public static void setREQ_PARAM_SCOPE_KEY(String key) {
		if (key == null || key.trim().length() == 0)
			return;

		MVCConfigConstant.REQ_PARAM_SCOPE_KEY = key;
	}

	public static void setFORWARD_BASE_PATH(String key) {
		if (key == null || key.trim().length() == 0)
			return;
		MVCConfigConstant.FORWARD_BASE_PATH = key;
	}
	
	public static void setHTTP_METHOD_PARAM(String key){
		if (key == null || key.trim().length() == 0)
			return;
		
		MVCConfigConstant.HTTP_METHOD_PARAM = key;
	}
	
	public static void setHTTP_HEADER_ACCEPT_PARAM(String key){
		if (key == null || key.trim().length() == 0)
			return;
		
		MVCConfigConstant.HTTP_HEADER_ACCEPT_PARAM = key;
	}
}
