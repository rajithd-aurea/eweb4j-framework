package org.eweb4j.orm;

import java.util.Collection;

import org.eweb4j.orm.dao.DAO;

public class PageImpl<T> implements Page<T>{

	private final int pageIndex;
	private final int pageSize;

	private final DAO owner;

	public PageImpl(int pageIndex, int pageSize, DAO owner) {
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.owner = owner;
	}
	
	public Collection<T> getList() {
		return this.owner.query(pageIndex, pageSize);
	}

	public int getTotalRowCount() {
		return Integer.parseInt(String.valueOf(this.owner.count()));
	}

	public int getTotalPageCount() {
		final int all = getTotalRowCount();
		if (pageSize == 0)
			return 0;
		
		return all/pageSize + (all%pageSize > 0 ? 1 : 0);
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public boolean hasNext() {
		return getTotalPageCount() > pageIndex;
	}

	public boolean hasPrev() {
		return pageIndex > 1;
	}

	public Page<T> next() {
		return this.owner.getPage(pageIndex + 1, pageSize);
	}

	public Page<T> prev() {
		return this.owner.getPage(pageIndex - 1, pageSize);
	}

	public String getDisplayXtoYofZ(String to, String of) {
		return null;
	}

}
