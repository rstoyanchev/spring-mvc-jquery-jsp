package org.springframework.web.servlet.view;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.ClassUtils;

/**
 * Class {@code JspTemplateViewResolver} is a ViewResolver implementation that supports automatic decoration
 * of JSP views with a view template.
 * JspTemplateViewResolver can be used in Spring MVC projects based on the JSP rendering-technology as a drop-in
 * replacement to {@link InternalResourceViewResolver} and {@link org.springframework.web.servlet.view.tiles.TilesViewResolver}. 
 * 
 * <p>{@code JspTemplateViewResolver} extends {@link InternalResourceViewResolver}, so the features implemented by its parent class,
 * such as view logical name resolution based on properties {@link #prefix} and {@link #suffix}, are also available.
 * 
 * {@code JspTemplateViewResolver} differs from  {@link org.springframework.web.servlet.view.tiles.TilesViewResolver} in that it does not require a 
 * third-party dependency on Tiles and does not require the use of external view description files (e.g. {@code tiles.xml})
 * Additionally, JspTemplateViewResolver does not makes use of dedicated TagLibs to define or use templates.
 * The template view uses the JSP standard tag {@code <jsp:include >} and a Java or EL expression to include page specific content.
 * JSTL can optionally be used to support for page title resolution and i18n.
 *
 * An example basic configuration in a Spring beans XML file is shown below:
 *
 * <pre>{@code
 * <bean class="org.springframework.web.servlet.view.JspTemplateViewResolver">
 *   <constructor-arg value="layout/standard"/>
 *   <property name="prefix" value="/WEB-INF/views/" />
 *   <property name="suffix" value=".jsp" />
 * </bean>
 * }</pre>
 * 
 * , or alternatively:
 *
 * <pre>{@code
 * <bean class="org.springframework.web.servlet.view.JspTemplateViewResolver">
 *   <property name="defaultTemplateName" value="layout/standard"/>
 *   <property name="prefix" value="/WEB-INF/views/" />
 *   <property name="suffix" value=".jsp" />
 * </bean>
 * }</pre>
 * 
 * With the above setting the template resource file is located in path:  
 * {@code /WEB-INF/views/layout/standard.jsp}
 * 
 * There are two key points to follow to write template view for {@code JspTemplateViewResolver}:
 * <ol>
 *  <li> The view main content page is inserted with: 
 *  <pre>{@code
 *  <jsp:include page="${viewUrl}" />
 *  }</pre>
 *  
 *  <li> The view title is resolved (and localized) with: 
 * <pre>{@code
 *   <fmt:setBundle basename="titles" var="titlesBundle" />
 *   <title><fmt:message key="${viewTitle}" bundle="${titlesBundle}"/></title>
 *  }</pre>
 * </ol>
 *
 * When an implementation of {@link MessageSource} is configured in available in the application context, there is no need to explicitly define
 * the property bundle with {@code <fmt:setBundle> }, since the {@link ResourceBundle} object is automatically exported.
 * 
 * Below, is an an example template JSP view that can used as starting point or blueprint for
 * application template views.
 *
 * <pre>{@code
 * <%@ page ... %>
 * <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 * <html>
 * <head>
 *	 <fmt:setBundle basename="titles" var="titlesBundle" />
 *   <title><fmt:message key="${viewTitle}" bundle="${titlesBundle}"/></title>
 *   ...
 * </head>
 * <body> 
 *   <jsp:include page="common/header.jsp" />
 *
 *   <jsp:include page="${viewUrl}" />
 *
 *   <jsp:include page="common/footer.jsp" />
 * </body>
 * </html>
 * }</pre>
 *
 * The property file for titles (named titles.properties for the example view above)
 * will typically look something like this:
 * 
 * <pre>{@code
 * #titles.properties
 * view.title.account.list=Account List
 * view.title.account.show=Account Details
 * #....
 * * }</pre>
 * 
 *
 * In addition to a default template name configured in property {@link #defaultTemplateName},
 * templates can also be selected based patterns on the view name.
 * This is done by configuring the property {@link JspTemplateViewResolver#templateNameMap}.
 * If no match is found on the {@link JspTemplateViewResolver#templateNameMap},
 * then template selection falls-back to {@link #defaultTemplateName}.
 * 
 * An example configuration setting {@link JspTemplateViewResolver#templateNameMap} is shown below: 
 * 
 *  <pre>{@code
 * <bean class="org.springframework.web.servlet.view.JspTemplateViewResolver">
 *   <property name="prefix" value="/WEB-INF/views/"/>
 *   <property name="suffix" value=".jsp"/>
 *   <property name="templateNameMap">
 *     <map>
 *       <entry key="account/.*" value="common/standard-account"/>
 *     </map>
 *	 </property>
 *   <property name="usePatterns" value="true" />
 *	 <property name="defaultTemplateName" value="common/standard"/>
 * </bean>
 * }</pre>
 * 
 * The use of pattern needs to be explicitly set, otherwise the keys in {@templateNameMap} are
 * simply looked up by view name.
 * Patterns are specified using Java regular expression.
 * (In futures versions, additional pattern languages may be supported.)
 * 
 * View decoration can be prevented on a per-request basis, by setting the
 * value of the HTTP request parameter {@link #layoutParam} equals to {@link #cancelLayoutParamValue}).
 * This is useful, for example,  to support partial-page rendering
 * driven by JavaScript+AJAX requests. 
 * With the default configuration, view decoration is canceled by setting HTTP request parameter "layout=none".
 * This can be also overridden by setting properties {@link #layoutParam} 
 * and/or {@link #cancelLayoutParamValue}.
 * 
 * Template selection can be made on a per-request basis, by setting property
 * {@link #dynamicTemplates} to {@code true}.
 * With this setting the request parameter {@link #layoutParam} is interpreted as the name
 * of the template (except when is equal to {@link #cancelLayoutParamValue}).
 * 
 * @author Jorge Sim&atilde;o (original concept, design, implementation, documentation)
 * @author Rossen Stoyanchev (design contributions, and samples)
 * @since 22.11.2011
 * @see DecoratedInternalResourceView
 * @see #setDefaultTemplateName(String)
 * @see #setLayoutParam(String)
 * @see #setCancelLayoutParamValue(String)
 * @see #setDynamicTemplates(boolean)
 * @see InternalResourceView
 *
 */
