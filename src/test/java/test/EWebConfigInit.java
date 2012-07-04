package test;

import org.eweb4j.config.ConfigBeanCreator;
import org.eweb4j.config.ConfigConstant;
import org.eweb4j.config.EWeb4JConfig;
import org.eweb4j.config.bean.ConfigBean;
import org.eweb4j.config.bean.I18N;
import org.eweb4j.config.bean.Locale;
import org.eweb4j.config.bean.LogConfigBean;
import org.eweb4j.config.bean.LogsConfigBean;
import org.eweb4j.config.bean.Prop;

public class EWebConfigInit {

        public static void main(String[] args) {
                
                String err = EWeb4JConfig.start();
                if (err != null)
                        throw new RuntimeException(err);
                
//              ConfigBean cb = ConfigBeanCreator.getConfigBean();
//
//              // 设置是否每次启动框架都重新读取配置文件
//              cb.setReload("false");
//
//              // -----------设置i18n国际化文件-----------------------------
//              I18N i18n = new I18N();
//              Locale locale = new Locale();
//              locale.setLanguage(java.util.Locale.CHINA.getLanguage());
//              locale.setCountry(java.util.Locale.CHINA.getCountry());
//              i18n.getLocale().add(locale);
//      //      cb.setI18n(i18n);
//              
//              // --------设置properties配置文件----------------------------
//              Prop prop = new Prop();
//              // 是否全局
//              prop.setGlobal("false");
//              // 文件ID
//              prop.setId("Message_zh_CN");
//              // 文件保存路径，相对路径，相对于${ConfigBasePath}
//              prop.setPath("Message_zh_CN.properties");
//              cb.getProperties().getFile().set(0, prop);
//              // -----------------------------------------------------------
//
//              // ---------- 日志 --------------------------------------------
//              LogConfigBean lcb = new LogConfigBean();
//              /* 控制台打印 */
//              lcb.setConsole("1");
//              /* 日志级别 */
//              lcb.setLevel("INFO");
//              /* 日志文件保存路径，相对于${ConfigBasePath} */
//              lcb.setFile("log.log");
//              /* 日志文件最大大小，单位MB */
//              lcb.setSize("5");
//              LogsConfigBean logs = new LogsConfigBean();
//              logs.getLog().add(lcb);
//              
//              // --------IOC相关的配置---------------------------------------
//              cb.getIoc().setOpen("true");
//              cb.getIoc().setLogs(logs);
//              // 添加一个hello-ioc.xml
//              cb.getIoc().getIocXmlFiles().getPath().set(0, "hello-ioc.xml");
//              // ------------------------------------------------------------
//
//              // -----------ORM相关的配置-------------------------------------
//              // ORM开关，开启了之后，框架在启动的时候会尝试去连接数据库
//              cb.getOrm().setOpen("true");
//              cb.getOrm().setLogs(logs);
//              // 设置数据源
//              cb.getOrm().setDataSource("com.mchange.v2.c3p0.ComboPooledDataSource");
//              cb.getOrm().getOrmXmlFiles().getPath().set(0, "demo-orm.xml");
//              // 设置扫描的包名，框架会扫描该包及其子包中所有的类，发现实体类后会加载配置信息，可以填写 "." 表示当前类路径
//              cb.getOrm().getScanPojoPackage().getPath().add("org.eweb4j");
//              // 设置数据库连接信息配置的文件
//              cb.getOrm().getDbInfoXmlFiles().getPath().set(0, "mysql.xml");
//              // ------------------------------------------------------
//
//              // ----------MVC相关的配置--------------------------------
//              // MVC开关，开启之后，要在WEB-INF/web.xml中配置EWebFilter或EWebServlet才能使用MVC的所有功能
//              cb.getMvc().setOpen("true");
//              cb.getMvc().setLogs(logs);
//              // 设置Action的配置文件，跟Struts类似
//              cb.getMvc().getActionXmlFiles().getPath().set(0, "hello-world-action.xml");
//              cb.getMvc().getScanActionPackage().getPath().set(0, "org.eweb4j");
//              // 设置Action的拦截器配置文件
//              cb.getMvc().getInterXmlFiles().getPath().set(0, "interceptor.xml");
//              // ------------------------------------------------------
//              
//              // 默认的start.xml文件名：eweb4j-start-config.xml
//              String path = ConfigConstant.START_FILE_PATH();
//              // 生成 start.xml
//              try {
//                      System.out.println(path);
//                      EWeb4JConfig.createStartXml(path, cb);
//              } catch (Exception e) {
//                      e.printStackTrace();
//              }
//
//              // 启动框架，让框架自动生成设置好的hello-ioc.xml
//              EWeb4JConfig.start();
        }

}