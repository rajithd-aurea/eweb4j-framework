package org.eweb4j.orm;

public interface IModel {

	void setDataSourceName(String dsName);

	/**
	 * 将当前实体的状态插入数据库，并将ID值注入当前实体。 若当前实体本身已含有ID值，不执行操作。
	 * 
	 * @return
	 */
	boolean create();

	/**
	 * 将当前实体的存入数据库（若ID值没有，则插入，否则执行更新）
	 * 
	 * @return
	 */
	Model save();

	/**
	 * 删除实体 by id
	 * 
	 * @return 删除
	 */
	boolean delete();

	/**
	 * 通过当前实体的ID值加载实体数据，默认从缓存中获取，若找不到，则取数据库取
	 * 
	 * @return
	 */
	Model load();

}
