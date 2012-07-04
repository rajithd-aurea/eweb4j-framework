package org.eweb4j.config;

import java.util.ArrayList;

import org.eweb4j.config.bean.ActionXmlFile;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.config.bean.ConfigIOC;
import org.eweb4j.config.bean.ConfigMVC;
import org.eweb4j.config.bean.ConfigORM;
import org.eweb4j.config.bean.DBInfoXmlFiles;
import org.eweb4j.config.bean.I18N;
import org.eweb4j.config.bean.IOCXmlFiles;
import org.eweb4j.config.bean.InterXmlFile;
import org.eweb4j.config.bean.Locale;
import org.eweb4j.config.bean.ORMXmlFiles;
import org.eweb4j.config.bean.Prop;
import org.eweb4j.config.bean.Properties;
import org.eweb4j.config.bean.ScanActionPackage;
import org.eweb4j.config.bean.ScanPojoPackage;
import org.eweb4j.config.bean.UploadConfigBean;


/**
 * EWeb4J存取配置信息的bean的创建
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class ConfigBeanCreator {
	
	private ConfigBean configBean = new ConfigBean();
	private Properties props = new Properties();
	private ConfigIOC ioc = new ConfigIOC();
	private IOCXmlFiles iocXmlFiles = new IOCXmlFiles();
	
	public ConfigBeanCreator(){
		this.configBean.setProperties(props);
		this.configBean.setIoc(ioc);
		this.ioc.setIocXmlFiles(iocXmlFiles);
	}
	
	public ConfigBeanCreator createProp(String id, String path){

		Prop prop = new Prop();
		prop.setId(id);
		prop.setPath(path);
		props.getFile().add(prop);
		
		return this;
	}
	
	public ConfigBeanCreator createIoc(String path){
		ioc.getIocXmlFiles().getPath().add(path);
		
		return this;
	}
	
	public static ConfigBean getConfigBean() {
		ConfigBean configBean = new ConfigBean();
		
		I18N i18n = new I18N();
		Locale locale = new Locale();
		locale.setLanguage(java.util.Locale.CHINA.getLanguage());
		locale.setCountry(java.util.Locale.CHINA.getCountry());
		i18n.getLocale().add(locale);
		configBean.setLocales(i18n);
		
		Properties props = new Properties();
		Prop file = new Prop();
		props.getFile().add(file);
		configBean.setProperties(props);
		
		ConfigIOC ioc = new ConfigIOC();
		IOCXmlFiles iocXmlFiles = new IOCXmlFiles();
		iocXmlFiles.setPath(new ArrayList<String>());
		ioc.setIocXmlFiles(iocXmlFiles);
		configBean.setIoc(ioc);

		ConfigORM orm = new ConfigORM();
		ORMXmlFiles ormXmlFiles = new ORMXmlFiles();
		orm.setOrmXmlFiles(ormXmlFiles);
		ScanPojoPackage spp = new ScanPojoPackage();
		orm.setScanPojoPackage(spp);

		DBInfoXmlFiles dbInfoXmlFiles = new DBInfoXmlFiles();
		dbInfoXmlFiles.setPath(new ArrayList<String>());
		orm.setDbInfoXmlFiles(dbInfoXmlFiles);

		configBean.setOrm(orm);

		ConfigMVC mvc = new ConfigMVC();
		UploadConfigBean upload = new UploadConfigBean();
		mvc.setUpload(upload);
		
		ActionXmlFile actionFiles = new ActionXmlFile();
		actionFiles.setPath(new ArrayList<String>());
		mvc.setActionXmlFiles(actionFiles);
		
		InterXmlFile intFiles = new InterXmlFile();
		intFiles.setPath(new ArrayList<String>());
		mvc.setInterXmlFiles(intFiles);
		
		ScanActionPackage sap = new ScanActionPackage();
		sap.setPath(new ArrayList<String>());
		mvc.setScanActionPackage(sap);
		
		configBean.setMvc(mvc);

		return configBean;
	}
}
