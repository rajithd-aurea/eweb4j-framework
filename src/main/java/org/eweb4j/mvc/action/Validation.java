package org.eweb4j.mvc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Context of request validte
 * 
 * @author weiwei
 * 
 */
public class Validation {

	private Map<String, Map<String, String>> errors = new HashMap<String, Map<String, String>>();

	public boolean hasErr() {
		if (errors.isEmpty()) return false;
		
		return errors.size() > 0;
	}

	public Map<String, List<String>> getAllErr() {
		Map<String, List<String>> result = new HashMap<String, List<String>>();

		for (Iterator<Entry<String, Map<String, String>>> it = iterator(); it.hasNext(); ) {
			Entry<String, Map<String, String>> en = it.next();
			Map<String, String> mess = en.getValue();
			for (Iterator<Entry<String,String>> m = mess.entrySet().iterator(); m.hasNext(); ){
				Entry<String, String> en2 = m.next();
				String key = en2.getKey();
				String value = en2.getValue();
				if (!result.containsKey(key)){
					List<String> messes = new ArrayList<String>();
					messes.add(value);
					result.put(key, messes);
				}else{
					result.get(key).add(value);
				}
			}
			
		}

		return result;
	}

	public Iterator<Entry<String, Map<String, String>>> iterator() {
		return errors.entrySet().iterator();
	}

	public Map<String, Map<String, String>> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, Map<String, String>> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "Validation [errors=" + errors + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errors == null) ? 0 : errors.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Validation other = (Validation) obj;
		if (errors == null) {
			if (other.errors != null)
				return false;
		} else if (!errors.equals(other.errors))
			return false;
		return true;
	}

}
