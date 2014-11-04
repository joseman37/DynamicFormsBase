package mx.jmgs.dynamicformsbase.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.jmgs.dynamicformsbase.singleton.JsfConfiguration;

/**
 * Dynamic form filter that redirect the request to the corresponding URL.
 * 
 * @author jgonzalez
 * 
 */
@WebFilter(filterName="dynamicFormFilter", urlPatterns=JsfConfiguration.DYNAMIC_URL + "/*")
public class DynamicFormFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;

		String urlRequet = req.getRequestURI();
		System.out.println("FILTER before request: " + urlRequet);
		urlRequet = urlRequet.substring(req.getContextPath().length()); 
		// TODO
		System.out.println("FILTER after request: " + urlRequet);
		System.out.println();
		
		if (urlRequet.equals(JsfConfiguration.DYNAMIC_URL)) {
			// dynamic form in required, so redirect to index.
			HttpServletResponse res = (HttpServletResponse) response;
			res.sendRedirect(req.getContextPath() + "/index.xhtml");
		} else {
			urlRequet = urlRequet.substring(JsfConfiguration.DYNAMIC_URL.length()+1);
			
			System.out.println("FILTER set request attribute: " + urlRequet);

			req.setAttribute("formName", urlRequet);
			
			RequestDispatcher rd = req.getRequestDispatcher(JsfConfiguration.FORM_URL);
			rd.forward(request, response);
			
			// so just continue request.
			// chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Not implemented

	}

	@Override
	public void destroy() {
		// Not implemented

	}

}
