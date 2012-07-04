package org.eweb4j.config;

import java.io.File;

import org.eweb4j.cache.ActionConfigBeanCache;
import org.eweb4j.cache.IOCConfigBeanCache;
import org.eweb4j.cache.ORMConfigBeanCache;
import org.eweb4j.cache.Props;
import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.ioc.config.IOCConfig;
import org.eweb4j.mvc.config.ActionAnnotationConfig;
import org.eweb4j.mvc.config.ActionConfig;
import org.eweb4j.mvc.config.InterceptorAnnotationConfig;
import org.eweb4j.mvc.config.InterceptorConfig;
import org.eweb4j.orm.config.ORMConfig;
import org.eweb4j.orm.config.PojoAnnotationConfig;
import org.eweb4j.orm.dao.config.DAOConfig;
import org.eweb4j.util.FileUtil;
import org.eweb4j.util.StringUtil;
import org.eweb4j.util.xml.BeanXMLUtil;
import org.eweb4j.util.xml.XMLReader;
import org.eweb4j.util.xml.XMLWriter;

/**
 * EWeb4J配置
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class EWeb4JConfig {
 
	private static Log log = LogFactory.getConfigLogger(EWeb4JConfig.class);

	public synchronized static String start() {
		return start(ConfigConstant.START_FILE_NAME);
	}

	public synchronized static String start(String fileName) {
		setSTART_FILE_NAME(fileName);

		return startByAbFile(ConfigConstant.START_FILE_PATH());
	}
	
	private synchronized static String startByAbFile(String aStartXmlPath) {
		String startXmlPath = aStartXmlPath;
		String error = null;
		File file = null; 
		boolean readXml = true;
		if (ConfigConstant.SUCCESS_START.equals(String.valueOf(SingleBeanCache.get(ConfigConstant.SUCCESS_START)))) {
			ConfigBean cb = (ConfigBean) SingleBeanCache.get(ConfigConstant.CONFIGBEAN_ID);

			String reload = (cb == null ? "true" : cb.getReload());
			if ("true".equals(reload) || "1".equals(reload)) {
				// 如果开了DEBUG,清空缓存，重新读取配置文件
				SingleBeanCache.clear();
				ORMConfigBeanCache.clear();
				IOCConfigBeanCache.clear();
				ActionConfigBeanCache.clear();
				log.debug("EWeb4J clear cache");
				readXml = true;
			} else {
				// 否则，不需要读取配置文件
				readXml = false;
			}
		}

		if (readXml) {
			// 1.读取配置文件
			try {
				file = new File(startXmlPath);
				XMLReader reader = BeanXMLUtil.getBeanXMLReader(file);
				reader.setBeanName("eweb4j");
				reader.setClass("eweb4j", ConfigBean.class);
				ConfigBean cb = reader.readOne();
				if (cb == null) {
					error = " can not read any configuration info! But now have bean repaired, please restart.";
				} else {

					StringBuilder infos = new StringBuilder("EWeb4JConfig.start \n");
					infos.append("start-config-xml-path --> ").append(ConfigConstant.START_FILE_PATH()).append("\n");
					infos.append("${RootPath} --> ").append(ConfigConstant.ROOT_PATH).append("\n");
					infos.append(cb).append("\n");

					log.debug(infos.toString());

					// 检查配置信息格式是否填写正确
					String error1 = CheckConfigBean.checkEWeb4JConfigBean(cb);
					if (error1 != null)
						error = error1;

					String error2 = CheckConfigBean.checkEWeb4JIOCPart(cb.getIoc());
					if (error2 != null)
						if (error == null)
							error = error2;
						else
							error += error2;

					String error3 = CheckConfigBean.checkIOCXml(cb.getIoc().getIocXmlFiles());
					if (error3 != null)
						if (error == null)
							error = error3;
						else
							error += error3;

					String error4 = CheckConfigBean.checkEWeb4JORMPart(cb.getOrm());
					if (error4 != null)
						if (error == null)
							error = error4;
						else
							error += error4;

					String error5 = CheckConfigBean.checkORMXml(cb.getOrm().getOrmXmlFiles());
					if (error5 != null)
						if (error == null)
							error = error5;
						else
							error += error5;

					String error6 = CheckConfigBean.checkEWeb4JMVCPart(cb.getMvc());
					if (error6 != null)
						if (error == null)
							error = error6;
						else
							error += error6;

					String error7 = CheckConfigBean.checkMVCActionXmlFile(cb.getMvc().getActionXmlFiles());
					if (error7 != null)
						if (error == null)
							error = error7;
						else
							error += error7;

					String error8 = CheckConfigBean.checkInter(cb.getMvc().getInterXmlFiles());
					if (error8 != null)
						if (error == null)
							error = error8;
						else
							error += error8;

					if (error == null) {
						// 验证通过，将读取到的信息放入缓存池中
						SingleBeanCache.add(ConfigConstant.CONFIGBEAN_ID, cb);
						SingleBeanCache.add(cb.getClass(), cb);
						// ------log-------
						String info = "EWeb4J start configuration info have bean validated and pushed to the cache. ";
						log.debug(info);
						// ------log-------
						// 继续验证其他组件配置信息

						// properties
						String error13 = null;
						try {
							for (org.eweb4j.config.bean.Prop f : cb.getProperties().getFile()) {
								error13 = Props.readProperties(f, true);
								if (error13 != null)
									if (error == null)
										error = error13;
									else
										error += error13;
							}
						} catch (Exception e) {
							log.warn(e.toString());
							if (error == null)
								error = e.toString();
							else
								error += e.toString();
						}

						if ("true".equals(cb.getIoc().getOpen()) || "1".equals(cb.getIoc().getOpen())) {
							String error10 = IOCConfig.check();
							if (error10 != null)
								if (error == null)
									error = error10;
								else
									error += error10;
						}
						if ("true".equals(cb.getOrm().getOpen()) || "1".equals(cb.getOrm().getOpen())) {
							String error14 = DAOConfig.check();
							if (error14 != null)
								if (error == null)
									error = error14;
								else
									error += error14;

							String error10 = new PojoAnnotationConfig().readAnnotation(cb.getOrm().getScanPojoPackage().getPath());
							if (error10 != null)
								if (error == null)
									error = error10;
								else
									error += error10;

							String error11 = ORMConfig.check();
							if (error11 != null)
								if (error == null)
									error = error11;
								else
									error += error11;

						}
						if ("true".equals(cb.getMvc().getOpen()) || "1".equals(cb.getMvc().getOpen())) {
							String error20 = new ActionAnnotationConfig().readAnnotation(cb.getMvc().getScanActionPackage().getPath());
							if (error20 != null)
								if (error == null)
									error = error20;
								else
									error += error20;

							String error11 = ActionConfig.check();
							if (error11 != null)
								if (error == null)
									error = error11;
								else
									error += error11;

							String error12 = new InterceptorAnnotationConfig().readAnnotation(cb.getMvc().getScanInterceptorPackage().getPath());
							if (error12 == null)
								error = error12;
							else
								error += error12;
							
							String error21 = InterceptorConfig.check();
							if (error21 != null)
								if (error == null)
									error = error21;
								else
									error += error21;

						}
					}
				}

			} catch (Exception e) {
				// 重写配置文件
				try {
					// 保存为备份文件
					FileUtil.copy(file, new File(startXmlPath + ".back" + "_"
							+ StringUtil.getNowTime("MMddHHmmss")));
					XMLWriter writer = BeanXMLUtil.getBeanXMLWriter(file,ConfigBeanCreator.getConfigBean());
					writer.setBeanName("eweb4j");
					writer.setClass("eweb4j", ConfigBean.class);
					writer.write();
					String info = "configuration error, now it has repaired.";
					error = StringUtil.getNowTime() + "EWeb4JConfig : " + info
							+ "exception：" + StringUtil.getExceptionString(e);

					log.error(info);

					e.printStackTrace();
				} catch (Exception e1) {
					String info = "can not write any configuration";
					error = StringUtil.getNowTime() + "EWeb4JConfig : " + info
							+ "exception：" + StringUtil.getExceptionString(e1);
					log.fatal(info);

					e1.printStackTrace();
				}
			}
			if (error != null) {
				SingleBeanCache.clear();
				ORMConfigBeanCache.clear();
				IOCConfigBeanCache.clear();
				ActionConfigBeanCache.clear();

				log.error(error);
			} else {
				SingleBeanCache.add(ConfigConstant.SUCCESS_START,
						ConfigConstant.SUCCESS_START);
			}
		}

		return error;
	}

	public static void createStartXml(String path, ConfigBean cb)
			throws Exception {
		XMLWriter writer = BeanXMLUtil.getBeanXMLWriter(new File(path), cb);
		writer.setBeanName("eweb4j");
		writer.setClass("eweb4j", ConfigBean.class);
		writer.write();
	}
	
	public static String about(){
		
		return "EWeb4J Framework 1.9-SNAPSHOT";
	}
	
	public static void setSTART_FILE_NAME(String START_FILE_NAME) {
		if (START_FILE_NAME == null || START_FILE_NAME.trim().length() == 0)
			return;
		ConfigConstant.START_FILE_NAME = START_FILE_NAME;
	}

	public static void setCONFIG_BASE_PATH(String CONFIG_BASE_PATH) {
		if (CONFIG_BASE_PATH == null || CONFIG_BASE_PATH.trim().length() == 0)
			return;
		ConfigConstant.CONFIG_BASE_PATH = CONFIG_BASE_PATH;
	}

}
