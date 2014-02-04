package org.springframework.web.servlet.view.deprecated;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.JspTemplateViewResolver;
import org.springframework.web.servlet.view.JstlView;

public class DecoratedJstlView extends JstlView implements DecoratedView {
	protected String templatePath;
	protected String title;
	protected String layoutParam = JspTemplateViewResolver.LAYOUT_PARAM;
	protected String layoutParamValue = JspTemplateViewResolver.CANCEL_LAYOUT;

	public DecoratedJstlView() {
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
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (useTemplate(request)) {
			model.put("main", getUrl());
			model.put("title", title);
			System.out.println(this + "render: page:" + getUrl() + " template:" + templatePath);
		}
		super.renderMergedOutputModel(model, request, response);
	}

	@Override
	protected String prepareForRendering(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (useTemplate(request)) {
			return templatePath;
		}
		else {
			return super.prepareForRendering(request, response);
		}
	}

	protected boolean useTemplate(HttpServletRequest request) {
		System.out.println(this + ".useTemplate:" + layoutParam + "=" + layoutParamValue + " =?"
				+ request.getParameter(layoutParam));
		return layoutParamValue == null || !layoutParamValue.equals(request.getParameter(layoutParam));
	}
}
