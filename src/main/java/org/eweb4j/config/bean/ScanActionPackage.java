package org.eweb4j.config.bean;

import java.util.ArrayList;
import java.util.List;

public class ScanActionPackage {
	private List<String> path = new ArrayList<String>();

	public List<String> getPath() {
		if (path.isEmpty())
			path.add(".");
		return path;
	}

	public void setPath(List<String> path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "ScanActionPackage [path=" + path + "]";
	}
}
