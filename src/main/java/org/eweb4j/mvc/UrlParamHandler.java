package org.eweb4j.mvc;


public class UrlParamHandler {
	/**
	 * Url参数正则匹配
	 * 
	 * @param url 只能处理一个“{}”变量
	 * @param regexSplit
	 * @return 返回匹配出来的数组（包括参数名和参数值） 思路：
	 * 
	 *         1.url 包含"@"吗？ 1.1包含：url首尾是否有"@"包含？ 1.1.1包含：取去掉首尾"@"剩下的部分
	 *         1.1.2不包含：去掉"@" 1.2不包含：将首部分替换成"@",重复1 所以，其处理过程为：
	 *         给定urlMapping：test/{id}/edit
	 *         test/{id}/edit——>@{id}/edit——>@{id}/edit——>@{id}@
	 *         给定urlMapping：test/{id}/edit/{name}/wei
	 * 
	 *         reqUrl：test/3/edit
	 * 
	 */
	public static String matchersUrlParam(String _url, String[] regexSplit) {
		String url = new String(_url);
		for (int i = 0; i < regexSplit.length + 1; i++) {
//			System.out.println("url-->"+url);
			if (url.contains("@")) {
//				System.out.println("con@url-->"+url);
				int start = url.indexOf("@");
				int end = url.lastIndexOf("@");

				if (end > start){
//					System.out.println("con@val-->"+url.substring(start + 1, end));
					return url.substring(start + 1, end);
					
				}
				else if (i == regexSplit.length && end == start){
//					System.out.println("con@val-->"+url.substring(start + 1));
					return url.substring(start + 1);
				}
				
				
				url = url.substring(end);
//				System.out.println("con@url.sub-->"+url);
			}

			if (i < regexSplit.length){
				
				url = new String(url.replaceFirst(regexSplit[i], "@"));
//				System.out.println("no@url.rep-->"+regexSplit[i]+"-->"+url);
			}
		}

		return null;
	}
}
