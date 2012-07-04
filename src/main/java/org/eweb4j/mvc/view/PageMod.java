package org.eweb4j.mvc.view;

import java.util.List;

public class PageMod<T> {
	private List<T> pojos;
	private long allCount;

	public PageMod(List<T> pojos, long allCount) {
		this.pojos = pojos;
		this.allCount = allCount;
	}

	public List<T> getPojos() {
		return pojos;
	}

	public void setPojos(List<T> pojos) {
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
