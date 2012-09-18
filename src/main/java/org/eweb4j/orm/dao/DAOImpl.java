package org.eweb4j.orm.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.eweb4j.cache.DBInfoConfigBeanCache;
import org.eweb4j.cache.ORMConfigBeanCache;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;
import org.eweb4j.orm.OrderType;
import org.eweb4j.orm.Page;
import org.eweb4j.orm.PageImpl;
import org.eweb4j.orm.config.ORMConfigBeanUtil;
import org.eweb4j.orm.dao.config.DAOConfigConstant;
import org.eweb4j.orm.jdbc.DataSourceWrapCache;
import org.eweb4j.orm.jdbc.JdbcUtil;
import org.eweb4j.orm.sql.SelectSqlCreator;
import org.eweb4j.orm.sql.SqlFactory;
import org.eweb4j.util.ClassUtil;
import org.eweb4j.util.CommonUtil;
import org.eweb4j.util.ReflectUtil;

public class DAOImpl implements DAO {

	private static Log log = LogFactory.getORMLogger(DAO.class);

	private Map<String, Object> buffer = new HashMap<String, Object>();
	
	private String orderStr = "";
	private boolean express = false;
	private Map<String, Object> map;
	private Class<?> clazz;
	private String dsName;
	private StringBuilder sql = new StringBuilder("");
	private StringBuilder condition = new StringBuilder("");
	private List<Object> args = new ArrayList<Object>();;
	private String dbType;
	private DataSource ds;
	private Set<String> joins = new HashSet<String>();
	private String table = null;
	private String selectAllColumn;

	private Map<String, String> aliasMap = new HashMap<String, String>();

	public DAOImpl(String dsName) {
		this.dsName = dsName;

		this.dsName = dsName;
		this.ds = DataSourceWrapCache.get(dsName);

		dbType = DBInfoConfigBeanCache.get(dsName).getDataBaseType();
	}

	private void init(Class<?> clazz, String dsName) {
		if (dsName == null)
			dsName = DAOConfigConstant.MYDBINFO;

		this.clazz = clazz;
		this.dsName = dsName;
		this.ds = DataSourceWrapCache.get(dsName);

		dbType = DBInfoConfigBeanCache.get(dsName).getDataBaseType();
		if (Map.class.isAssignableFrom(clazz)) {
			if (this.map != null) {
				selectAllColumn = ORMConfigBeanUtil.getSelectAllColumn(map);
				table = (String) map.get("table") + " map";
			}
		} else {
			this.table = ORMConfigBeanUtil.getTable(clazz);
			selectAllColumn = ORMConfigBeanUtil.getSelectAllColumn(clazz);
		}

		if (selectAllColumn == null || selectAllColumn.trim().length() == 0)
			selectAllColumn = "*";
		
		this.buffer.put("orderField", ORMConfigBeanUtil.getIdField(clazz));
		this.buffer.put("orderType", OrderType.DESC_ORDER);
	}

	public DAOImpl(Map<String, Object> map) {
		this.map = map;
		init(Map.class, null);
	}

	public DAOImpl(Map<String, Object> map, String dsName) {
		this.map = map;
		init(Map.class, dsName);
	}

	public DAOImpl(Class<?> clazz) {
		init(clazz, null);
	}

	public DAOImpl(Class<?> clazz, String dsName) {
		init(clazz, dsName);
	}

	public DAO append(String query) {
		String _fieldName = handleFieldAlias(query);
		String column = ORMConfigBeanUtil.getColumn(clazz, _fieldName);
		if (column == null)
			column = query;

		query = ORMConfigBeanUtil.parseQuery(query, clazz);

		this.condition.append(" ").append(query).append(" ");
		return this;
	}
	
	public DAO field(String fieldName) {
		String _fieldName = handleFieldAlias(fieldName);
		String column = ORMConfigBeanUtil.getColumn(clazz, _fieldName);
		this.condition.append(" ").append(column).append(" ");
		return this;
	}

	private String handleFieldAlias(String fieldName) {
		int dotIndex = fieldName.indexOf(".");
		if (dotIndex > 0 && dotIndex < fieldName.length() -1){
			String[] dots = fieldName.split("\\.");
			StringBuilder builder = new StringBuilder();
			for (String dot : dots){
				if (aliasMap.containsKey(dot))
					dot = aliasMap.get(dot);
				if (builder.length() > 0)
					builder.append(".");
				builder.append(dot);
			}
			if (builder.length() > 0)
				fieldName = builder.toString();
		}
		return fieldName;
	}

