package org.eweb4j.orm;

import org.eweb4j.orm.dao.DAOFactory;

/**
 * Db operator for Active Record Model Helper
 * @author weiwei l.weiwei@163.com
 * @date 2013-1-22 下午03:51:22
 */
public class Db {
	
	public static <T> void batchUpdate(T[] ts, String... fields){
		DAOFactory.getUpdateDAO().batchUpdate(ts, fields);
	}
	
	public static <T> void batchInsert(T[] ts, String... fields){
		Number[] ids = DAOFactory.getInsertDAO().batchInsert(ts, fields);
		if (ids != null && ids.length > 0){
			for (int i = 0; i < ts.length; i++){
				ModelHelper<T> helper = new ModelHelper<T>(ts[i]);
				helper._setId(ids[i].longValue());
			}
		}
	}
	
	public static <T> ModelHelper<T> ar(T t){
		return new ModelHelper<T>(t);
	}
	
	public static <T> ModelHelper<T> ar(Class<T> cls){
		return Models.inst(cls);
	}
}
