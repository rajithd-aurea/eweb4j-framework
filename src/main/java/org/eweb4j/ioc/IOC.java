package org.eweb4j.ioc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eweb4j.cache.IOCConfigBeanCache;
import org.eweb4j.cache.SingleBeanCache;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.ioc.config.IOCConfigConstant;
import org.eweb4j.ioc.config.bean.IOCConfigBean;
import org.eweb4j.ioc.config.bean.Injection;
import org.eweb4j.util.ReflectUtil;
import org.eweb4j.util.StringUtil;

/**
 * IOC Bean工厂，负责生产出各种各样的bean 按照配置文件配置信息进行bean的生产 服从依赖注入
 * <b>目前支持的功能非常单薄，仅能注入基本类型和自定义类类型，集合类暂时不支持</b>
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class IOC {
        private static Log log = LogFactory.getConfigLogger(IOC.class);

        /**
         * 查找beanID的bean是否存在
         * 
         * @param beanID
         * @return
         */
        public static boolean containsBean(String beanID) {
                return IOCConfigBeanCache.containsKey(beanID);
        }

        /**
         * 查找clazz类型的bean是否存在
         * 
         * @param clazz
         * @return
         */
        public static boolean containsBean(Class<?> clazz) {
                return IOCConfigBeanCache.containsKey(clazz);
        }

        /**
         * 生产出指定Class类型的bean
         * 
         * @param <T>
         * @param requiredType
         * @return
         * @throws Exception
         */
        public <T> T getBean(Class<T> requiredType) throws Exception {
                return (T) getBean(IOCConfigBeanCache.get(requiredType).getId());
        }

        /**
         * 查看beanID的bean是什么类型
         * 
         * @param beanID
         * @return
         * @throws Exception
         */
        public Class<?> getType(String beanID) throws Exception {
                return IOCConfigBeanCache.get(beanID).getClass();
        }

        /**
         * 查看beanID的bean生命周期是否是原型
         * 
         * @param beanID
         * @return
         * @throws Exception
         */
        public boolean isPrototype(String beanID) throws Exception {
                return IOCConfigConstant.PROTOTYPE_SCOPE.equals(IOCConfigBeanCache.get(
                                beanID).getScope());
        }

        /**
         * 查看beanID的生命周期是否是单件
         * 
         * @param beanID
         * @return
         * @throws Exception
         */
        public boolean isSingleton(String beanID) throws Exception {
                return IOCConfigConstant.SINGLETON_SCOPE.equals(IOCConfigBeanCache.get(
                                beanID).getScope());
        }

        /**
         * 查看beanID的bean生命周期是否是原型
         * 
         * @param clazz
         * @return
         * @throws Exception
         */
        public boolean isPrototype(Class<?> clazz) throws Exception {
                return IOCConfigConstant.PROTOTYPE_SCOPE.equals(IOCConfigBeanCache.get(
                                clazz).getScope());
        }

        /**
         * 查看beanID的生命周期是否是单件
         * 
         * @param clazz
         * @return
         * @throws Exception
         */
        public boolean isSingleton(Class<?> clazz) throws Exception {
                return IOCConfigConstant.SINGLETON_SCOPE.equals(IOCConfigBeanCache.get(
                                clazz).getScope());
        }

        /**
         * 查看beanID和targetType是否相符
         * 
         * @param beanID
         * @param targetType
         * @return
         * @throws Exception
         */
        public boolean isTypeMatch(String beanID, Class<?> targetType)
                        throws Exception {
                return targetType.equals(IOCConfigBeanCache.get(beanID).getClass());
        }

        /**
         * 生产出符合beanID名字的bean
         * 
         * @param <T>
         * @param beanID
         * @return
         * @throws Exception
         */
        public static <T> T getBean(String beanID) {
                if (!containsBean(beanID)) {
                        return null;
                }
                // 声明用来返回的对象
                T t = null;
                try {
                        // 声明构造方法参数列表的初始化值
                        Object[] initargs = null;
                        // 声明构造方法参数列表
                        Class<?>[] args = null;
                        List<Object> initargList = new ArrayList<Object>();
                        List<Class<?>> argList = new ArrayList<Class<?>>();
                        // 遍历配置文件，找出beanID的bean
                        if (IOCConfigBeanCache.containsKey(beanID)) {
                                IOCConfigBean iocBean = IOCConfigBeanCache.get(beanID);
                                // 取出该bean的类型，便于最后使用反射调用构造方法实例化
                                Class<T> clazz = (Class<T>) Class.forName(iocBean.getClazz());
                                // 判断该bean的生命周期
                                if (IOCConfigConstant.SINGLETON_SCOPE.equalsIgnoreCase(iocBean
                                                .getScope())) {
                                        // 如果是单件，就从单件缓存池中取
                                        if (SingleBeanCache.containsKey(beanID)) {
                                                t = (T) SingleBeanCache.get(beanID);
                                                return t;
                                        }
                                }
                                // 如果单件缓存池中没有,就创建新的对象
                                t = clazz.newInstance();
                                // 添加到单件缓存池中 t这个引用会在下面重新实例化
                                SingleBeanCache.add(beanID, t);

                                // 遍历每个bean的注入配置
                                for (Iterator<Injection> it = iocBean.getInject().iterator(); it
                                                .hasNext();) {
                                        Injection inj = it.next();
                                        String ref = inj.getRef();
                                        if (ref != null && !"".equals(ref)) {
                                                // 如果ref不为空，说明注入的是对象类型，后面需要进入递归
                                                String name = inj.getName();
                                                if (name != null && !"".equals(name)) {
                                                        // 如果属性名字不为空，说明使用的是setter注入方式
                                                        // 使用setter注入的时候，需要提供一个无参构造方法
                                                        if (t == null)
                                                                t = clazz.newInstance();

                                                        ReflectUtil ru = new ReflectUtil(t);
                                                        Method m = ru.getSetter(name);
                                                        if (m != null)
                                                                m.invoke(t, getBean(ref));

                                                } else {
                                                        // 如果属性名字为空，说明使用的是构造器注入方式
                                                        // 使用构造器注入的时候，需要按照构造器参数列表顺序实例化
                                                        t = (T) getBean(ref);
                                                        argList.add(t.getClass());
                                                        initargList.add(t);
                                                }
                                        } else {
                                                // 注入基本类型
                                                String type = inj.getType();
                                                String value = inj.getValue();
                                                if (value == null) {
                                                        value = "";
                                                }
                                                String name = inj.getName();
                                                if (name != null && !"".equals(name)) {
                                                        // 如果属性名字不为空，说明使用的是setter注入方式
                                                        // 使用setter注入的时候，需要提供一个无参构造方法
                                                        if (t == null)
                                                                t = clazz.newInstance();

                                                        ReflectUtil ru = new ReflectUtil(t);
                                                        Method m = ru.getMethod("set"
                                                                        + StringUtil.toUpCaseFirst(name));
                                                        if (m != null) {
                                                                if (IOCConfigConstant.INT_ARGTYPE
                                                                                .equalsIgnoreCase(type)
                                                                                || "java.lang.Integer"
                                                                                                .equalsIgnoreCase(type)) {
                                                                        if ("".equals(value.trim()))
                                                                                value = "0";

                                                                        // int
                                                                        m.invoke(t, Integer.parseInt(value));
                                                                } else if (IOCConfigConstant.STRING_ARGTYPE
                                                                                .equalsIgnoreCase(type)
                                                                                || "java.lang.String"
                                                                                                .equalsIgnoreCase(type)) {
                                                                        // String
                                                                        m.invoke(t, value);
                                                                } else if (IOCConfigConstant.LONG_ARGTYPE
                                                                                .equalsIgnoreCase(type)
                                                                                || "java.lang.Long"
                                                                                                .equalsIgnoreCase(type)) {
                                                                        // long
                                                                        if ("".equals(value.trim()))
                                                                                value = "0";

                                                                        m.invoke(t, Long.parseLong(value));
                                                                } else if (IOCConfigConstant.FLOAT_ARGTYPE
                                                                                .equalsIgnoreCase(type)
                                                                                || "java.lang.Float"
                                                                                                .equalsIgnoreCase(type)) {
                                                                        // float
                                                                        if ("".equals(value.trim()))
                                                                                value = "0.0";

                                                                        m.invoke(t, Float.parseFloat(value));
                                                                } else if (IOCConfigConstant.BOOLEAN_ARGTYPE
                                                                                .equalsIgnoreCase(type)
                                                                                || "java.lang.Boolean"
                                                                                                .equalsIgnoreCase(type)) {
                                                                        // boolean
                                                                        if ("".equals(value.trim())) {
                                                                                value = "false";
                                                                        }
                                                                        m.invoke(t, Boolean.parseBoolean(value));
                                                                } else if (IOCConfigConstant.DOUBLE_ARGTYPE
                                                                                .equalsIgnoreCase(type)
                                                                                || "java.lang.Double"
                                                                                                .equalsIgnoreCase(type)) {
                                                                        // double
                                                                        if ("".equals(value.trim())) {
                                                                                value = "0.0";
                                                                        }
                                                                        m.invoke(t, Double.parseDouble(value));
                                                                }
                                                        }
                                                } else {
                                                        // 如果属性名字为空，说明使用的是构造器注入方式
                                                        // 使用构造器注入的时候，需要按照构造器参数列表顺序实例化
                                                        if (IOCConfigConstant.INT_ARGTYPE
                                                                        .equalsIgnoreCase(type)
                                                                        || "java.lang.Integer"
                                                                                        .equalsIgnoreCase(type)) {
                                                                // int
                                                                if ("".equals(value.trim())) {
                                                                        value = "0";
                                                                }
                                                                argList.add(int.class);
                                                                initargList.add(Integer.parseInt(value));
                                                        } else if (IOCConfigConstant.LONG_ARGTYPE
                                                                        .equalsIgnoreCase(type)
                                                                        || "java.lang.Long".equalsIgnoreCase(type)) {
                                                                // long
                                                                if ("".equals(value.trim())) {
                                                                        value = "0";
                                                                }
                                                                argList.add(long.class);
                                                                initargList.add(Long.parseLong(value));
                                                        } else if (IOCConfigConstant.FLOAT_ARGTYPE
                                                                        .equalsIgnoreCase(type)
                                                                        || "java.lang.Float".equalsIgnoreCase(type)) {
                                                                // float
                                                                if ("".equals(value.trim())) {
                                                                        value = "0.0";
                                                                }
                                                                argList.add(float.class);
                                                                initargList.add(Float.parseFloat(value));
                                                        } else if (IOCConfigConstant.BOOLEAN_ARGTYPE
                                                                        .equalsIgnoreCase(type)
                                                                        || "java.lang.Boolean"
                                                                                        .equalsIgnoreCase(type)) {
                                                                // boolean
                                                                if ("".equals(value.trim())) {
                                                                        value = "false";
                                                                }
                                                                argList.add(boolean.class);
                                                                initargList.add(Boolean.parseBoolean(value));
                                                        } else if (IOCConfigConstant.DOUBLE_ARGTYPE
                                                                        .equalsIgnoreCase(type)
                                                                        || "java.lang.Double"
                                                                                        .equalsIgnoreCase(type)) {
                                                                // double
                                                                if ("".equals(value.trim())) {
                                                                        value = "0.0";
                                                                }
                                                                argList.add(double.class);
                                                                initargList.add(Double.parseDouble(value));
                                                        } else if (IOCConfigConstant.STRING_ARGTYPE
                                                                        .equalsIgnoreCase(type)
                                                                        || "java.lang.String"
                                                                                        .equalsIgnoreCase(type)) {
                                                                // String
                                                                argList.add(String.class);
                                                                initargList.add(value);
                                                        }
                                                }
                                        }
                                }
                                // 如果构造方法参数列表不为空，说明需要使用构造方法进行注入
                                if (argList.size() > 0 && initargList.size() > 0) {
                                        args = new Class<?>[argList.size()];
                                        initargs = new Object[initargList.size()];
                                        for (int i = 0; i < argList.size(); i++) {
                                                args[i] = argList.get(i);
                                                initargs[i] = initargList.get(i);
                                        }

                                        t = clazz.getDeclaredConstructor(args)
                                                        .newInstance(initargs);
                                }
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                String info = "IOC.getBean(" + beanID + ") ——> " + t;
                log.debug(info);

                return t;
        }
}
