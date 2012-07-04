package org.eweb4j.mvc.view;

import java.util.ArrayList;
import java.util.List;



/**
 * 列表页视图数据对象
 * 
 * @author weiwei
 * 
 */
public class ListPage {
	private String model;
	private SearchForm searchForm;
	private List<?> pojos;
	private DivPageComp dpc;
	private List<THeadCell> thead = new ArrayList<THeadCell>();
	private List<TRData> trdatas = new ArrayList<TRData>();

	public ListPage(String model, SearchForm searchForm, List<?> pojos,
			DivPageComp dpc) {
		this.model = model;
		this.searchForm = searchForm;
		this.pojos = pojos;
		this.dpc = dpc;
	}

	public ListPage(String model, SearchForm searchForm, DivPageComp dpc,
			List<THeadCell> thead, List<TRData> trdatas) {
		this.model = model;
		this.searchForm = searchForm;
		this.thead = thead;
		this.trdatas = trdatas;
		this.dpc = dpc;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public SearchForm getSearchForm() {
		return searchForm;
	}

	public void setSearchForm(SearchForm searchForm) {
		this.searchForm = searchForm;
	}

	public List<?> getPojos() {
		return pojos;
	}

	public void setPojos(List<?> pojos) {
		this.pojos = pojos;
	}

	public DivPageComp getDpc() {
		return dpc;
	}

	public void setDpc(DivPageComp dpc) {
		this.dpc = dpc;
	}

	public List<THeadCell> getThead() {
		return thead;
	}

	public void setThead(List<THeadCell> thead) {
		this.thead = thead;
	}

	public List<TRData> getTrdatas() {
		return trdatas;
	}

	public void setTrdatas(List<TRData> trdatas) {
		this.trdatas = trdatas;
	}

	@Override
	public String toString() {
		return "ListPage [model=" + model + ", searchForm=" + searchForm
				+ ", pojos=" + pojos + ", dpc=" + dpc + ", thead=" + thead
				+ ", trdatas=" + trdatas + "]";
	}

}
