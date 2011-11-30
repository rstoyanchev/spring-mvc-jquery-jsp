package org.springframework.web.servlet.view;

public interface TemplateView {
	void setTemplatePath(String templateName);
	void setTitle(String title);
	void setLayoutParam(String paramName);
	void setLayoutParamValue(String paramValue);	
}