public class JspTemplateViewResolver extends InternalResourceViewResolver {
	private static final boolean jstlPresent = ClassUtils.isPresent(
			"javax.servlet.jsp.jstl.core.Config", InternalResourceViewResolver.class.getClassLoader());

	/**
	 * Default name for {@link #layoutParam} 
	 */
	public static final String LAYOUT_PARAM = "layout";

	/**
	 * Default name for {@link #cancelLayoutParamValue} 
	 */	
	public static final String CANCEL_LAYOUT = "none";

	/**
	 * Character separator between template name and view name (default '+').
	 * Controller handler methods can return string value such as 'templateName+viewName},
	 * to explicitly specify a template name in additional to the usual view name.
	 */		
	public static final String TEMPLATE_NAME_CHAR = "+";
	
	/**
	 * Map from viewNames to templates.
	 * 
	 * Recommended to be set as a {@code LinkedHashMap<String, String>()} in order to keep preserve search order.
	 * (When configured from a Spring bean file, not really possible because its not yet supported by <beans:map> or <util:map> elements).
	 */
	protected Map<String, String> templateNameMap;

	/**
	 * Cache of previously resolved template names to each view name.
	 */
	private Map<String, String> templateNameCache;

	/**
	 * Map of view name patterns as strings to {@link java.util.Pattern}
	 */
	private Map<String, Pattern> patternMap;
	
	/**
	 * Logical name of template  view.
	 */
	protected String defaultTemplateName;
	
	/**
	 * Name of HTTP request parameter to check for the per-request template names and/or cancel
	 * page template-based decoration. 
	 */
	protected String layoutParam = LAYOUT_PARAM;
	
	/**
	 * Value for the HTTP parameter requesting partial rendering. 
	 * If the value of HTTP parameter named with the value of {@link #layoutParam} is equal to {@link #cancelLayoutParamValue}
	 * then it is a partial rendering request and no template-based decoration is performed.
	 */
	protected String cancelLayoutParamValue = CANCEL_LAYOUT;
	
