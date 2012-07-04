package org.eweb4j.config.bean;

import java.io.File;

import org.eweb4j.util.xml.AttrTag;

public class UploadConfigBean {

	@AttrTag
	private String tmp = "${RootPath}" + File.separator + "WEB-INF/tmp";

	@AttrTag
	private String maxMemorySize = "4M";

	@AttrTag
	private String maxRequestSize = "4M";

	//private String suffix = "zip,rar,jpg,jpeg,png,bmp,gif,doc,docx,ppt,pptx,xls,xlsx";
	
	public long countMaxRequestSize(){
		return 0;
	}
	
	public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	public String getMaxMemorySize() {
		return maxMemorySize;
	}

	public void setMaxMemorySize(String maxMemorySize) {
		this.maxMemorySize = maxMemorySize;
	}

	public String getMaxRequestSize() {
		return maxRequestSize;
	}

	public void setMaxRequestSize(String maxRequestSize) {
		this.maxRequestSize = maxRequestSize;
	}

//	public String getSuffix() {
//		return suffix;
//	}
//
//	public void setSuffix(String suffix) {
//		this.suffix = suffix;
//	}

}
