package org.eweb4j.mvc.view;


public class EditPage<T> {
	private String action;
	private String model;
	private T pojo;

	public EditPage(String model,String action, T pojo) {
		this.model = model;
		this.action = action;
		this.pojo = pojo;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public T getPojo() {
		return pojo;
	}

	public void setPojo(T pojo) {
		this.pojo = pojo;
	}
}
