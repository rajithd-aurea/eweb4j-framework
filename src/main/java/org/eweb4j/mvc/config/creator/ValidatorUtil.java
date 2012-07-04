package org.eweb4j.mvc.config.creator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eweb4j.mvc.config.bean.ValidatorConfigBean;
import org.eweb4j.mvc.validator.annotation.Skip;
import org.eweb4j.util.ClassUtil;
import org.eweb4j.util.ReflectUtil;


public class ValidatorUtil {
//	/**
//	 * 读取注解中验证器部分
//	 * 
//	 * @param actionIndex
//	 * @param validatorAnn
//	 * @param fieldAnn
//	 * @param paramAnn
//	 * @return
//	 */
//	public static List<ValidatorConfigBean> readValidator() {
//		List<ValidatorConfigBean> vList = new ArrayList<ValidatorConfigBean>();
////		String[] name = validatorAnn.value();
////		String[] clsName = validatorAnn.clazz();
//
//		for (int a = 0; a < name.length; ++a) {
//			ValidatorConfigBean v = new ValidatorConfigBean();
//			if (name != null && name.length > a)
//				v.setName(StringUtil.parsePropValue(name[a]));
//
//			if (clsName != null && clsName.length > a)
//				v.setClazz(StringUtil.parsePropValue(clsName[a]));
//
//			if (valMessAnn == null || valFieldAnn == null)
//				continue;
//
//			// 验证器数组下标
//			int[] valIndex = valMessAnn.validator();
//			// 需要验证的属性域数组下标
//			int[] fieldIndex = valMessAnn.field();
//
//			String[] valField = valFieldAnn.value();
//			String[] mess = valMessAnn.value();
//
//			List<String> fnamelist = new ArrayList<String>();
//			for (int in : fieldIndex)
//				fnamelist.add(StringUtil.parsePropValue(valField[in]));
//
//			String[] fname = fnamelist.toArray(new String[] {});
//
//			List<FieldConfigBean> fList = new ArrayList<FieldConfigBean>();
//			for (int b = 0; b < valIndex.length; ++b) {
//				if (valIndex[b] == a) {
//					FieldConfigBean f = new FieldConfigBean();
//					fList.add(f);
//					f.setName(StringUtil.parsePropValue(fname[b]));
//					f.setMessage(StringUtil.parsePropValue(mess[b]));
//
//					if (paramAnn == null || paramName == null)
//						continue;
//
//					int[] pindex = paramAnn.valMess();
//					int[] pnameIndex = paramAnn.name();
//					String[] pnames = paramName.value();
//
//					List<String> pnamelist = new ArrayList<String>();
//					for (int in : pnameIndex)
//						pnamelist.add(StringUtil.parsePropValue(pnames[in]));
//
//					String[] pname = pnamelist.toArray(new String[] {});
//					String[] pvalue = paramAnn.value();
//
//					List<ParamConfigBean> pList = new ArrayList<ParamConfigBean>();
//					for (int c = 0; c < pindex.length; ++c) {
//						if (pindex[c] == b) {
//							ParamConfigBean p = new ParamConfigBean();
//							p.setName(StringUtil.parsePropValue(pname[c]));
//							p.setValue(StringUtil.parsePropValue(pvalue[c]));
//							pList.add(p);
//						}
//					}
//
//					f.setParam(pList);
//
//				}
//			}
//
//			v.setField(fList);
//			vList.add(v);
//		}
//
//		return vList;
//	}

	/**
	 * 从Action属性中读取验证器配置
	 * @param <T>
	 * @param params
	 * @param scopeName
	 * @param ru
	 * @param vList
	 * @param hasCls
	 * @return
	 */
	public static <T> List<ValidatorConfigBean> readValidator(final String[] params,final String[] excepts, String scopeName,
			ReflectUtil ru, List<ValidatorConfigBean> vList,
			Set<Class<?>> hasCls) {
		if (params == null || params.length == 0)
			return null;
		
		if (ru == null)
			return null;

		Field[] fs = ru.getFields();
		if (fs == null)
			return null;

		if (vList == null)
			vList = new ArrayList<ValidatorConfigBean>();

		ValidatorConfigBean val = null;
		for (Field f : fs) {
			Skip iv = f.getAnnotation(Skip.class);
			if (iv != null)
				continue;

			if (ClassUtil.isPojo(f.getType())) {
				// 解决无限递归问题
				if (hasCls == null)
					hasCls = new HashSet<Class<?>>();

				if (!hasCls.contains(f.getType())) {
					hasCls.add(f.getType());
					if (scopeName != null && scopeName.length() > 0)
						scopeName = scopeName+"."+f.getName();
					else
						scopeName = f.getName();
					
					try {
						readValidator(params,excepts, scopeName, new ReflectUtil(f.getType()),vList, hasCls);
						scopeName = null;
					} catch (Exception e) {
						continue;
					}
				}

				continue;
			}

			for (Annotation ann : f.getAnnotations()) {
				ValidatorCreator valCreator = ValidatorFactory.get(ann);
				if (valCreator == null)
					continue;
				
				String name = f.getName();
				if (scopeName != null && scopeName.length() > 0)
					name = scopeName + "." + name;

				for (String param : params){
					
					if (Arrays.asList(excepts).contains(name))
						continue;
					
					boolean flag = false;
					
					if (!param.equals("*") && param.endsWith("*") 
							&& name.startsWith(param.replace("*", "")))
						flag = true;
					
					if (!param.equals("*") && param.startsWith("*") 
							&& name.endsWith(param.replace("*", "")))
						flag = true;
					
					if (name.equals(param) || param.equals("*"))
						flag = true;
					
					if (flag){
						val = valCreator.create(name, val);
						if (val != null) 
							vList.add(val);
						
						break;
					}
				}
			}
		}

		scopeName = null;
		if (vList.size() > 0)
			return vList;

		return null;
	}
}
