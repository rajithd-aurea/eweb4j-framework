package org.eweb4j.mvc.view;

import java.util.ArrayList;
import java.util.List;

public class TRData {
	private String id;
	private List<String> datas = new ArrayList<String>();

	public List<String> getDatas() {
		return datas;
	}

	public void setDatas(List<String> datas) {
		this.datas = datas;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TRData [id=" + id + ", datas=" + datas + "]";
	}
	
	public TRData clone(){
		TRData data = new TRData();
		data.setId(this.getId());
		data.setDatas(this.getDatas());
		return data;
	}
	

}
