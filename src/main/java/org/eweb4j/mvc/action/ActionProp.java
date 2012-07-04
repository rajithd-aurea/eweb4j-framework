package org.eweb4j.mvc.action;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import org.eweb4j.cache.Props;

public class ActionProp {
	private String propId;
	private Map<String, String> map = null;

	public ActionProp(){}
	
	public ActionProp(String propId) {
		this.propId = propId;
		this.map = Props.getMap(propId);
	}

	public String read(String key) {
		if (map == null)
			return null;
		
		return map.get(key);
	}
	
	public void write(String key, Object value) throws IOException{
		Props.writeProp(key, key, value);
	}

	public Map<String, String> getMap() {
		return map;
	}

	public String getPropId() {
		return propId;
	}

	public void setPropId(String propId) {
		this.propId = propId;
	}

	public void setMap(Hashtable<String, String> map) {
		this.map = map;
	}
	
	
}