	/**
	 * Specifies if template selection can be done on a per-request basis. 
	 */
	protected boolean dynamicTemplates;

	/**
	 * Specifies if template name selection for views should be cached.
	 * Ignored if {@link #templateNameMap} is {@code null}.
	 */
	protected boolean cacheTemplatesNames = true;

	/**
	 * Specifies is templateNameMap entries contains patterns.
	 */
	protected boolean usePatterns;
	
	/**
	 * Specifies if Pattern objects should be cached.
	 * Ignored if {@link #templateNameMap} is {@code null}.
	 */
	protected boolean cachePatterns = true;	
	
	//
	// Constructors
	//
	
	/**
	 * Create instance of JspTemplateViewResolver.
	 *
	 * @param defaultTemplateName the logical name of default template view.
	 */
	public JspTemplateViewResolver(String defaultTemplateName) {
		this.defaultTemplateName = defaultTemplateName;
		Class<?> viewClass = jstlPresent ? DecoratedJstlView.class : DecoratedInternalResourceView.class;
		setViewClass(viewClass);
	}
	
	/**
	 * Create instance of JspTemplateViewResolver.
	 * 
	 * Default template name should be set with {@link #setDefaultTemplateName(String)}.
	 */
	public JspTemplateViewResolver() {
		this(null);
	}
	
	//
	// Property getters and setters
	//

	
	/**
	 * Get the value of templateNameMap.
	 *
	 * @return the templateNameMap
	 */
	public Map<String, String> getTemplateNameMap() {
		return templateNameMap;
	}

	/**
	 * Set the value of {@link #templateNameMap}.
	 *
	 * Recommended to be set as a LinkedHashMap<String, String>() in order to keep preserve search order.
	 * When configured from a Spring bean file, the <util:*> XML name-space can be used for this purpose.
	 * 
	 * The template map is not consulted in any of the following conditions:
	 * <ul>
	 *  <li> it is {@code null};
	 *  <li> {@link #dynamicTemplates} is {@code true}, and the request includes the layout HTTP request parameter;
	 *  <li> the name of the view contains character {@link #TEMPLATE_NAME_CHAR} ('+') 
	 *  <li> a matching cache entry is found
	 * </ul>
	 * 
	 *
	 * @param templateNameMap the value of {@link #templateNameMap} to set
	 */
	public void setTemplateNameMap(Map<String, String> templateNameMap) {
		this.templateNameMap = templateNameMap;
	}

	/**
	 * Get the value of {@link #defaultTemplateName}.
	 *
	 * @return the value of {@link #defaultTemplateName}
	 */
	public String getDefaultTemplateName() {
		return defaultTemplateName;
	}

	/**
	 * Set the value of the the default template view {@link #defaultTemplateName}.
	 * 
	 * This the name of the template that is used when the map {@link #templateNameMap} is not set,
	 * or there is no matching entry in the map.
	 * 
	 * The default template not used in the following conditions:
	 * <ul>
	 *  <li> {@link #dynamicTemplates} is {@code true}, and the request includes the layout HTTP request parameter;
	 *  <li> the name of the view contains character {@link #TEMPLATE_NAME_CHAR} ('+');
	 *  <li> a matching cache entry is found {@link #templateNameCache};
	 *  <li> a matching entry is found in {@link #templateNameMap};
	 * </ul>
	 *  
	 * @param defaultTemplateName the value of {@link #defaultTemplateName} to set
	 */
	public void setDefaultTemplateName(String defaultTemplateName) {
		this.defaultTemplateName = defaultTemplateName;
	}

	/**
	 * Get the value of {@link #layoutParam}.
	 *
	 * Name of HTTP request parameter to check for the per-request template names and/or cancel
	 * page template-based decoration. 
	 *
	 * @return the value of {@link #layoutParam}
	 */
	public String getLayoutParam() {
		return layoutParam;
	}

	
	/**
	 * Set the value of {@link #layoutParam}.
	 *
	 * Name of HTTP request parameter to check for the per-request template names and/or cancel
	 * page template-based decoration. 
	 *
	 * @param layoutParam the value of {@link #layoutParam} to set
	 */
	public void setLayoutParam(String layoutParam) {
		this.layoutParam = layoutParam;
	}

