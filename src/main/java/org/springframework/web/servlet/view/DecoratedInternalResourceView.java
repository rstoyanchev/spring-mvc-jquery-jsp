package org.springframework.web.servlet.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.InternalResourceView;

/**
 * An {@InternalResourceView} that is decorated by a JSP template.
 * 
 * 
 * @author Jorge Sim&atilde;o
 * @since 22.11.2011
 * @see JspTemplateViewResolver
 * @see DecoratedJstlView
 */
public class DecoratedInternalResourceView extends InternalResourceView {
	/**
	 * Value of prefix for title key to be resolved to a localized view title.
	 *
	 * The suffix of the title key is the generated from view name,
	 * with slash characters replaced by '.'.
	 * For example, view name "account/show" is mapped to key "view.title.account.show",
	 * that in turn could be resolved to "Account Details".
	 */
	public static final String TITLE_KEY_PREFIX = "view.title.";
	
	/**
	 * Name of request-scoped attribute used to save resource URL this view.
	 * 
	 * Should match the variable name used in template view in includes,
	 * such as: <pre>{@code <jsp:include page=${viewUtl} />}</pre>
	 */
	public static final String VIEW_URL_ATTR = "viewUrl";

	/**
	 * Name of request-scoped attribute used to save name of this view.
	 */
	public static final String VIEW_NAME_ATTR = "viewName";
	
	/**
	 * Name of request-scoped attribute used to save this view title key.
	 */
	public static final String TITLE_ATTR = "title";
	
	
	/**
	 * URL for resource of Template view.
	 */
	protected String templatePath;
	
	/**
	 * The {@link JspTemplateViewResolver} that created this view.
	 */
	protected JspTemplateViewResolver viewResolver;

	
	/**
	 * Create instance of DecoratedInternalResourceView.
	 *
	 */
	public DecoratedInternalResourceView() {
	}

	/**
	 * Create instance of DecoratedInternalResourceView.
	 *
	 * @param url the URL for the view resource
	 */
	public DecoratedInternalResourceView(String url) {
		super(url);
	}
	
	
	
	/**
	 * Get the value of templatePath.
	 *
	 * @return the templatePath
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * Set the value of templatePath.
	 *
	 * @param templatePath the templatePath to set
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	
	/**
	 * Get the value of viewResolver.
	 *
	 * @return the viewResolver
	 */
	public JspTemplateViewResolver getViewResolver() {
		return viewResolver;
	}

	/**
	 * Set the value of viewResolver.
	 *
	 * @param viewResolver the viewResolver to set
	 */
	public void setViewResolver(JspTemplateViewResolver viewResolver) {
		this.viewResolver = viewResolver;
	}
	
	@Override
	protected void renderMergedOutputModel(
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (useTemplate(request)) {
			model.put(VIEW_URL_ATTR, getUrl());
			model.put(VIEW_NAME_ATTR, getBeanName());
			model.put(TITLE_ATTR, getTitleKey(getBeanName()));
			System.out.println(this + ".render: page:" + getUrl() + " template:" + templatePath);
		}
		super.renderMergedOutputModel(model, request, response);
	}
	
	@Override
	protected String prepareForRendering(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (viewResolver.isDynamicTemplates()) {
			String templatePath = selectTemplate(request);
			if (templatePath!=null) {
				return templatePath;
			} else {
				return super.prepareForRendering(request, response);
			}
		} else {
			if (useTemplate(request)) {
				return templatePath;
			} else {
				return super.prepareForRendering(request, response);
			}
		}
	}
	
	/**
	 * Check if the view should be decorated.
	 * @param request the HttpServletRequest
	 * @return true, if the view should be decorated; false, otherwise.
	 */
	protected boolean useTemplate(HttpServletRequest request) {
		String layoutParam = viewResolver.getLayoutParam();
		String cancelLayoutParamValue = viewResolver.getCancelLayoutParamValue();
		return cancelLayoutParamValue==null || !cancelLayoutParamValue.equals(request.getParameter(layoutParam));
	}

	
	/**
	 * Select template to decorate view for the request.
	 *
	 * Algorithm is as follow:
	 * 1) if request parameter {#layoutParam} is specified:
	 *   1.1) if value is equals to {@layoutParamValue} no decoration is performed.
	 *   1.2) otherwise, the value of the parameter is taken as a template name
	 * 2) otherwise, the value of property {#templatePath} is used
	 * 
	 * @param request the HttpServletRequest
	 * @return the template path to use for decoration of the view for this request;
	 * or null, if no decoration should be performed. 
	 */
	protected String selectTemplate(HttpServletRequest request) {
		String layoutParam = viewResolver.getLayoutParam();
		String cancelLayoutParamValue = viewResolver.getCancelLayoutParamValue();
		String requestLayoutParamValue = request.getParameter(layoutParam);
		if (requestLayoutParamValue==null) {
			return templatePath;
		}
		if (cancelLayoutParamValue!=null && cancelLayoutParamValue.equals(requestLayoutParamValue)) {
			return null;
		}
		return viewResolver.buildUrl(requestLayoutParamValue);
	}

	/**
	 * Get the message key to use for the title of a view.
	 * In current implementation, for a view named "account/view" the
	 * title key "view.title.account.view" is returned.
	 * 
	 * @param viewName the unprocessed view name String as returned by controller
	 * @return the title message key
	 */
	protected String getTitleKey(String viewName) {
		return TITLE_KEY_PREFIX + preprocessViewName(viewName).replace('/', '.');
	}
	
	/**
	 * Extract view name from String value returned by web controller handler method.
	 * 
	 * @param viewName the unprocessed view name String as returned by controller
	 * @return the processed view name.
	 */
	protected String preprocessViewName(String viewName) {
		int i= viewName.indexOf('?');
		return i>0 ? viewName.substring(0,i) : viewName;
	}

	@Override
	public String toString() {
		return super.toString() + " templatePath:" + templatePath;
	}
}

