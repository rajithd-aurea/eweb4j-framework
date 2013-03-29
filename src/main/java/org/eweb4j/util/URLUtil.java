package org.eweb4j.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-3-29 下午01:00:11
 */
public class URLUtil {
	
	public static String read(String link){
		BufferedReader reader = null;
		try {
			URL url = new URL(link);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null){
				sb.append(line);
			}
			
			return sb.toString();
		} catch (Throwable e){
			e.printStackTrace();
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}

	public static void main(String[] args){
		String json = URLUtil.read("http://localhost:8080/duan/api/create/url?data=http://www.baidu.com");
		System.out.println(json);
		Map<String, Object> jsonMap = CommonUtil.parse(json, Map.class);
		String shortLink = "http://localhost:8080/duan/" + jsonMap.get("code");
		System.out.println(shortLink);
	}
}
