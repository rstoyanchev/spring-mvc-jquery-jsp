package org.helloapp.spring.web.view.jsp;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.InternalResourceView;

public class DecoratedInternalResourceView extends InternalResourceView implements TemplateView {
	protected String templatePath;
	protected String title;
	protected String layoutParam = JspTemplateViewResolver.LAYOUT_PARAM;
	protected String layoutParamValue = JspTemplateViewResolver.LAYOUT_PARAM_VALUE;
	
	
	public DecoratedInternalResourceView() {
	}

	public String getTemplatePath() {
		return templatePath;
	}
	
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLayoutParam() {
		return layoutParam;
	}

	public void setLayoutParam(String layoutParam) {
		this.layoutParam = layoutParam;
	}

	public String getLayoutParamValue() {
		return layoutParamValue;
	}

	public void setLayoutParamValue(String layoutParamValue) {
		this.layoutParamValue = layoutParamValue;
	}
	
	@Override
	protected void renderMergedOutputModel(
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (useTemplate(request)) {
			model.put("main", getUrl());
			model.put("title", title);
			System.out.println(this + "render: page:" + getUrl() + " template:" + templatePath);
		}
		super.renderMergedOutputModel(model, request, response);
	}
	
	@Override
	protected String prepareForRendering(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (useTemplate(request)) {
			return templatePath;
		} else {
			return super.prepareForRendering(request, response);
		}
	}
	
	protected boolean useTemplate(HttpServletRequest request) {
		return layoutParamValue==null || layoutParamValue.equals(request.getParameter(layoutParam));
	}
	
	
}

