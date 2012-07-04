package org.eweb4j.mvc.view;


/**
 * 分页组件
 * 
 * @author cfuture.aw
 * 
 */
public class DivPageComp {
	private String location;// 分页url
	private int nextPage;// 下一页
	private int prePage;// 上一页
	private long allCount;// 所有记录数
	private int currentPage;// 当前页号
	private int pageCount;// 可以分成多少页
	private int numPerPage;// 每一页多少
	private int maxShow;// 最多显示多少页码
	private String html;
	private String classStyle;// css样式
	private String curPageClassStyle;
	private String nxtBtnName = "下一页";
	private String preBtnName = "上一页";
	private String firstBtnName = "首页";
	private String lastBtnName = "末页";

	public DivPageComp(int pageNum, int numPerPage, long allCount) {
		if (pageNum == 0) {
			pageNum = 1;
		}
		if (numPerPage == 0) {
			numPerPage = 10;
		}
		this.setMaxShow(4);
		this.currentPage = pageNum;
		this.numPerPage = numPerPage;
		this.allCount = allCount;
		this.doWork();
	}
	
	public DivPageComp(int pageNum, int numPerPage, long allCount, int maxShow) {
		if (pageNum == 0) {
			pageNum = 1;
		}
		if (numPerPage == 0) {
			numPerPage = 10;
		}
		this.setMaxShow(maxShow);
		this.currentPage = pageNum;
		this.numPerPage = numPerPage;
		this.allCount = allCount;
		this.doWork();
	}

	public DivPageComp(String strPageNum, String strNumPerPage, long allCount,
			int maxShow) {
		int pageNum = 1;
		if (strPageNum.matches("^-?\\d+$")) {
			pageNum = Integer.parseInt(strPageNum);
		}
		int numPerPage = 10;
		if (strNumPerPage.matches("^-?\\d+$")) {
			numPerPage = Integer.parseInt(strNumPerPage);
		}

		this.setMaxShow(maxShow);
		this.currentPage = pageNum;
		this.numPerPage = numPerPage;
		this.allCount = allCount;
		this.doWork();
	}

