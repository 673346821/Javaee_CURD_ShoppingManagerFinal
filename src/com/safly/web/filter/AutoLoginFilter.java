package com.safly.web.filter;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.safly.domain.User;
import com.safly.service.UserService;



public class AutoLoginFilter implements Filter{
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		User user = (User) req.getSession().getAttribute("user");
		
		if(user==null){
			String cookie_username = null;
			String cookie_password = null;
			
			Cookie[] cookies = req.getCookies();
			if(cookies!=null){
				System.out.println("cookie_username cookie_password ²»Îª¿Õ");
				for(Cookie cookie:cookies){
					if("cookie_username".equals(cookie.getName())){
						cookie_username = cookie.getValue();
					}
					if("cookie_password".equals(cookie.getName())){
						cookie_password = cookie.getValue();
					}
				}
			}else{
				System.out.println("cookie_username cookie_password null");
			}
			
			if(cookie_username!=null&&cookie_password!=null){
				UserService service = new UserService();
				try {
					user = service.login(cookie_username,cookie_password);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				req.getSession().setAttribute("user", user);
				
			}
		}
		
		
		chain.doFilter(req, response);
		
	}
	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	

	@Override
	public void destroy() {
		
	}

}
