package org.eweb4j.mvc.exception;
/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-3-29 上午11:26:35
 */
public class ActionException extends RuntimeException{

	private int statusCode = 500;
	private static final long serialVersionUID = 1L;

	public ActionException(int statusCode, String err){
		super(err);
		this.statusCode = statusCode;
	}
	
	public ActionException(int statusCode, Throwable e){
		super(e);
		this.statusCode = statusCode;
	}
	
	public ActionException(int statusCode, String err, Throwable e){
		super(err, e);
		this.statusCode = statusCode;
	}
	
	public ActionException(String err){
		super(err);
	}

	public ActionException(String err, Throwable e){
		super(err, e);
	}
	
	public ActionException(Throwable e){
		super(e);
	}

	
	public int getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
}
