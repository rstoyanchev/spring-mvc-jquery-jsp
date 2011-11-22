package org.helloapp.spring.web.view.jsp;

public interface TemplateView {
	void setTemplatePath(String templateName);
	void setTitle(String title);
	void setLayoutParam(String paramName);
	void setLayoutParamValue(String paramValue);	
}