	/**
	 * Get the value of {@link #cancelLayoutParamValue}.
	 *
	 * If the value of HTTP parameter named with the value of {@link #layoutParam} is equal to {@link #cancelLayoutParamValue}
	 * no template-based decoration is performed. 
	 * This is useful to support partial page rendering, or simply see page markup without the template decorations.
	 * 
	 * @return the value of {@link #cancelLayoutParamValue}
	 */
	public String getCancelLayoutParamValue() {
		return cancelLayoutParamValue;
	}

	/**
	 * Set the value of {@link #cancelLayoutParamValue}.
	 *
	 * @param cancelLayoutParamValue the value of {@link #cancelLayoutParamValue} to be set
	 */
	public void setCancelLayoutParamValue(String cancelLayoutParamValue) {
		this.cancelLayoutParamValue = cancelLayoutParamValue;
	}

	/**
	 * Get the value of {@link #dynamicTemplates}.
	 * 
	 * If {@code} template selection can be done on a per-request basis, by setting the 
	 * value of HTTP request parameter {@link #layoutParam}.
	 * 
	 * @return {@code true}, if template selection can be done on a per-request basis; {@code false}, otherwise.
	 */
	public boolean isDynamicTemplates() {
		return dynamicTemplates;
	}

	/**
	 * Set the value of {@link #dynamicTemplates}.
	 *
	 * If {@code} template selection can be done on a per-request basis, by setting the 
	 * value of HTTP request parameter {@link #layoutParam}.
     *
	 * @param dynamicTemplates 	{@code true}, if template selection can be done on a per-request basis; {@code false}, otherwise.
	 */
	public void setDynamicTemplates(boolean dynamicTemplates) {
		this.dynamicTemplates = dynamicTemplates;
	}

	/**
	 * Get the value of cacheTemplateNames.
	 *
	 * @return the cacheTemplateNames
	 */
	public boolean isCacheTemplateNames() {
		return cacheTemplatesNames;
	}

	/**
	 * Set the value of {@link #cacheTemplatesNames}. 
	 * If the value is true then the mapping between view names and templates are cached.
	 * 
	 * @param cacheTemplateNames the value of cacheTemplateNames to set
	 */
	public void setCacheTemplate(boolean cacheTemplateNames) {
		this.cacheTemplatesNames = cacheTemplateNames;
	}

	/**
	 * Get the value of cachePatterns.
	 *
	 * @return the cachePatterns
	 */
	public boolean isCachePatterns() {
		return cachePatterns;
	}

	/**
	 * Set the value of {@link #cachePatterns}.
	 *
	 * If {@code true} created view name regular expression Pattern objects are cached (default).
	 * Pattern objects are created only if {@link #usePatterns} is {@code true}.
	 * 
	 * @param cachePatterns the value {@link #cachePatterns} to set
	 */
	public void setCachePatterns(boolean cachePatterns) {
		this.cachePatterns = cachePatterns;
	}
	
	/**
	 * Get the value of {@link #usePatterns}.
	 *
	 * @return {@code true}, if {@link #templateNameMap} entries contain patterns; {@code false}, otherwise.
	 */
	public boolean isUsePatterns() {
		return usePatterns;
	}

	/**
	 * Set the value of {@link #usePatterns}.
	 * 
	 * A value of {@code true} specified that  {@link #templateNameMap}  contains view name patterns.
	 * If the value is {@code false}, lookup in {@link #templateNameMap} is performed by simple map key resolution ({@code Map.get()}).
	 * If the value is {@code true}, lookup if first attempted by key resolution and then by iteration over patterns to check for matches.
	 * 
	 * @param usePatterns {@code true}, if {@link #templateNameMap} entries contain patterns; {@code false}, otherwise (default).
	 */
	public void setUsePatterns(boolean usePatterns) {
		this.usePatterns = usePatterns;
	}
	
