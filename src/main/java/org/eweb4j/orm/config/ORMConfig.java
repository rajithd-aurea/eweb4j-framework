package org.eweb4j.orm.config;

import java.io.File;
import java.util.List;

import org.eweb4j.cache.ORMConfigBeanCache;
import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.CheckConfigBean;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.orm.config.bean.ORMConfigBean;
import org.eweb4j.util.FileUtil;
import org.eweb4j.util.StringUtil;
import org.eweb4j.util.xml.BeanXMLUtil;
import org.eweb4j.util.xml.XMLReader;
import org.eweb4j.util.xml.XMLWriter;

/**
 * ORM configuration
 * 
 * @author weiwei
 * 
 */
public class ORMConfig {

	private static Log log = LogFactory.getORMLogger(ORMConfig.class);

	public synchronized static String check() {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigConstant.CONFIGBEAN_ID);
		if (cb == null)
			return null;

		List<String> ormXmlFilePaths = cb.getOrm().getOrmXmlFiles().getPath();
		for (String filePath : ormXmlFilePaths) {
			if (filePath == null || filePath.length() == 0)
				continue;

			File configFile = new File(ConfigConstant.CONFIG_BASE_PATH+filePath);
			try {
				XMLReader reader = BeanXMLUtil.getBeanXMLReader(configFile);
				reader.setBeanName("orm");
				reader.setClass("orm", ORMConfigBean.class);
				List<ORMConfigBean> ormList = reader.read();
				if (ormList == null || ormList.isEmpty()) {
					error = rebuildXmlFile(configFile, ConfigInfoCons.CANNOT_READ_ANY_CONFIG_INFO);
				} else {
					for (ORMConfigBean orm : ormList) {
						String error1 = CheckConfigBean.checkORM(orm, filePath);
						if (error1 != null)
							if (error == null)
								error = error1;
							else
								error += error1;

						String error2 = CheckConfigBean.checkORMProperty(orm.getProperty(), ormList, orm.getId(), filePath);
						if (error2 != null)
							if (error == null)
								error = error2;
							else
								error += error2;
					}
					
					if (error == null){
						for (ORMConfigBean orm : ormList) {
							if (!"".equals(orm.getClazz()))
								ORMConfigBeanCache.add(Class.forName(orm.getClazz()), orm);
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				error = rebuildXmlFile(configFile,StringUtil.getExceptionString(e));
			}
		}
		
		//error = Model2Table.write();
		
		if (error != null)
			ORMConfigBeanCache.clear();
		else 
			log.debug(ConfigInfoCons.READ_CONFIG_INFO_SUCCESS);
		
		return error;
	}

	private static String rebuildXmlFile(File configFile, String err) {
		String error;
		try {
			// 保存为备份文件
			File tf = new File(configFile.getAbsolutePath() + ".back"+ StringUtil.getNowTime("_MMddHHmmss"));
			FileUtil.copy(configFile, tf);
			log.debug("backup file ->" + tf.getAbsolutePath());
			XMLWriter writer = BeanXMLUtil.getBeanXMLWriter(configFile,ORMConfigBeanCreator.getORMBean());
			writer.setBeanName("orm");
			writer.setClass("orm", ORMConfigBean.class);
			writer.write();
			StringBuilder sb = new StringBuilder(ConfigInfoCons.REPAIR_INFO);
			sb.append(err);
			error = sb.toString();

			log.error(error);
		} catch (Exception e1) {
			e1.printStackTrace();
			StringBuilder sb3 = new StringBuilder(StringUtil.getExceptionString(e1));
			sb3.append(ConfigInfoCons.CANNOT_REPAIR_CONFIG_FILE);
			error = sb3.toString();
			log.error(error);
		}
		return error;
	}
}
