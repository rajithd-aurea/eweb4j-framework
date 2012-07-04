package org.eweb4j.ioc.config;

import java.util.ArrayList;
import java.util.List;

import org.eweb4j.ioc.config.bean.IOCConfigBean;
import org.eweb4j.ioc.config.bean.Injection;

/**
 * IOC组件存取配置信息的bean的创建
 * @author cfuture.aw
 * @since v1.a.0
 *
 */
public class IOCConfigBeanCreator {
	public static IOCConfigBean getIOCBean(){
		IOCConfigBean iocBean = null;
		iocBean = new IOCConfigBean();
		List<Injection> list = new ArrayList<Injection>();
		Injection depen = new Injection();
		list.add(depen);
		iocBean.setInject(list);
		return iocBean;
	}
}