	//
	// Other methods
	//
	
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		DecoratedInternalResourceView view = (DecoratedInternalResourceView) super.buildView(getViewName(viewName));
		String templatePath = buildUrl(getTemplateName(viewName));
		view.setTemplatePath(templatePath);
		view.setViewResolver(this);
		return (AbstractUrlBasedView)view;
	}

	/**
	 * Build resource path for the specified viewName.
	 * 
	 * (This function should be moved to {@link InternalResourceViewResolver})
	 * 
	 * @param viewName the name of the view to build the resource path
	 * @return the resource path for the view.
	 */
	protected String buildUrl(String viewName) {
		return getPrefix() + viewName + getSuffix();
	}
	
	/**
	 * Extract viewName from string returned by controller handler method.
	 * 
	 * If the string contains the character {@link #TEMPLATE_NAME_CHAR} ('+' by default),
	 * the view name is the part after that character.
	 * Otherwise, the view name is the string returned by the controller unchanged.
	 *
	 * @param viewName the string returned by controller
	 * @return the actual view name.
	 */
	protected String getViewName(String viewName) {
		int i = viewName.indexOf(TEMPLATE_NAME_CHAR);
		return i<0 ? viewName :  viewName.substring(i+1);
	}
	
	/**
	 * Get name of template to use for a view name based on {@link #templateNameMap}.
	 * 
	 * Current implementation:
	 * <ol>
	 * <li> Check if view name string returned by controller handler method 
	 *    specifies an explicit template with syntax: templateName+viewName
	 * <li> Checks if a mapping between the view name and template are been cached
	 * <li> Checks for exact match between view name and key in {@link #templateNameMap}
	 * <li> Iterates over set of entries and {@link #templateNameMap} and check for a pattern match.
	 * <li> Falls-back to  {@link #defaultTemplateName}.
	 * </ol>
	 * 
	 * Note: Although the mapping between viewNames and View objects are cached by 
	 * {@link InternalResourceViewResolver} this feature can be disabled.
	 * So the mapping between viewName and templateName is also cached by this class.
	 * 
	 * Note: This method should be protected or private, from the purely functional perspective. 
	 * However, it is made public to allow unit testing of the configuration.
	 * 
	 * @param viewName the view name to find the template name.
	 * @return the template name.
	 */
	public String getTemplateName(String viewName) {
		int i = viewName.indexOf(TEMPLATE_NAME_CHAR);
		if (i>=0) {
			return viewName.substring(0, i);
		}
		if (templateNameMap!=null) { 
			if (templateNameCache!=null) {
				String templateName = templateNameCache.get(viewName);
				if (templateName!=null) {
					return templateName;
				}
			}
			//Check template entries by simple view name
			String templateName = templateNameMap.get(viewName);
			if (templateName!=null) {
				putTemplateName(viewName, templateName);
				return templateName;
			}
			//Check template entries by pattern match
			if (usePatterns) {
				for (Map.Entry<String, String> e: templateNameMap.entrySet()) {
					if (matchesViewName(e.getKey(), viewName)) {
						templateName = e.getValue();
						break;
					}
				}
				if (templateName!=null) {
					putTemplateName(viewName, templateName);
					return templateName;
				}
			}
		}
		return defaultTemplateName;
	}

	/**
	 * Save viewName to templateName map entry.
	 * 
	 * Does nothing if caching is disabled as specified by property {@link #cacheTemplatesNames}
	 * 
	 * @param viewName the view name
	 * @param templateName the template name
	 */
	protected void putTemplateName(String viewName, String templateName) {
		if (cacheTemplatesNames) {
			if (templateNameCache==null) {
				templateNameCache = new HashMap<String, String>();
			}
			templateNameCache.put(viewName, templateName);
		}		
	}
	/**
	 * Checks if a view name pattern matches a view name.
	 * 
	 * @param key the view name pattern.
	 * @param viewName the view name to match.
	 * @return {@code true}, if there is a match; {@code false}, otherwise.
	 */
	protected boolean matchesViewName(String key, String viewName) {
		Pattern pattern;
		if (cachePatterns) {
			if (patternMap==null) {
				patternMap = new HashMap<String, Pattern>();
			}
			pattern = patternMap.get(key);
			if (pattern==null) {
				pattern = Pattern.compile(key);
				patternMap.put(key, pattern);
			}
		} else {
			pattern = Pattern.compile(key);			
		}
		//Note: Matcher is not thread safe; it can not be cached
		Matcher matcher = pattern.matcher(viewName);
		return matcher.matches();
	}
	
}
