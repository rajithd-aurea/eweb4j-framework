package org.eweb4j.mvc.view;

/**
 * 渲染接口
 * @author weiwei
 *
 */
public interface Renderer {
	public <T> void render(String[] names,T... ts);
	public <T> void render(String name,T t);
}
