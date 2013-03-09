package org.eweb4j.mvc.view;

import java.util.Collection;

public class PageMod<T> {
	private Collection<T> pojos;
	private long allCount;

	public PageMod(Collection<T> pojos, long allCount) {
		this.pojos = pojos;
		this.allCount = allCount;
	}

	public Collection<T> getPojos() {
		return pojos;
	}

	public void setPojos(Collection<T> pojos) {
		this.pojos = pojos;
	}

	public long getAllCount() {
		return allCount;
	}

	public void setAllCount(long allCount) {
		this.allCount = allCount;
	}

	@Override
	public String toString() {
		return "PageMod [pojos=" + pojos + ", allCount=" + allCount + "]";
	}

}
