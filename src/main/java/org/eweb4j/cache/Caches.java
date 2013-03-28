package org.eweb4j.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple cache implementation with LRU and lease time for elements.
 */
public class Caches {
	private static final long serialVersionUID = -8741231309235098214L;
	private Map<Object, Element> cache = null;	
	private int maxCacheTime = -1;
	public final static int CACHE_FOREVER = -1;
	
	/**
	 * Creates a simple cache.
	 * @param maxCapacity maximum number of elements in the LRU cache
	 * @param leaseTime maximum time in millis an element will be returned after it's last put
	 */
	@SuppressWarnings("serial")
	public Caches(final int maxCapacity, final int leaseTime) {
		this.cache = Collections.synchronizedMap(new LinkedHashMap<Object, Element>(25, 0.75f) {
			@SuppressWarnings("unchecked")
			protected boolean removeEldestEntry(Map.Entry eldest) {
		        return size() > maxCapacity;
		     }
		});
		this.maxCacheTime = leaseTime;
	}
	
	/**
	 * Looks for an element in the cache.
	 * @param key the cache key
	 * @return null, if no element for the key found or if the element was longer than <tt>leaseTime</tt> in the cache.
	 */
	public Object get(Object key) {
		Element el = this.cache.get(key);
		if (el != null) {
			if (maxCacheTime >= 0) {
				if (System.currentTimeMillis() - el.timestamp <= maxCacheTime) {
					return el.element;
				} else {
					this.cache.remove(key);
					return null;
				}
			}
		}
		return null;
	}
	
	/**
	 * Stores an element in the cache
	 * @param key the cache key
	 * @param value the element to store
	 * @return the previous element with the given cache key, or null.
	 */
	public Object put(Object key, Object value) {
		Element el = this.cache.put(key, new Element(value));
		if (el != null) {
			return el.element;
		}
		return null;
	}
	
	/**
	 * Clears the cache.
	 */
	public void clearCache() {
		this.cache.clear();
	}
	
	/**
	 * Returns the current count of cached elements. This might include expired elements too.
	 * @return current count
	 */
	public int size() {
		return cache.size();
	}
	
	/**
	 * Helper class to implement the lease time.
	 * @author Philipp Naderer
	 */
	private class Element {
		protected Object element;
		protected long timestamp;
		
		private Element(Object element) {
			this.element = element;
			this.timestamp = System.currentTimeMillis();
		}
		
		@SuppressWarnings("unused")
		private long touch() {
			long old = this.timestamp;
			this.timestamp = System.currentTimeMillis();
			return old;
		}
	}
	
	public static void main(String[] args){
		Caches cache = new Caches(5, Caches.CACHE_FOREVER);
	}
}