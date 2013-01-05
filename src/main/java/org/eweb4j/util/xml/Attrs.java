package org.eweb4j.util.xml;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * 强大的XML标签属性过滤【通过正则】
 * @author weiwei l.weiwei@163.com
 * @date 2013-1-5 下午09:44:24
 */
public class Attrs {

	public static void main(String[] args){
		//XML文本
		String xml = "<div style='width:250; height:auto;'>This is div.<img src='http://www.baidu.com/logo.gif' alt='This is img' /></div><p style='padding:5px;'>This is p.<ul><li>This is li.<a href='http://www.baidu.com'>This is link.</a></li></ul></p>";
		
		//删除所有标签的所有属性
		String rs = Attrs.me().xml(xml).rm().ok();
		System.out.println(rs);//结果：<div>This is div.<img /></div><p>This is p.<ul><li>This is li.<a>This is link.</a></li></ul></p>
		
		//删除所有标签的style属性和alt属性
		String rs2 = Attrs.me().xml(xml).rm("style", "alt").Tags().ok();
		System.out.println(rs2);//结果：<div>This is div.<img src='http://www.baidu.com/logo.gif' /></div><p>This is p.<ul><li>This is li.<a href='http://www.baidu.com'>This is link.</a></li></ul></p>
		
		//删除img标签的src、alt属性，删除div标签的style属性
		String rs3 = Attrs.me().xml(xml).tag("img").rm("src", "alt").tag("div", "p").rm("style").ok();
		System.out.println(rs3);//结果：<div>This is div.<img /></div><p style='padding:5px;'>This is p.<ul><li>This is li.<a href='http://www.baidu.com'>This is link.</a></li></ul></p>
	}
	
	private String xml = null;
	private Collection<String> currentTag = new HashSet<String>();
	
	public static Attrs me(){
		return new Attrs();
	}
	
	public Attrs xml(String xml){
		this.xml = xml;
		return this;
	}
	
	public Tags Tags(){
		return Tags.me().xml(xml);
	}
	
	public Attrs rm(){
		xml = removeXmlTagAttr(xml, "", null);
		return this;
	}
	
	public Attrs tag(String tag){
		this.currentTag.add(tag);
		return this;
	}
	
	public Attrs tag(String... tag){
		this.currentTag.addAll(Arrays.asList(tag));
		return this;
	}
	
	public Attrs rm(String attr){
		xml = removeXmlTagAttr(xml, currentTag, Arrays.asList(attr));
		return this;
	}
	
	public Attrs rm(String... attr){
		xml = removeXmlTagAttr(xml, currentTag, Arrays.asList(attr));
		return this;
	}
	
	public String ok(){
		currentTag = null;
		return xml;
	}
	
	public static String removeXmlTagAttr(String html, Collection<String> tags, Collection<String> attrs){
		if (tags == null || tags.isEmpty())
			return removeXmlTagAttr(html, "", attrs);
		String rs = html;
		for (String tag : tags){
			rs = removeXmlTagAttr(rs, tag, attrs);
		}
		return rs;
	}
	public static String removeXmlTagAttr(String html, String tag, Collection<String> attrs){
		String fmt = "(?<=<%s{1,255})\\s+%s=[\"'][^'\"]*[\"']";
		
		if (tag == null || tag.trim().length() == 0)
			tag = ".";//all tags
		
		if (attrs == null || attrs.size() == 0)
			return html.replaceAll(String.format(fmt, tag, "\\w+"), "");//all attributes
		
		for (String attr : attrs){
			if (attr == null || attr.trim().length() == 0)
				continue;
			
			String regex = String.format(fmt, tag, attr);
			html = html.replaceAll(regex, "");
		}
		
		return html;
	}
	
	
}
