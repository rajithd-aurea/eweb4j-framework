package org.eweb4j.mvc.config;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.eweb4j.cache.InterConfigBeanCache;
import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.CheckConfigBean;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.mvc.config.bean.InterConfigBean;
import org.eweb4j.util.FileUtil;
import org.eweb4j.util.StringUtil;
import org.eweb4j.util.xml.BeanXMLUtil;
import org.eweb4j.util.xml.XMLReader;
import org.eweb4j.util.xml.XMLWriter;

public class InterceptorConfig {

	private static Log log = LogFactory.getMVCLogger(InterceptorConfig.class);

	public synchronized static String check() {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigConstant.CONFIGBEAN_ID);

		if (cb != null) {
			for (String filePath : cb.getMvc().getInterXmlFiles().getPath()) {
				if (filePath != null && filePath.length() > 0) {
					File configFile = new File(ConfigConstant.CONFIG_BASE_PATH + filePath);
					try {
						XMLReader reader = BeanXMLUtil.getBeanXMLReader(configFile);
						reader.setBeanName("interceptor");
						reader.setClass("interceptor", InterConfigBean.class);
						List<InterConfigBean> interList = reader.read();

						if (interList == null || interList.isEmpty()) {
							error = rebuildXmlFile(configFile, ConfigErrCons.CANNOT_READ_CONFIG_INFO);
						} else {
							for (Iterator<InterConfigBean> it = interList.iterator(); it.hasNext();) {
								InterConfigBean inter = it.next();
								String error1 = CheckConfigBean.checkMVCInterceptor(inter, filePath);
								if (error1 != null) {
									if (error != null) {
										error += error1;
									} else {
										error = error1;
									}
								}

							}

							if (error == null) {
								for (Iterator<InterConfigBean> it = interList.iterator(); it.hasNext();) {
									InterConfigBean inter = it.next();
									if (!"".equals(inter.getClazz())) {
										InterConfigBeanCache.add(inter);
									}
								}

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						error = rebuildXmlFile(configFile,
								StringUtil.getExceptionString(e));
					}
				}
			}
			if (error != null) {
				InterConfigBeanCache.clear();
			}
		}
		return error;
	}

	private static String rebuildXmlFile(File configFile, String err) {
		String error;
		StringBuilder sb = new StringBuilder(err);
		try {
			// 保存为备份文件
			File tf = new File(configFile.getAbsolutePath() + ".back" + StringUtil.getNowTime("_MMddHHmmss"));
			FileUtil.copy(configFile, tf);
			log.debug("backup file->" + tf.getAbsolutePath());

			XMLWriter writer = BeanXMLUtil.getBeanXMLWriter(configFile,
					MVCConfigBeanCreator.getInterBean());
			writer.setBeanName("interceptor");
			writer.setClass("interceptor", InterConfigBean.class);
			writer.write();

			StringBuilder tsb = new StringBuilder(ConfigErrCons.CONFIG_INFO_READ_ERROR);
			tsb.append(sb.toString());
			log.error(tsb.toString());
			error = tsb.toString();
		} catch (Exception e1) {
			e1.printStackTrace();
			StringBuilder sb2 = new StringBuilder(ConfigErrCons.CANNOT_REPAIR_CONFIG_FILE + e1.toString());
			for (StackTraceElement ste : e1.getStackTrace()) {
				sb2.append("\n").append(ste.toString());
			}

			log.error(sb2.toString()); 
			error = sb2.toString();
		}
		return error;
	}
}
