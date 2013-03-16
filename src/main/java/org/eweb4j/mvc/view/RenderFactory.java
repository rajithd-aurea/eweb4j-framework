package org.eweb4j.mvc.view;

import org.eweb4j.mvc.action.RenderType;


public class RenderFactory {
	
	public static Renderer create(String name) {
		if (RenderType.VELOCITY.equals(name) || RenderType.VELOCITY2.equals(name))
			return new VelocityRendererImpl();
		
		if (RenderType.FREEMARKER.equals(name) || RenderType.FREEMARKER2.equals(name))
			return new FreemarkerRendererImpl();
		
		return null;
	}
	
}