	public void doWork() {
		String clickHandler = "divPage(%s)";
		if (this.location != null && !"".equals(this.location.trim())) {
			clickHandler = "window.location='"
					+ this.location.replace("{pageNum}", "%s") + "'";
		}
		this.countPageCount();
		this.doNextAndPre();
		if (this.pageCount < this.maxShow) {
			this.maxShow = this.pageCount;
		}
		StringBuilder sb = new StringBuilder();
		if (this.prePage > 1) {
			if (this.classStyle != null) {
				sb.append("<span class=\"").append(this.classStyle)
						.append("\" onclick=\"")
						.append(String.format(clickHandler, this.prePage))
						.append("\"").append(">"+this.preBtnName+"</span>");
			} else {
				sb.append(
						"<span onmouseover=\"this.style.color='red'\" onmouseout=\"this.style.color=''\" style='padding:5px; cursor:pointer;' onclick=\"")
						.append(String.format(clickHandler, this.prePage))
						.append("\"").append(">"+this.preBtnName+"</span>");
			}
		}
		int start = 1;
		int end = this.maxShow + this.currentPage;
		if (end >= this.pageCount) {
			end = this.pageCount;
		}
		int add = this.currentPage - this.maxShow;

		if (add > start) {
			start = add;
		}

		if (start > this.maxShow) {
			if (this.classStyle != null) {
				sb.append("<span class='").append(this.classStyle).append("'")
						.append(" onclick=\"")
						.append(String.format(clickHandler, 1)).append("\"")
						.append(">").append(this.firstBtnName).append("</span>...");
			} else {
				sb.append(
						"<span onmouseover=\"this.style.color='red'\" onmouseout=\"this.style.color=''\" style='padding:5px; cursor:pointer;' onclick=\"")
						.append(String.format(clickHandler, 1)).append("\"")
						.append(")>[").append(this.firstBtnName).append("]</span>...");
			}
		}

		for (int i = start; i <= end; ++i) {
			if (this.classStyle != null) {
				sb.append("<span class=\"").append(this.classStyle)
						.append("\"");
			} else {
				sb.append("<span onmouseover=\"this.style.color='red'\" onmouseout=\"this.style.color=''\" style='padding:5px; cursor:pointer;' ");
			}
			if (i == this.currentPage) {
				if (this.curPageClassStyle != null
						&& !"".equals(this.curPageClassStyle)) {
					sb.append("'><span class='").append(this.curPageClassStyle)
							.append("'>").append(i).append("</span>");
				} else {
					sb.append(
							"'><font color='red'><span style='color:red;cursor:text;'>")
							.append(i).append("</span></font>");
				}
			} else {
				sb.append("onclick=\"").append(String.format(clickHandler, i))
						.append("\"").append(">");
				if (this.classStyle != null) {
					sb.append(i);
				} else {
					sb.append("[").append(i).append("]");
				}
			}
			sb.append("</span>");
		}

		if (end < this.pageCount - this.maxShow) {
			if (this.classStyle != null) {
				sb.append("... <span class='").append(this.classStyle)
						.append("'").append(" onclick=\"")
						.append(String.format(clickHandler, this.pageCount))
						.append("\"").append(">").append(this.lastBtnName)
						.append("</span>");
				;
			} else {
				sb.append(
						"... <span onmouseover=\"this.style.color='red'\" onmouseout=\"this.style.color=''\" style='padding:5px; cursor:pointer;'")
						.append(" onclick=\"")
						.append(String.format(clickHandler, this.pageCount))
						.append("\"").append(">[").append(this.lastBtnName)
						.append("]</span>");
			}
		}

		if (this.nextPage < this.pageCount) {
			if (this.classStyle != null) {
				sb.append("<span class=\"").append(this.classStyle)
						.append("\"").append(" onclick=\"")
						.append(String.format(clickHandler, this.nextPage))
						.append("\"").append(">" + this.nxtBtnName + "</span>");
			} else {
				sb.append(
						"<span onmouseover=\"this.style.color='red'\" onmouseout=\"this.style.color=''\" style='padding:5px; cursor:pointer;' onclick=\"")
						.append(String.format(clickHandler, this.nextPage))
						.append("\"").append(">" + this.nxtBtnName + "</span>");
			}
		}

		this.html = sb.toString();
	}

	private void countPageCount() {
		pageCount = (int) (allCount - 1) / numPerPage + 1;
	}

	private void doNextAndPre() {
		if (currentPage < pageCount)
			nextPage = currentPage + 1;
		else
			nextPage = pageCount;
		if (currentPage > 1)
			prePage = currentPage - 1;
		else
			prePage = 1;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getPrePage() {
		return prePage;
	}

	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public long getAllCount() {
		return allCount;
	}

	public void setAllCount(long allCount) {
		this.allCount = allCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		if (currentPage < 1) {
			this.currentPage = 1;
			return;
		}
		if (pageCount >= currentPage)
			this.currentPage = currentPage;
		else
			this.currentPage = pageCount;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getHtml() {
		return html;
	}

	public void setMaxShow(int maxShow) {
		this.maxShow = maxShow;
	}

	public int getMaxShow() {
		return maxShow;
	}

	public void setClassStyle(String classStyle) {
		this.classStyle = classStyle;
	}

	public String getClassStyle() {
		return classStyle;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCurPageClassStyle() {
		return curPageClassStyle;
	}

	public void setCurPageClassStyle(String curPageClassStyle) {
		this.curPageClassStyle = curPageClassStyle;
	}

	public String getNxtBtnName() {
		return nxtBtnName;
	}

	public void setNxtBtnName(String nxtBtnName) {
		this.nxtBtnName = nxtBtnName;
	}

	public String getPreBtnName() {
		return preBtnName;
	}

	public void setPreBtnName(String preBtnName) {
		this.preBtnName = preBtnName;
	}

	public String getFirstBtnName() {
		return firstBtnName;
	}

	public void setFirstBtnName(String firstBtnName) {
		this.firstBtnName = firstBtnName;
	}

	public String getLastBtnName() {
		return lastBtnName;
	}

	public void setLastBtnName(String lastBtnName) {
		this.lastBtnName = lastBtnName;
	}
}
