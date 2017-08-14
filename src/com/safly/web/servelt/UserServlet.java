package com.safly.web.servelt;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.safly.domain.User;
import com.safly.service.UserService;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
				String username = request.getParameter("username");
				String password = request.getParameter("password");

				System.out.println(username+"---user pass--"+password);
				//password = MD5Utils.md5(password);

				UserService service = new UserService();
				User user = null;
				try {
					user = service.login(username,password);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				if(user!=null){
					String autoLogin = request.getParameter("autoLogin");
					if("autoLogin".equals(autoLogin)){
						System.out.println("---autoLogin---");
						Cookie cookie_username = new Cookie("cookie_username",user.getUsername());
						cookie_username.setMaxAge(10*60);
						Cookie cookie_password = new Cookie("cookie_password",user.getPassword());
						cookie_password.setMaxAge(10*60);

						response.addCookie(cookie_username);
						response.addCookie(cookie_password);

					}else{
						System.out.println("---autoLogin null---");
					}

					session.setAttribute("user", user);

					response.sendRedirect(request.getContextPath()+"/index.jsp");
				}else{
					request.setAttribute("loginError", "µÇÂ¼Ê§°Ü");
					request.getRequestDispatcher("/login.jsp").forward(request, response);
				}
				
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
