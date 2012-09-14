package org.eweb4j.config.bean;

import java.util.ArrayList;
import java.util.List;

public class ScanPojoPackage {
	private List<String> path = new ArrayList<String>();

	public List<String> getPath() {
		if (path.isEmpty()){
			path.add(".");
			path.add("JAR:*");
			path.add("AP:${RootPath}/target/classes");
		}
		return path;
	}

	public void setPath(List<String> path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "ScanPojoPackage [path=" + path + "]";
	}
}