	public DAO notLike(Object value) {
		this.condition.append(" NOT LIKE ");
		
		if (!this.express)
			condition.append("'").append(value).append("' ");
		else
			condition.append(ORMConfigBeanUtil.getColumn(clazz, handleFieldAlias(String.valueOf(value))));
		
		return this;
	}

	public DAO notEqual(Object value) {
		this.condition.append(" <> ");
		
		if (!this.express)
			condition.append("'").append(value).append("' ");
		else
			condition.append(ORMConfigBeanUtil.getColumn(clazz, handleFieldAlias(String.valueOf(value))));
		
		return this;
	}

	public DAO equal(Object value) {
		this.condition.append(" = ");
		
		if (!this.express)
			condition.append("'").append(value).append("' ");
		else
			condition.append(ORMConfigBeanUtil.getColumn(clazz, handleFieldAlias(String.valueOf(value))));
		
		return this;
	}

	public DAO moreThan(Object value) {
		this.condition.append(" > ");
		
		if (!this.express)
			condition.append("'").append(value).append("' ");
		else
			condition.append(ORMConfigBeanUtil.getColumn(clazz, handleFieldAlias(String.valueOf(value))));
		
		return this;
	}

	public DAO lessThan(Object value) {
		this.condition.append(" < ");
		
		if (!this.express)
			condition.append("'").append(value).append("' ");
		else
			condition.append(ORMConfigBeanUtil.getColumn(clazz, handleFieldAlias(String.valueOf(value))));
		
		return this;
	}

	public DAO or(String fieldName) {
		String _fieldName = handleFieldAlias(fieldName);
		String column = ORMConfigBeanUtil.getColumn(clazz, _fieldName);
		this.condition.append(" OR ").append(column).append(" ");
		return this;
	}

	public DAO and(String fieldName) {
		String _fieldName = handleFieldAlias(fieldName);
		String column = ORMConfigBeanUtil.getColumn(clazz, _fieldName);
		this.condition.append(" AND ").append(column).append(" ");
		return this;
	}

	public DAO orderBy(String fieldName) {
		String _fieldName = handleFieldAlias(fieldName);
		String column = ORMConfigBeanUtil.getColumn(clazz, _fieldName);
		this.orderStr = " ORDER BY " + column;
		this.buffer.put("orderField", column);
		return this;
	}
	
	public DAO desc(){
		this.orderStr += " DESC ";
		this.buffer.put("orderType", OrderType.DESC_ORDER);
		return this;
	}
	
	public DAO asc(){
		this.orderStr += " ASC ";
		this.buffer.put("orderType", OrderType.ASC_ORDER);
		return this;
	}
	
	public DAO order(String fieldName, String orderType){
		String _fieldName = handleFieldAlias(fieldName);
		String column = ORMConfigBeanUtil.getColumn(clazz, _fieldName);
		this.orderStr = " ORDER BY " + column + " DESC ";
		this.buffer.put("orderField", column);
		if ("asc".equalsIgnoreCase(orderType))
			this.buffer.put("orderType", OrderType.ASC_ORDER);
		else
			this.buffer.put("orderType", OrderType.DESC_ORDER);
		
		return this;
	}
	
	public DAO desc(String fieldName) {
		String _fieldName = handleFieldAlias(fieldName);
		String column = ORMConfigBeanUtil.getColumn(clazz, _fieldName);
		this.orderStr = " ORDER BY " + column + " DESC ";
		this.buffer.put("orderField", column);
		this.buffer.put("orderType", OrderType.DESC_ORDER);
		return this;
	}

	public DAO asc(String fieldName) {
		String _fieldName = handleFieldAlias(fieldName);
		String column = ORMConfigBeanUtil.getColumn(clazz, _fieldName);
		this.orderStr = " ORDER BY " + column + " ASC ";
		this.buffer.put("orderField", column);
		this.buffer.put("orderType", OrderType.ASC_ORDER);
		return this;
	}

	// --------------------------------------------------------

