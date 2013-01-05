package org.eweb4j.util.xml;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * 强大的XML标签过滤【通过正则】
 * @author weiwei l.weiwei@163.com
 * @date 2013-1-5 下午09:44:24
 */
public class Tags {
	
	public static void main(String[] args){
		//XML文本
		String xml = "<div>This is div.</div><p>This is p.<ul><li>This is li.<a href='http://www.baidu.com'>This is link.</a></li></ul></p>";
		
		//删除所有标签
		String rs = Tags.me().xml(xml).rm().ok();
		System.out.println(rs);//结果：This is div.This is p.This is li.This is link.
		
		//保留p、a标签，其他都删除
		String kpRs = Tags.me().xml(xml).kp("p", "a").ok();
		System.out.println(kpRs);//结果：This is div.<p>This is p.This is li.<a href='http://www.baidu.com'>This is link.</a></p>
		
		//删除p、a标签，其他保留
		String rmRs = Tags.me().xml(xml).rm("p", "a").ok();
		System.out.println(rmRs);//结果：<div>This is div.</div>This is p.<ul><li>This is li.This is link.</li></ul>
		
		//Tags和Attrs两个类是可以同时使用的，切换的时候，上一个的执行结果作为下一个的参数继续处理
		//删除div、ul、li标签然后删除a标签的href属性
		String sRs = Tags.me().xml(xml).rm("div", "ul","li").Attrs().tag("a").rm("href").ok();
		System.out.println(sRs);//结果：<div>This is div.</div><p>This is p.<ul><li>This is li.<a>This is link.</a></li></ul></p>
		
		//删除所有标签的href属性，然后保留div、a标签，其他标签都删除
		String sRs2 = Attrs.me().xml(xml).rm("href").Tags().kp("div", "a").ok();
		System.out.println(sRs2);//结果：
	}
	
	private String xml = null;
	private Collection<String> kps = new HashSet<String>();
	private Collection<String> rms = new HashSet<String>();
	
	public static Tags me(){
		return new Tags();
	}
	
	public Tags xml(String xml){
		this.xml = xml;
		return this;
	}
	
	public Attrs Attrs(){
		return Attrs.me().xml(xml);
	}
	
	public Tags rm(){
		xml = cleanXmlTags(xml);
		return this;
	}
	
	public Tags rm(String tag){
		this.rms.add(tag);
		return this;
	}
	
	public Tags rm(String... tag){
		this.rms.addAll(Arrays.asList(tag));
		return this;
	}
	
	public Tags kp(String tag){
		this.kps.add(tag);
		return this;
	}
	
	public Tags kp(String... tag){
		this.kps.addAll(Arrays.asList(tag));
		return this;
	}
	
	/**
	 * 本方法主要用于方便Tags切换到Attrs
	 */
	public Tags exe(){
		if (!this.rms.isEmpty()){
			xml = cleanXmlTags(xml, rms.toArray(new String[]{}));
			this.rms.clear();
		} if (!this.kps.isEmpty()){
			xml = cleanOtherXmlTags(xml, kps.toArray(new String[]{}));
			this.kps.clear();
		}
		
		return this;
	}
	
	public String ok(){
		exe();
		return xml;
	}
	
	/**
	 * 如果不给定keepTags会删除所有Tag，否则删除给定之外的Tag
	 * @date 2013-1-5 下午05:24:06
	 * @param html
	 * @param keepTags
	 * @return
	 */
	public static String cleanOtherXmlTags(String html, String... keepTags) {
		return html.replaceAll(inverseXmlTagsRegex(keepTags), "");
	}
	
	/**
	 * 如果不给定delTags，会删除所有Tag，否则删除给定的Tag
	 * @date 2013-1-5 下午05:35:27
	 * @param html
	 * @param delTags
	 * @return
	 */
	public static String cleanXmlTags(String html, String... delTags) {
		return html.replaceAll(xmlTagsRegex(delTags), "");
	}
	
	public static String inverseXmlTagsRegex(String... excludeTags) {
		if (excludeTags == null || excludeTags.length == 0)
			return "<[!/]?\\b\\w+\\b\\s*[^>]*>";
		String fmt = "\\b%s\\b";
		StringBuilder sb = new StringBuilder();
		for (String kt : excludeTags){
			if (kt == null || kt.trim().length() == 0)
				continue;
			
			if (sb.length() > 0)
				sb.append("|");
			sb.append(String.format(fmt, kt));
		}
		if (sb.length() == 0)
			return "<[!/]?\\b\\w+\\b\\s*[^>]*>";
		
		String pattern = "<[!/]?\\b(?!("+sb.toString()+"))+\\b\\s*[^>]*>";
		
		return pattern;
	}
	
	public static String xmlTagsRegex(String... excludeTags) {
		if (excludeTags == null || excludeTags.length == 0)
			return "<[!/]?\\b\\w+\\b\\s*[^>]*>";
		String fmt = "\\b%s\\b";
		StringBuilder sb = new StringBuilder();
		for (String kt : excludeTags){
			if (kt == null || kt.trim().length() == 0)
				continue;
			
			if (sb.length() > 0)
				sb.append("|");
			sb.append(String.format(fmt, kt));
		}
		if (sb.length() == 0)
			return "<[!/]?\\b\\w+\\b\\s*[^>]*>";
		
		String pattern = "<[!/]?("+sb.toString()+")\\s*[^>]*>";
		
		return pattern;
	}
}
