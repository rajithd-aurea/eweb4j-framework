package org.eweb4j.orm;

import org.eweb4j.orm.dao.cascade.CascadeDAO;

public class CascadeImpl implements Cascade{

	private CascadeDAO cascadeDAO;
	private Model model;
	
	CascadeImpl(CascadeDAO cascadeDAO, Model model){
		this.cascadeDAO = cascadeDAO;
		this.model = model;
	}
	
	public void merge(String... fields) {
		this.cascadeDAO.select(model, fields);
	}

	public void refresh(long newIdVal, String... fields) {
		this.cascadeDAO.update(model, newIdVal, fields);
	}

	public void remove(String... fields) {
		this.cascadeDAO.delete(model, fields);
	}

	public void persist(String... fields) {
		this.cascadeDAO.insert(model, fields);
	}

}