	private <T> List<T> query(String sql) {
		List<T> result = null;
		try {

			if (Map.class.isAssignableFrom(this.clazz)) {
				Connection con = ds.getConnection();
				if (args != null && args.size() > 0) {
					result = (List<T>) JdbcUtil.getListWithArgs(con, clazz, sql, args.toArray(new Object[] {}));
				} else {
					result = (List<T>) JdbcUtil.getList(con, clazz, sql);
				}

			} else {
				if (args != null && args.size() > 0) {
					result = (List<T>) DAOFactory.getSelectDAO(dsName).selectBySQL(clazz, sql, args.toArray(new Object[] {}));
				} else {
					result = (List<T>) DAOFactory.getSelectDAO(dsName).selectBySQL(clazz, sql);
				}

			}

			//this.clear();
			if (result != null && result.size() > 0){
				for (T t : result){
					// ToOne relation class cascade select
					final String[] fields = ORMConfigBeanUtil.getToOneField(t.getClass());
					if (fields == null || fields.length == 0)
						continue;
					
					DAOFactory.getCascadeDAO(dsName).select(t, fields);
				}
			}
			
			return result;
		} catch (Exception e) {
			log.error("sql-->" + sql + "exception:" + CommonUtil.getExceptionString(e));
			throw new DAOException(sql + " execute exception", e);
		}
	}
	
	public long count(){
		final String query = this.condition.toString().replace("WHERE", "").replace("'?'", "?");
		String _table = this.table;
		if (this.joins != null && !this.joins.isEmpty()){
			StringBuilder sb = new StringBuilder();
			for (String j : joins){
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(j);
			}
			_table = this.table + ", " + sb.toString();
		}
		
		final String sql = "SELECT COUNT(*) as count FROM " + _table + " WHERE " + ORMConfigBeanUtil.parseQuery(query, clazz);
		List<Map> maps = null;
		if (args != null && args.size() > 0) {
			maps = DAOFactory.getSelectDAO(dsName).selectBySQL(Map.class, sql, args);
		}else
			maps = DAOFactory.getSelectDAO(dsName).selectBySQL(Map.class, sql);
		
		if (maps == null || maps.isEmpty())
			return 0;
		
		return Long.parseLong(String.valueOf(maps.get(0).get("count")));
	}

	public <T> List<T> query() {
		return query(toSql());

	}

	public <T> List<T> query(int max) {
		return query(1, max);
	}

	public <T> List<T> query(int page, int length) {
		String sql = null;
		final String orderField = (String) buffer.get("orderField");
		final int oType = (Integer) buffer.get("orderType");
		final String query = this.condition.toString().replace("'?'", "?");
		try {
			Object obj = null;
			if (Map.class.isAssignableFrom(clazz)){
				obj = new HashMap<String, Object>();
				((Map<String, Object>)obj).put("table", this.table);
			}else {
				obj = clazz.newInstance();
			}
			
			SelectSqlCreator<Object> select =  SqlFactory.getSelectSql(obj, dbType);
			if (this.joins != null && !this.joins.isEmpty()){
				StringBuilder sb = new StringBuilder();
				for (String j : joins){
					if (sb.length() > 0)
						sb.append(", ");
					sb.append(j);
				}
				select.setTable(this.table + ", " + sb.toString());
			}
			sql = select.divPage(page, length, orderField, oType, query.replace("WHERE", ""));
		} catch (Exception e) {
			String _table = this.table;
			if (this.joins != null && !this.joins.isEmpty()){
				StringBuilder sb = new StringBuilder();
				for (String j : joins){
					if (sb.length() > 0)
						sb.append(", ");
					sb.append(j);
				}
				_table = this.table + ", " + sb.toString();
			}
			
			sql = this.sql.append(orderStr).append(" LIMIT ").append((page - 1) * length).append(", ").append(length).toString().replace("${_TABLES_}", _table).replace("${_where_}", query);
		}
		
		return query(sql);
	}

	public <T> T queryOne() {
		List<T> list = query();
		T result = list == null ? null : list.size() > 0 ? list.get(0) : null;
		return result;
	}

	public DAO selectStr(String str) {
		if (str == null || clazz == null)
			return this;

		this.sql.append(" SELECT ").append(str).append(" FROM ")
				.append(" ${_TABLES_} ").append(" ");

		return this;
	}

