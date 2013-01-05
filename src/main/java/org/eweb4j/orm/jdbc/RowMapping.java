package org.eweb4j.orm.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eweb4j.util.CommonUtil;


/**
 * 将从数据库查询出来的结果集映射成java对象
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
@SuppressWarnings("all")
public class RowMapping {

	public static void main(String[] args){
		System.out.println(Byte.class.getName());
	}
	
	public static <T> T mapOneRow(ResultSet rs, Class<T> cls) throws Exception {
		List<T> list = mapRows(rs, cls);
		return list == null ? null : list.get(0);
	}
	
	public static <T> List<T> mapRows(ResultSet rs, Class<T> cls) throws Exception {
		if (rs == null)
			return null;
		
		ResultSetMetaData rsmd = rs.getMetaData();
		List<String> columns = new ArrayList<String>();
		for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
			columns.add(rsmd.getColumnLabel(i));
		}
		
		List<Map<String, Object>> _list = new ArrayList<Map<String, Object>>();

		while (rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columns.size(); ++i) {
				String name = columns.get(i - 1);
				name = name.toLowerCase();
				Object obj = rs.getObject(name);
				//System.out.println("rs->"+columns.get(i - 1)+"=>"+obj);
				map.put(name, obj);
			}
			_list.add(map);
		}
		
		if (Map.class.isAssignableFrom(cls)) 
			return (List<T>) (_list.isEmpty() ? null : _list);
		
		return CommonUtil.mappingPojo(_list, cls);
	}

}
