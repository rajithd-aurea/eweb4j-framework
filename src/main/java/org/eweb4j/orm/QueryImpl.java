package org.eweb4j.orm;

import java.util.Collection;

import org.eweb4j.orm.dao.DAO;

@SuppressWarnings("all")
public class QueryImpl implements Query {
	private DAO dao;

	QueryImpl(DAO dao) {
		this.dao = dao;
	}

	public <T> Collection<T> fetch() {
		Collection<T> list =  dao.query();
		
		return list;
	}

	public <T> Collection<T> fetch(int max) {
		Collection<T> list =  dao.query(max);
		
		return list;
	}

	public <T> Collection<T> fetch(int page, int length) {
		Collection<T> list =  dao.query(page, length);
		
		return list;
	}

	public <T> T first() {
		T t = (T)dao.queryOne();
		
		return t;
	}
	
}
