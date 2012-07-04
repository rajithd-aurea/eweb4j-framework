package org.eweb4j.mvc;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.MVCConfigConstant;
import org.eweb4j.mvc.validator.annotation.Int;
import org.eweb4j.mvc.validator.annotation.Required;
import org.eweb4j.mvc.validator.annotation.Size;
import org.eweb4j.mvc.view.DivPageComp;
import org.eweb4j.util.JsonConverter;
import org.eweb4j.util.xml.BeanXMLUtil;
import org.eweb4j.util.xml.XMLWriter;

/**
 * this is a base super controller
 * 
 * @author weiwei
 * 
 */
public abstract class Controller implements Crud{

	private final static String PAGE_MESS = "分页参数必须是整数格式";
	private final static String PAGE_UP_MESS = "分页参数必须大于 0 ";

	protected final static String SUCCESS = MVCConfigConstant.SUCCESS_RESULT;
	
	@Required
	@Size(min = 1)
	@Int
	protected Long id;

	@Int(mess = PAGE_MESS)
	@Size(min = 1, mess = PAGE_UP_MESS)
	protected int pageNum = 1;

	@Int(mess = PAGE_MESS)
	@Size(min = 1, mess = PAGE_UP_MESS)
	protected int numPerPage = 10;

	protected String keyword = "";

	protected Context context;

	protected DivPageComp dpc = null;

	private String render(String template) {

		return template;
	}

	private String render(String template, String[] names, Object... objs) {
		for (int i = 0; i < names.length; i++)
			this.context.getModel().put(names[i], objs[i]);

		return template;
	}

	protected String render(String template, String names, Object... objs) {
		return render(template, names.split(","), objs);
	}

	protected String renderFMT(String template) {
		return "fmt:" + render(template);
	}

	protected String renderFMT(String template, String[] names, Object... objs) {
		return "fmt:" + render(template, names, objs);
	}

	protected String renderFMT(String template, String names, Object... objs) {
		return "fmt:" + render(template, names, objs);
	}

	protected String renderJSP(String template) {
		return "forward:" + render(template);
	}

	protected String renderJSP(String template, String[] names, Object... objs) {
		return "forward:" + render(template, names, objs);
	}

	protected String renderJSP(String template, String names, Object... objs) {
		return "forward:" + render(template, names, objs);
	}

	protected void renderJSON(Object obj) {
		try {
			this.context.getResponse().setContentType(MIMEType.JSON);
			this.context.getWriter().print(JsonConverter.convert(obj));
			this.context.getWriter().flush();
		} catch (IOException e) {

		}
	}

	protected void renderXML(Object obj) {
		try {
			Class<?> cls = obj.getClass();

			XMLWriter writer = BeanXMLUtil.getBeanXMLWriter(obj);
			writer.setSubNameAuto(true);
			writer.setClass(cls);
			writer.setRootElementName(null);
			this.context.getResponse().setContentType(MIMEType.XML);
			this.context.getWriter().print(writer.toXml());
			this.context.getWriter().flush();
		} catch (Exception e) {
		}
	}

	protected String render404() {
		this.context.getResponse().setStatus(404);

		return "forward:errors/404.html";
	}

	protected String renderRedirect(String path) {
		return "redirect:" + path;
	}

	protected String renderAction(String uri, String httpMethod,
			Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<Entry<String, String>> it = params.entrySet().iterator(); it
				.hasNext();) {
			Entry<String, String> en = it.next();
			String name = en.getKey();
			String value = en.getValue();
			if (sb.length() > 0)
				sb.append("&");

			sb.append(name).append("=").append(value);
		}
		if (sb.length() > 0)
			sb.insert(0, "?");

		return "action:" + uri + "@" + httpMethod + sb.toString();
	}


	protected Map<String, Object> getModel() {
		return context.getModel();
	}
	
	protected Validation getValidation(){
		return context.getValidation();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public DivPageComp getDpc() {
		return dpc;
	}

}
