package org.eweb4j.orm;

import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.eweb4j.mvc.validator.annotation.Int;
import org.eweb4j.mvc.validator.annotation.Required;
import org.eweb4j.orm.annotation.Ignore;
import org.eweb4j.orm.config.ORMConfigBeanUtil;
import org.eweb4j.orm.dao.DAO;
import org.eweb4j.orm.dao.DAOFactory;
import org.eweb4j.util.ReflectUtil;

@MappedSuperclass
public class Model{
	
	@Id
	@Column
	@GeneratedValue
	protected Long id = null;
	
	@Ignore
	private String dsName = null;

	public void setDataSourceName(String dsName) {
		this.dsName = dsName;
	}

	public DAO dao() {
		return DAOFactory.getDAO(getClass(), dsName);
	}
	
	public boolean create(String... field) {
		Long _id = getId();
		if (_id != null && _id > 0)
			return false;
		Number id;
		if (field != null && field.length > 0)
			id = DAOFactory.getInsertDAO(dsName).insertByField(this, field);
		else
			id = DAOFactory.getInsertDAO(dsName).insert(this);
		
		if (id == null || (Integer) id == -1)
			return false;

		this.setId(Long.parseLong(id + ""));
		return true;
	}

	public Model save(String... field) {
		Long id = getId();
		if (id != null && id > 0)
			if (field != null && field.length > 0)
				DAOFactory.getUpdateDAO(dsName).updateByFields(this, field);
			else
				DAOFactory.getUpdateDAO(dsName).update(this);
		else
			create(field);

		return this;
	}

	/**
	 * 根据当前实体的ID值来删除自己
	 */
	public boolean delete() {
		Long id = getId();
		if (id == null || id <= 0)
			return false;

		Number rows = DAOFactory.getDeleteDAO(dsName).deleteById(this);
		if (rows == null || (Integer) rows == -1)
			return false;

		return true;
	}

	/**
	 * 根据当前实体ID值去查询数据库
	 */
	public Model load() {
		Long id = getId();
		if (id == null || id <= 0)
			return this;

		ReflectUtil ru = new ReflectUtil(this);
		Model _model = DAOFactory.getSelectDAO(dsName).selectOneById(this);
		if (_model == null)
			return this;

		ReflectUtil _ru = new ReflectUtil(_model);
		for (String field : ru.getFieldsName()) {
			Method setter = ru.getSetter(field);
			if (setter == null)
				continue;

			Method _getter = _ru.getGetter(field);
			if (_getter == null)
				continue;

			try {
				setter.invoke(this, _getter.invoke(_model));
			} catch (Exception e) {
				continue;
			}
		}

		// ToOne relation class cascade select
		final String[] fields = ORMConfigBeanUtil.getToOneField(getClass());
		if (fields != null && fields.length > 0)
			DAOFactory.getCascadeDAO(dsName).select(this, fields);
		
		return this;
	}

	public int delete(String query, Object... params) {
		return (Integer) DAOFactory.getDeleteDAO(dsName).deleteWhere(this.getClass(), query, params);
	}

	public int deleteAll() {
		return DAOFactory.getDAO(getClass(), dsName).delete().execute();
	}

	public <T> T findById(long id) {
		T t = (T) DAOFactory.getSelectDAO(dsName).selectOneById(getClass(), id);
		
		// ToOne relation class cascade select
		final String[] fields = ORMConfigBeanUtil.getToOneField(getClass());
		if (fields != null && fields.length > 0)
			DAOFactory.getCascadeDAO(dsName).select(t, fields);
		
		return t;
	}

	public Query find() {
		Class<?> clazz = getClass();
		DAO dao = DAOFactory.getDAO(clazz, dsName);
		dao.selectAll();
		Query _query = new QueryImpl(dao);

		return _query;
	}

	public Query find(String query, Object... params) {
		Class<?> clazz = getClass();
		DAO dao = DAOFactory.getDAO(clazz, dsName);

		dao.selectAll().where().append(query).fillArgs(params);
		Query _query = new QueryImpl(dao);

		return _query;
	}

	public <T> List<T> findAll() {
		List<T> list = (List<T>) DAOFactory.getSelectDAO(dsName).selectAll(getClass());
		if (list != null)
			for (T t : list){
				// ToOne relation class cascade select
				final String[] fields = ORMConfigBeanUtil.getToOneField(getClass());
				if (fields == null || fields.length == 0)
					continue;
				
				DAOFactory.getCascadeDAO(dsName).select(t, fields);
			}
		
		return list;
	}

	public long count() {
		return DAOFactory.getSelectDAO().selectCount(getClass());
	}

	public long count(String query, Object... params) {
		return DAOFactory.getSelectDAO(dsName).selectCount(getClass(), query,params);
	}
	
	public Cascade cascade(){
		Cascade cascade = new CascadeImpl(DAOFactory.getCascadeDAO(dsName), this);
		
		return cascade;
	}

	public Long getId() {
		if ("id".equals(ORMConfigBeanUtil.getIdField(this)))
			return id;
		
		try {
			return (Long) ORMConfigBeanUtil.getIdVal(this);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return id;
	}

	public void setId(long id) {
		String idField = ORMConfigBeanUtil.getIdField(this);
		if ("id".equals(idField)){
			this.id = id;
			return ;
		}

		try {
			new ReflectUtil(this).getSetter(idField).invoke(this, id);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
