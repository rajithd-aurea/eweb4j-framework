package org.eweb4j.mvc.upload;

import java.io.File;

public class UploadFile {
	private File tmpFile;
	private String fileName;
	private String fieldName;
	private long size;
	private String contentType;
	
	public UploadFile(){}

	
	public UploadFile(File tmpFile, String fileName, String fieldName,long size, String contentType) {
		super();
		this.tmpFile = tmpFile;
		this.fileName = fileName;
		this.fieldName = fieldName;
		this.size = size;
		this.contentType = contentType;
	}

	public File getTmpFile() {
		return tmpFile;
	}
	public void setTmpFile(File tmpFile) {
		this.tmpFile = tmpFile;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	@Override
	public String toString() {
		return "UploadFile [tmpFile=" + tmpFile + ", fileName=" + fileName
				+ ", fieldName=" + fieldName + ", size=" + size
				+ ", contentType=" + contentType + "]";
	}
	
}
