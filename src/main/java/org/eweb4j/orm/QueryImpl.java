package org.eweb4j.orm;

import java.util.List;

import org.eweb4j.orm.dao.DAO;

public class QueryImpl implements Query {
	private DAO dao;

	QueryImpl(DAO dao) {
		this.dao = dao;
	}

	public <T> List<T> fetch() {
		List<T> list =  dao.query();
		
		return list;
	}

	public <T> List<T> fetch(int max) {
		List<T> list =  dao.query(max);
		
		return list;
	}

	public <T> List<T> fetch(int page, int length) {
		List<T> list =  dao.query(page, length);
		
		return list;
	}

	public <T> T first() {
		T t = (T)dao.queryOne();
		
		return t;
	}
	
}
