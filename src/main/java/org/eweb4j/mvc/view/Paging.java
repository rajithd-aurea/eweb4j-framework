package org.eweb4j.mvc.view;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页组件
 * 
 * @author cfuture.aw
 * 
 */
public class Paging {
	public int start = 1;
	public int end;
	public int nextNum;// 下一页
	public String nextBtn = "下一页";

	public int preNum;// 上一页
	public String preBtn = "上一页";

	public long allCount;// 所有记录数
	public int currentNum;// 当前页号
	public int pageCount;// 可以分成多少页
	public int numPerPage;// 每一页多少
	public int maxShow;// 最多显示多少页码

	public String firstBtn = "首页";
	public String lastBtn = "末页";

	public List<Integer> numbers = new ArrayList<Integer>();

	public Paging(int pageNum, int numPerPage, long allCount, int maxShow) {
		if (pageNum == 0) {
			pageNum = 1;
		}
		if (numPerPage == 0) {
			numPerPage = 10;
		}
		this.maxShow = maxShow;
		this.currentNum = pageNum;
		this.numPerPage = numPerPage;
		this.allCount = allCount;
		this.doWork();
	}

	public Paging(String strPageNum, String strNumPerPage, long allCount,int maxShow) {
		int pageNum = 1;
		if (strPageNum.matches("^-?\\d+$")) {
			pageNum = Integer.parseInt(strPageNum);
		}
		int numPerPage = 10;
		if (strNumPerPage.matches("^-?\\d+$")) {
			numPerPage = Integer.parseInt(strNumPerPage);
		}

		this.maxShow = maxShow;
		this.currentNum = pageNum;
		this.numPerPage = numPerPage;
		this.allCount = allCount;
		this.doWork();
	}

	public void doWork() {

		this.countPageCount();
		this.doNextAndPre();
		
		if (this.pageCount < this.maxShow)
			this.maxShow = this.pageCount;

		end = this.maxShow + this.currentNum;
		if (end >= this.pageCount)
			end = this.pageCount;

		int add = this.currentNum - this.maxShow;

		if (add > start)
			start = add;

		for (int i = start; i <= end; ++i)
			numbers.add(i);
		
	}

	private void countPageCount() {
		pageCount = (int) (allCount - 1) / numPerPage + 1;
	}

	private void doNextAndPre() {
		if (currentNum < pageCount)
			nextNum = currentNum + 1;
		else
			nextNum = pageCount;
		if (currentNum > 1)
			preNum = currentNum - 1;
		else
			preNum = 1;
	}

	public void setCurrentPage(int currentPage) {
		if (currentPage < 1) {
			this.currentNum = 1;
			return;
		}
		if (pageCount >= currentPage)
			this.currentNum = currentPage;
		else
			this.currentNum = pageCount;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getNextNum() {
		return nextNum;
	}

	public void setNextNum(int nextNum) {
		this.nextNum = nextNum;
	}

	public String getNextBtn() {
		return nextBtn;
	}

	public void setNextBtn(String nextBtn) {
		this.nextBtn = nextBtn;
	}

	public int getPreNum() {
		return preNum;
	}

	public void setPreNum(int preNum) {
		this.preNum = preNum;
	}

	public String getPreBtn() {
		return preBtn;
	}

	public void setPreBtn(String preBtn) {
		this.preBtn = preBtn;
	}

	public long getAllCount() {
		return allCount;
	}

	public void setAllCount(long allCount) {
		this.allCount = allCount;
	}

	public int getCurrentNum() {
		return currentNum;
	}

	public void setCurrentNum(int currentNum) {
		this.currentNum = currentNum;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public int getMaxShow() {
		return maxShow;
	}

	public void setMaxShow(int maxShow) {
		this.maxShow = maxShow;
	}

	public String getFirstBtn() {
		return firstBtn;
	}

	public void setFirstBtn(String firstBtn) {
		this.firstBtn = firstBtn;
	}

	public String getLastBtn() {
		return lastBtn;
	}

	public void setLastBtn(String lastBtn) {
		this.lastBtn = lastBtn;
	}

	public List<Integer> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<Integer> numbers) {
		this.numbers = numbers;
	}

	
}
