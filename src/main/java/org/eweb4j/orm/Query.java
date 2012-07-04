package org.eweb4j.orm;

import java.util.List;

public interface Query {

	/**
	 * Retrieve all results of the query.
	 * 
	 * @param <T>
	 * @return
	 */
	<T> List<T> fetch();

	/**
	 * Retrieve results of query.
	 * 
	 * @param <T>
	 * @param max
	 * @return
	 */
	<T> List<T> fetch(int max);

	/**
	 * Retrieve a page of result.
	 * 
	 * @param <T>
	 * @param page
	 * @param length
	 * @return
	 */
	<T> List<T> fetch(int page, int length);

	/**
	 * 
	 * @param <T>
	 * @return
	 */
	<T> T first();
	
}
