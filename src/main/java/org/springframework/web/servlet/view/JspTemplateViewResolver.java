package org.springframework.web.servlet.view;

import org.springframework.util.ClassUtils;

public class JspTemplateViewResolver extends InternalResourceViewResolver {
	private static final boolean jstlPresent = ClassUtils.isPresent(
			"javax.servlet.jsp.jstl.core.Config", InternalResourceViewResolver.class.getClassLoader());

	public static final String LAYOUT_PARAM = "ajax";

	public static final String LAYOUT_PARAM_VALUE = "true";
	
	protected String templateName;
	
	protected String layoutParam = LAYOUT_PARAM;
	
	protected String layoutParamValue = LAYOUT_PARAM_VALUE;
	
	public JspTemplateViewResolver(String templateName) {
		this.templateName = templateName;
		Class<?> viewClass = jstlPresent ? DecoratedJstlView.class : DecoratedInternalResourceView.class;
		setViewClass(viewClass);
	}
	
	public JspTemplateViewResolver() {
		this(null);
	}
	
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		TemplateView view = (TemplateView) super.buildView(viewName);
		String templatePath = getPrefix() + templateName + getSuffix();
		view.setTemplatePath(templatePath);
		view.setTitle(getTitle(viewName));
		view.setLayoutParam(layoutParam);
		view.setLayoutParamValue(layoutParamValue);
		return (AbstractUrlBasedView)view;
	}
	
	protected String getTitle(String viewName) {
		return "view.title." + preprocessViewName(viewName).replace('/', '.');
	}
	
	protected String preprocessViewName(String viewName) {
		int i= viewName.indexOf('?');
		return i>0 ? viewName.substring(0,i) : viewName;
	}
	
}
