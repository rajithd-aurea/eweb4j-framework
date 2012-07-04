package org.eweb4j.orm.dao.cascade;

/**
 * 级联类型
 * 
 * @author weiwei
 * 
 */
public interface CascadeType {
	public final int SELECT = 1;
	public final int DELETE = 2;
	public final int INSERT = 3;
	public final int UPDATE = 4;
	public final int MANY = -1;
	public final int ONE = -2;
	public final int MANYMANY = -3;
}
