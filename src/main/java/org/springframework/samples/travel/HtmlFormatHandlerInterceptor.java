package org.springframework.samples.travel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class HtmlFormatHandlerInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if ("nolayout".equals(request.getParameter("htmlFormat"))) {
			modelAndView.setViewName(modelAndView.getViewName() + "Content");
		}
	}

}
