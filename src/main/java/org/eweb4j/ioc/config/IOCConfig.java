package org.eweb4j.ioc.config;

import java.io.File;
import java.util.List;

import org.eweb4j.cache.IOCConfigBeanCache;
import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.CheckConfigBean;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.ioc.config.bean.IOCConfigBean;
import org.eweb4j.util.FileUtil;
import org.eweb4j.util.StringUtil;
import org.eweb4j.util.xml.BeanXMLUtil;
import org.eweb4j.util.xml.XMLReader;
import org.eweb4j.util.xml.XMLWriter;

/**
 * IOC独立组件的启动配置
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class IOCConfig {

	private static Log log = LogFactory.getIOCLogger(IOCConfig.class);

	public synchronized static String check() {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigConstant.CONFIGBEAN_ID);
		if (cb == null)
			return null;

		List<String> iocXmlFilePaths = cb.getIoc().getIocXmlFiles().getPath();
		for (String filePath : iocXmlFilePaths) {
			if (filePath == null || filePath.length() == 0)
				continue;

			File configFile = new File(ConfigConstant.CONFIG_BASE_PATH+ filePath);
			try {
				XMLReader reader = BeanXMLUtil.getBeanXMLReader(configFile);
				reader.setBeanName("ioc");
				reader.setClass("ioc", IOCConfigBean.class);
				List<IOCConfigBean> iocList = reader.read();
				if (iocList == null || iocList.isEmpty()) {
					error = rebuildXmlFile(ConfigInfoCons.CANNOT_READ_CONFIG_FILE, configFile);
				} else {
					for (IOCConfigBean ioc : iocList) {
						String error1 = CheckConfigBean.checkIOC(ioc, filePath);
						if (error1 != null)
							if (error == null)
								error = error1;
							else
								error += error1;

						String error2 = CheckConfigBean.checkIOCJnject(ioc.getInject(), iocList, ioc.getId(), filePath);
						if (error2 != null)
							if (error == null)
								error = error2;
							else
								error += error2;

					}

					if (error == null) {
						for (IOCConfigBean ioc : iocList)
							if (!"".equals(ioc.getClazz())) {
								String clazz = ioc.getClazz();
								IOCConfigBeanCache.add(Class.forName(clazz), ioc);
								if (!"".equals(ioc.getId()))
									IOCConfigBeanCache.add(ioc.getId(), ioc);

							}

						log.debug(ConfigInfoCons.READ_CONFIG_FILE_SUCCESS);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				error = rebuildXmlFile(error + "|" + StringUtil.getExceptionString(e), configFile);
			}
		}

		if (error != null)
			IOCConfigBeanCache.clear();

		return error;
	}

	private static String rebuildXmlFile(String error, File configFile) {
		try {
			// 保存为备份文件
			File tf = new File(configFile.getAbsolutePath() + ".back" + StringUtil.getNowTime("_MMddHHmmss"));
			FileUtil.copy(configFile, tf);
			log.debug("backup file ->" + tf.getAbsolutePath());

			XMLWriter writer = BeanXMLUtil.getBeanXMLWriter(configFile, IOCConfigBeanCreator.getIOCBean());
			writer.setBeanName("ioc");
			writer.setClass("ioc", IOCConfigBean.class);
			writer.write();

			StringBuilder sb = new StringBuilder(ConfigInfoCons.REPAIR_FILE);

			sb.append(error);

			log.error(sb.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
			StringBuilder sb2 = new StringBuilder( ConfigInfoCons.CANNOT_REPAIR_FILE);

			sb2.append(StringUtil.getExceptionString(e1));
			error = sb2.toString();

			log.error(error);
		}

		return error;
	}
}
