package org.eweb4j.cache;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.eweb4j.util.CommonUtil;

public class SimpleCache<K, V> implements Cache<K, V> {

	private Map<K, CacheEntry<V>> cache;
	private Queue<K> queue;
	private int maxSize;
	private AtomicInteger cacheSize = new AtomicInteger();

	public SimpleCache(int maxSize) {
		this.maxSize = maxSize;
		cache = new ConcurrentHashMap<K, CacheEntry<V>>(maxSize);
		queue = new ConcurrentLinkedQueue<K>();
	}
	
	public SimpleCache() {
		this.maxSize = -1;
		cache = new ConcurrentHashMap<K, CacheEntry<V>>();
		queue = new ConcurrentLinkedQueue<K>();
	}

	/**
	 * {@inheritDoc}
	 */
	public V get(K key) {
		if (key == null) {
			throw new IllegalArgumentException("Invalid Key.");
		}

		CacheEntry<V> entry = cache.get(key);

		if (entry == null) {
			return null;
		}

		long timestamp = entry.getExpireBy();
		if (timestamp != -1 && System.currentTimeMillis() > timestamp) {
			remove(key);
			return null;
		}
		return entry.getEntry();
	}

	public V removeAndGet(K key) {

		if (key == null) {
			return null;
		}

		CacheEntry<V> entry = cache.get(key);
		if (entry != null) {
			cacheSize.decrementAndGet();
			return cache.remove(key).getEntry();
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void put(K key, V value, long secondsToLive) {
		if (key == null) {
			throw new IllegalArgumentException("Invalid Key.");
		}
		if (value == null) {
			throw new IllegalArgumentException("Invalid Value.");
		}

		long expireBy = secondsToLive != -1 ? System.currentTimeMillis()
				+ (secondsToLive * 1000L) : secondsToLive;
		boolean exists = cache.containsKey(key);
		if (!exists) {
			cacheSize.incrementAndGet();
			while (maxSize > 0 && cacheSize.get() > maxSize) {
				remove(queue.poll());
			}
		}
		cache.put(key, new CacheEntry<V>(expireBy, value));
		queue.add(key);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void put(K key, V value) {
		put(key, value, -1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void put(K key, V value, String timeToLive) {
		put(key, value, CommonUtil.toSeconds(timeToLive).longValue());
	}

	public boolean remove(K key) {
		return removeAndGet(key) != null;
	}

	public int size() {
		return cacheSize.get();
	}

	/**
	 * @param collection
	 * @return
	 */
	public Map<K, V> getAll(Collection<K> collection) {
		Map<K, V> ret = new HashMap<K, V>();
		for (K o : collection) {
			ret.put(o, get(o));
		}
		return ret;
	}

	public void clear() {
		cache.clear();
	}

	public int mapSize() {
		return cache.size();
	}

	public int queueSize() {
		return queue.size();
	}

	private class CacheEntry<V> {
		private long expireBy;
		private V entry;

		public CacheEntry(long expireBy, V entry) {
			super();
			this.expireBy = expireBy;
			this.entry = entry;
		}

		/**
		 * @return the expireBy
		 */
		public long getExpireBy() {
			return expireBy;
		}

		/**
		 * @return the entry
		 */
		public V getEntry() {
			return entry;
		}
	}

	public static void main(String[] args) throws InterruptedException{
		Cache<String, String> cache = new SimpleCache<String, String>(2);
		while (true) {
			String name = cache.get("name");
			if (name == null){
				System.out.println("store the name to cache at -> " + CommonUtil.getNowTime());
				name = "laiweiwei";
				cache.put("name", name, 5);
			}
			System.out.println("read the name->" + name + " from cache at -> " + CommonUtil.getNowTime());
			Thread.sleep(1*1000L);
		}
	}
}