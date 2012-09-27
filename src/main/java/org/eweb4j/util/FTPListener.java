package org.eweb4j.util;

public interface FTPListener {

	void onInfo(String info);
	
	void onError(String err, Exception e);
	
}
