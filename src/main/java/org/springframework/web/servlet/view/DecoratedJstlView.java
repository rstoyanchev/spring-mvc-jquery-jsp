package org.springframework.web.servlet.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.JstlUtils;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.JstlView;

/**
 * Specialization of {@link DecoratedInternalResourceView} for JSTL pages,
 * i.e. JSP pages that use the JSP Standard Tag Library.
 *
 * Implementation based on {@link JstlView}, except that {@link DecoratedInternalResourceView} is
 * used as parent class in stead of {@link InternalResourceView}.
 * 
 * @author Jorge Sim&atilde;o (adaptation)
 * @author Juergen Hoeller (implemented {@link JstlView})
 * @since 22.11.2011
 * @see JstlView
 */
public class DecoratedJstlView extends DecoratedInternalResourceView {
	private MessageSource messageSource;

	/**
	 * Constructor for use as a bean.
	 * @see #setUrl
	 */
	public DecoratedJstlView() {
	}

	/**
	 * Create a new JstlView with the given URL.
	 * @param url the URL to forward to
	 */
	public DecoratedJstlView(String url) {
		super(url);
	}

	/**
	 * Create a new JstlView with the given URL.
	 * @param url the URL to forward to
	 * @param messageSource the MessageSource to expose to JSTL tags
	 * (will be wrapped with a JSTL-aware MessageSource that is aware of JSTL's
	 * <code>javax.servlet.jsp.jstl.fmt.localizationContext</code> context-param)
	 * @see JstlUtils#getJstlAwareMessageSource
	 */
	public DecoratedJstlView(String url, MessageSource messageSource) {
		this(url);
		this.messageSource = messageSource;
	}


	/**
	 * Wraps the MessageSource with a JSTL-aware MessageSource that is aware
	 * of JSTL's <code>javax.servlet.jsp.jstl.fmt.localizationContext</code>
	 * context-param.
	 * @see JstlUtils#getJstlAwareMessageSource
	 */
	@Override
	protected void initServletContext(ServletContext servletContext) {
		if (this.messageSource != null) {
			this.messageSource = JstlUtils.getJstlAwareMessageSource(servletContext, this.messageSource);
		}
		super.initServletContext(servletContext);
	}

	/**
	 * Exposes a JSTL LocalizationContext for Spring's locale and MessageSource.
	 * @see JstlUtils#exposeLocalizationContext
	 */
	@Override
	protected void exposeHelpers(HttpServletRequest request) throws Exception {
		if (this.messageSource != null) {
			JstlUtils.exposeLocalizationContext(request, this.messageSource);
		}
		else {
			JstlUtils.exposeLocalizationContext(new RequestContext(request, getServletContext()));
		}
	}

}
