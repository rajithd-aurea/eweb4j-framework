package org.eweb4j.orm;

import java.util.Collection;

import org.eweb4j.orm.dao.DAO;

public final class PageImpl<T> implements Page<T>{

	private final int pageIndex;
	private final int pageSize;
	private final DAO owner;
	private Collection<T> pojos;
	private Long totalCount;
	private Long totalPageCount;
	
	public PageImpl(int pageIndex, int pageSize, DAO owner) {
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.owner = owner;
	}
	
	public synchronized Collection<T> getList() {
		if (this.pojos == null)
			this.pojos = this.owner.query(pageIndex, pageSize);
		return this.pojos;
	}

	public synchronized long getTotalRowCount() {
		if (totalCount == null)
			totalCount = this.owner.count();
		
		return totalCount;
	}

	public synchronized long getTotalPageCount() {
		final long all = getTotalRowCount();
		if (pageSize == 0)
			return 0;
		if (this.totalPageCount == null)
			this.totalPageCount = all/pageSize + (all%pageSize > 0 ? 1 : 0);
		
		return this.totalPageCount;
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
