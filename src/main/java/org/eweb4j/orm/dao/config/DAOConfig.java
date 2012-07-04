package org.eweb4j.orm.dao.config;

import java.io.File;

import javax.sql.DataSource;

import org.eweb4j.cache.DBInfoConfigBeanCache;
import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.CheckConfigBean;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.config.bean.DBInfoXmlFiles;
import org.eweb4j.orm.dao.config.bean.DBInfoConfigBean;
import org.eweb4j.orm.jdbc.DataSourceCreator;
import org.eweb4j.orm.jdbc.DataSourceWrap;
import org.eweb4j.orm.jdbc.DataSourceWrapCache;
import org.eweb4j.util.FileUtil;
import org.eweb4j.util.StringUtil;
import org.eweb4j.util.xml.BeanXMLUtil;
import org.eweb4j.util.xml.XMLReader;
import org.eweb4j.util.xml.XMLWriter;

public class DAOConfig {

	private static Log log = LogFactory.getORMLogger(DAOConfig.class);

	public synchronized static String check() {
		String error = null;
		ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigConstant.CONFIGBEAN_ID);
		if (cb == null)
			return null;

		DBInfoXmlFiles dbInfoXmlFiles = cb.getOrm().getDbInfoXmlFiles();
		if (dbInfoXmlFiles == null)
			return ConfigInfoCons.CANNOT_READ_CONFIG_FILE;

		for (String filePath : dbInfoXmlFiles.getPath()) {
			if (filePath == null || filePath.length() == 0)
				continue;

			File configFile = new File(ConfigConstant.CONFIG_BASE_PATH + filePath);
			try {
				XMLReader reader = BeanXMLUtil.getBeanXMLReader(configFile);
				reader.setBeanName("dataSource");
				reader.setClass("dataSource", DBInfoConfigBean.class);
				DBInfoConfigBean dcb = reader.readOne();
				if (dcb == null) {
					error = rebuildXmlFile(configFile,ConfigInfoCons.REPAIR_FILE_INFO);
				} else {
					String error1 = CheckConfigBean.checkORMDBInfo(dcb,filePath);
					if (error1 == null) {
						DBInfoConfigBeanCache.add(dcb.getDsName(), dcb);
						DataSource ds = DataSourceCreator.create(dcb);
						DataSourceWrap dsw = new DataSourceWrap(dcb.getDsName(), ds);

						String error2 = dsw.getConnection() == null ? ConfigInfoCons.CANNOT_GET_DB_CON : null;

						if (error2 != null)
							if (error == null)
								error = error2;
							else
								error += error2;

						else {
							String info = dcb.getDsName()+"."+ ConfigInfoCons.READ_CONFIG_INFO_SUCCESS;
							log.debug(info);
							// ------log-------
							// 将数据源放入缓存，它可是个重量级对象
							// 此步也是为了共存多个数据源
							DataSourceWrapCache.put(dcb.getDsName(), dsw);
						}
					} else if (error == null)
						error = error1;
					else
						error += error1;

				}
			} catch (Exception e) {
				e.printStackTrace();

				error = rebuildXmlFile(configFile, StringUtil.getExceptionString(e));
			}
		}

		if (error != null)
			DBInfoConfigBeanCache.clear();

		return error;
	}

	private static String rebuildXmlFile(File configFile, String err) {
		String error = null;
		try {
			// 保存为备份文件
			File tf = new File(configFile.getAbsolutePath() + ".back" + StringUtil.getNowTime("_MMddHHmmss"));
			FileUtil.copy(configFile, tf);
			log.debug("backup file->" + tf.getAbsolutePath());

			XMLWriter writer = BeanXMLUtil.getBeanXMLWriter(configFile, DAOConfigBeanCreator.getDAOBean());
			writer.setBeanName("dataSource");
			writer.setClass("dataSource", DBInfoConfigBean.class);
			writer.write();

			StringBuilder tsb = new StringBuilder(ConfigInfoCons.REPAIR_CONFIG_FILE);
			tsb.append(err);
			error = tsb.toString();

			log.error(error);
		} catch (Exception e1) {
			e1.printStackTrace();

			StringBuilder sb2 = new StringBuilder(ConfigInfoCons.CANNOT_REPAIR_FILE);
			sb2.append(StringUtil.getExceptionString(e1));
			error = sb2.toString();

			log.error(error);
		}
		return error;
	}
}
