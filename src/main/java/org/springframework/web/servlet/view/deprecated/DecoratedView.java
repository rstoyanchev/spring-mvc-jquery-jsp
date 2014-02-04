package org.springframework.web.servlet.view.deprecated;

import org.springframework.web.servlet.View;


/**
 * DecoratedView interface.
 * 
 * A View that supports automated decoration based on a view template,
 * supports for title resolution and i18n, and partial page rendering.
 * 
 * @author Jorge Simão
 *
 */
public interface DecoratedView extends View {
	
	/**
	 * Set the logical name of the template view.
	 * 
	 * @param templateName the logical name of the template view
	 */
	void setTemplatePath(String templateName);

	void setTitle(String title);

	void setLayoutParam(String paramName);

	void setLayoutParamValue(String paramValue);	
}