	public DAO insert(String... fields) {
		if (clazz == null)
			return this;

		StringBuilder sb = new StringBuilder();
		String[] columns;
		if (fields == null)
			columns = ORMConfigBeanUtil.getColumns(clazz);
		else
			columns = ORMConfigBeanUtil.getColumns(clazz, fields);

		for (String col : columns) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(col);
		}

		this.sql.append(" INSERT INTO ").append(table.replace(" "+clazz.getSimpleName().toLowerCase(), "")).append("(").append(sb.toString()).append(") ");

		return this;
	}

	public DAO values(Object... values) {
		if (values == null) {
			values = new Object[ORMConfigBeanUtil.getColumns(clazz).length];

			for (int i = 0; i < values.length; i++)
				values[i] = "?";
		}

		StringBuilder sb = new StringBuilder();
		for (Object val : values) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append("'").append(val).append("'");
		}
		this.sql.append(" VALUES(").append(sb.toString()).append(") ");

		return this;
	}

	public DAO insert(Map<String, Object> map) {
		int size = map.size();
		List<String> fields = new ArrayList<String>(size);
		List<Object> values = new ArrayList<Object>(size);
		for (Iterator<Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();) {
			Entry<String, Object> entry = it.next();
			fields.add(entry.getKey());
			values.add(entry.getValue());
		}

		this.insert(fields.toArray(new String[] {})).values(
				values.toArray(new Object[] {}));

		return this;
	}

	public DAO where() {
		this.sql.append("${_where_}");
		this.condition.append(" WHERE ");
		return this;
	}
	
	public DAO isNull(){
		this.condition.append(" IS NULL ");
		return this;
	}
	
	public DAO isNotNull(){
		this.condition.append(" IS NOT NULL ");
		return this;
	}

	public int execute() {
		int id = -1;
		String sql = this.sql.toString().replace("${_where_}", this.condition.toString()).replace("'?'", "?");
		DataSource ds = DataSourceWrapCache.get(dsName);
		try {
			int rs = 0;
			if (args != null && args.size() > 0) {
				rs = (Integer) JdbcUtil.updateWithArgs(ds.getConnection(), sql, args.toArray(new Object[] {}));
			} else {
				rs = (Integer) JdbcUtil.update(ds.getConnection(), sql);
			}

			if (rs > 0 && sql.contains("INSERT INTO")) {
				if (Map.class.isAssignableFrom(clazz)) {
					if (map == null) {
						map = new HashMap<String, Object>();
						map.put("idColumn", "id");
						map.put("table", this.table);
					} else if (map.get("idColumn") == null) {
						map.put("idColumn", "id");
					}

					id = (Integer) DAOUtil.selectMaxId(map, ds.getConnection(),dbType);
				} else {
					id = (Integer) DAOUtil.selectMaxId(clazz,ds.getConnection(), dbType);
				}
			}

		} catch (SQLException e) {
			log.error("sql-->" + sql + "exception:" + CommonUtil.getExceptionString(e));
			throw new DAOException(sql + " execute exception", e);
		}

		//this.clear();
		return id;
	}

	public DAO update() {
		if (clazz == null)
			return this;

		this.sql.append(" UPDATE ").append(table.replace(" "+clazz.getSimpleName().toLowerCase(), "")).append(" ");

		return this;
	}

	public DAO set(String[] fields, Object... values) {
		if (fields == null || values == null || fields.length == 0
				|| values.length == 0 || fields.length != values.length)
			return this;

		StringBuilder sb = new StringBuilder();
		String[] columns = ORMConfigBeanUtil.getColumns(clazz, fields);
		for (int i = 0; i < values.length; i++) {
			if (sb.length() > 0)
				sb.append(", ");
			String col = columns[i];
			Object val = values[i];
			sb.append(col).append(" = '").append(val).append("'");
		}

		this.sql.append(" SET ").append(sb.toString()).append(" ");

		return this;
	}

	public DAO set(Map<String, Object> map) {
		int size = map.size();
		List<String> fields = new ArrayList<String>(size);
		List<Object> values = new ArrayList<Object>(size);
		for (Iterator<Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();) {
			Entry<String, Object> entry = it.next();
			fields.add(entry.getKey());
			values.add(entry.getValue());
		}

		this.set(fields.toArray(new String[] {}),
				values.toArray(new Object[] {}));

		return this;
	}

	public DAO delete() {
		if (clazz == null)
			return this;

		this.sql.append(" DELETE FROM ").append(table.replace(" "+clazz.getSimpleName().toLowerCase(), "")).append(" ");

		return this;
	}

	public DAO selectAll() {
		this.sql.append(" SELECT ").append(selectAllColumn)
				.append(" FROM ").append(" ${_TABLES_} ").append(" ");
		return this;
	}

	public DAO select(Class<?>... classes){
		if (classes == null || classes.length == 0)
			return this;
		
		this.sql.append(" SELECT ");
		StringBuilder sb = new StringBuilder();
		for(Class<?> cls : classes){
			String _selectAllColumn = ORMConfigBeanUtil.getSelectAllColumn(cls);
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(_selectAllColumn);
		}
		
		if (sb.length() == 0)
			sb.append(selectAllColumn);
		
		sql.append(sb.toString()).append(" FROM ").append(" ${_TABLES_} ").append(" ");
		
		return this;
	}
	
	public DAO select(String... fields) {
		if (fields == null || clazz == null)
			return this;

		StringBuilder sb = new StringBuilder();
		for (String field : fields) {
			if (sb.length() > 0)
				sb.append(", ");
			String _field = handleFieldAlias(field);
			String col = ORMConfigBeanUtil.getColumn(clazz, _field);
			sb.append(col);
		}
		this.sql.append(" SELECT ").append(sb.toString())
				.append(" FROM ").append(" ${_TABLES_} ").append(" ");

		return this;
	}

	public DAO likeLeft(Object value) {
		this.condition.append(" LIKE ");
		
		if (!this.express)
			condition.append("'").append(value).append("%' ");
		else
			condition.append(ORMConfigBeanUtil.getColumn(clazz, handleFieldAlias(String.valueOf(value))));
		
		return this;
	}

	public DAO likeRight(Object value) {
		this.condition.append(" LIKE ");
		
		if (!this.express)
			condition.append("'%").append(value).append("' ");
		else
			condition.append(ORMConfigBeanUtil.getColumn(clazz, handleFieldAlias(String.valueOf(value))));
		
		return this;
	}

	public DAO like(Object value) {
		this.condition.append(" LIKE ");
		
		if (!this.express)
			condition.append("'%").append(value).append("%' ");
		else
			condition.append(ORMConfigBeanUtil.getColumn(clazz, handleFieldAlias(String.valueOf(value))));
		
		return this;
	}

	public DAO clear() {
		this.sql = null;
		this.sql = new StringBuilder();
		this.buffer.clear();
		this.buffer.put("orderField", ORMConfigBeanUtil.getIdField(clazz));
		this.buffer.put("orderType", OrderType.DESC_ORDER);
		this.condition = null;
		this.condition = new StringBuilder();
		this.args.clear();
		this.orderStr = "";
		this.joins = null;
		this.joins = new HashSet<String>();
		this.express = false;
		this.aliasMap = null;
		this.aliasMap = new HashMap<String, String>();

		return this;
	}

	public DAO in(Object... values) {
		if (values == null)
			return this;

		this.condition.append(" in(");

		StringBuilder sb = new StringBuilder();
		for (Object o : values) {
			if (sb.length() > 0)
				sb.append(", ");

			if (!this.express)
				sb.append("'").append(o).append("'");
			else
				sb.append(ORMConfigBeanUtil.getColumn(clazz, handleFieldAlias(String.valueOf(o))));
		}

		this.condition.append(sb.toString());

		this.condition.append(") ");

		return this;
	}

	public DAO notIn(Object... values) {
		if (values == null)
			return this;

		this.condition.append(" not in(");

		StringBuilder sb = new StringBuilder();
		for (Object o : values) {
			if (sb.length() > 0)
				sb.append(", ");

			if (!this.express)
				sb.append("'").append(o).append("'");
			else
				sb.append(ORMConfigBeanUtil.getColumn(clazz, handleFieldAlias(String.valueOf(o))));
		}

		this.condition.append(sb.toString());

		this.condition.append(") ");

		return this;
	}

	public DAO inSql(String sql) {
		if (sql == null)
			return this;

		this.condition.append(" in(").append(sql).append(") ");

		return this;
	}

	public DAO notInSql(String sql) {
		if (sql == null)
			return this;

		this.condition.append(" not in(").append(sql).append(") ");

		return this;
	}

	public String toSql() {
		String _table = this.table;
		if (this.joins != null && !this.joins.isEmpty()){
			StringBuilder sb = new StringBuilder();
			for (String j : joins){
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(j);
			}
			_table = this.table + ", " + sb.toString();
		}
		
		return sql.append(orderStr).toString().replace("${_TABLES_}", _table).replace("${_where_}", condition.toString()).replace("'?'", "?");
	}

	public DAO fillArgs(Object... args) {
		if (args != null) {
			for (Object arg : args)
				this.args.add(arg);
		}

		return this;
	}

	public DAO setTable(String table) {
		this.table = table;
		return this;
	}

	public DAO setClass(Class<?> clazz) {
		this.clazz = clazz;

		if (Map.class.isAssignableFrom(clazz)) {
			if (this.map != null) {
				selectAllColumn = ORMConfigBeanUtil.getSelectAllColumn(map);
				table = (String) map.get("table");
			}
		} else {
			this.table = ORMConfigBeanUtil.getTable(clazz);
			selectAllColumn = ORMConfigBeanUtil.getSelectAllColumn(clazz);
		}

		if (selectAllColumn == null || selectAllColumn.trim().length() == 0)
			selectAllColumn = "*";

		return this;
	}

	public DAO setMap(Map<String, Object> map) {
		this.map = map;
		return this;
	}

	public String getDsName() {
		return dsName;
	}

	public <T> Page<T> getPage(int pageIndex, int pageSize) {
		Page<T> page = new PageImpl<T>(pageIndex, pageSize, this);
		return page;
	}

	public DAO leftJoin(String... fieldNames) {
		
		return this;
	}

	public DAO rightJoin(String... fieldNames) {
		
		return this;
	}

	public DAO join(String fieldName){
		return join(fieldName, fieldName);
	}
	
	public DAO enableExpress(boolean flag){
		this.express = flag;
		return this;
	}
	
	public DAO join(String fieldName, String alias) {
		if (fieldName == null || alias == null)
			return this;
		
		handleJoin(fieldName, alias);
		
		return this;
	}
	
	public DAO alias(String alias){
		if (alias == null || alias.trim().length() == 0)
			return this;
		this.aliasMap.put(alias, this.clazz.getSimpleName().toLowerCase());
		return this;
	}

	private void handleJoin(String _fieldName, String _alias) {
		String[] fDots = _fieldName.split("\\.");
		String[] aDots = _alias.split("\\.");
		Class<?> currentClazz = this.clazz;
		for (int i = 0; i < fDots.length; i++){
			String fieldName = fDots[i];
			String alias = aDots[i];
		
			ReflectUtil ru = null;
			try {
				ru = new ReflectUtil(currentClazz);
				Field field = ru.getField(fieldName);
				if (field == null)
					throw new Exception("field->"+fieldName+" invalid");
				Class<?> cls = ClassUtil.getGenericType(field);
				if (cls == null)
					throw new Exception("can not get the field->"+fieldName+" class");
				if (!ORMConfigBeanCache.containsKey(cls.getName()))
					throw new Exception("field->" + fieldName + cls.getName() + " is not a entity");
				
				String table = ORMConfigBeanUtil.getTable(cls);
				
				joins.add(table);
				aliasMap.put(alias, fieldName);
				currentClazz = cls;
			} catch (Exception e){
				log.error(e.toString());
			}
		}
	}

	public DAO on() {
		return null;
	}

	public DAO groupBy(String... fieldNames) {
		if (fieldNames == null || fieldNames.length == 0)
			return this;

		StringBuilder builder = new StringBuilder();
		for (String field : fieldNames){
			String _field = handleFieldAlias(field);
			String col = ORMConfigBeanUtil.getColumn(clazz, _field);
			if (builder.length() > 0)
				builder.append(", ");
			builder.append(col);
		}
		this.condition.append(" group by ").append(builder.toString()).append(" ");

		return this;
	}
	
	
}
