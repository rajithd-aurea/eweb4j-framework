package org.eweb4j.mvc.action;

import java.util.Map;

public class QueryParams {
	private String[] value;
	private Map<String, String[]> map = null;

	public void putAll(Map<String, String[]> m) {
		this.map.putAll(m);
	}

	public void setMap(Map<String, String[]> amap) {
		map = amap;
	}

	private QueryParams get(String key) {
		this.value = map.get(key);

		return this;
	}

	private String toStr() {
		if (this.value == null)
			return "";
		
		return this.value[0];
	}

	private int toInt() {
		if (this.value == null)
			return 0;

		String s = this.value[0];
		if (s == null)
			return 0;

		return Integer.parseInt(s);
	}

	private Integer[] toIntArray() {
		String[] sts = this.value;
		if (sts == null)
			return null;

		Integer[] ints = new Integer[this.value.length];
		for (int i = 0; i < ints.length; i++) {
			if (this.value[i] == null)
				ints[i] = 0;
			else
				ints[i] = Integer.parseInt(this.value[i]);
		}

		return ints;
	}

	private String[] toStrArray() {
		return this.value;
	}

	public String toStr(String key) {
		return this.get(key).toStr();
	}

	public int toInt(String key) {
		return this.get(key).toInt();
	}

	public Integer[] toInts(String key) {

		return this.get(key).toIntArray();
	}

	public String[] toStrs(String key) {
		return this.get(key).toStrArray();
	}
}
