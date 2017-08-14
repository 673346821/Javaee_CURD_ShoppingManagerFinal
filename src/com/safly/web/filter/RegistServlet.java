package com.safly.web.filter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.safly.domain.User;
import com.safly.service.UserService;
import com.safly.utils.CommonUtils;
import com.safly.utils.MailUtils;

/**
 * Servlet implementation class RegistServlet
 */
public class RegistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		Map<String, String[]> parameterMap = request.getParameterMap();

		for (String key : parameterMap.keySet()) {
			System.out.println(key+""+parameterMap.get(key));
		}
		
		User user = new User();
		
		try {
			//String转为Date
			ConvertUtils.register(new Converter() {
				
				@Override
				public Object convert(Class arg0, Object arg1) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date parse = null;
					try {
						parse = format.parse(arg1.toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// TODO Auto-generated method stub
					return parse;
				}
			}, Date.class);
			BeanUtils.populate(user, parameterMap);
			
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		user.setUid(CommonUtils.getUUid());
		user.setTelephone(null);
		user.setState(0);
		String activeCode = CommonUtils.getUUid();
		user.setCode(activeCode);
		
		UserService service = new UserService();
		
		boolean isRegistSuccess = service.regist(user);
		
		if (isRegistSuccess) {
			String emailMsg = "恭喜您注册成功，请点击下面的连接进行激活账户"
					+ "<a href='http://localhost:8080/WebShop/active?activeCode="+activeCode+"'>"
							+ "http://localhost:8080/WebShop/active?activeCode="+activeCode+"</a>";
			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			
			response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
			
		}else{
			response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
		}
		
	}

}










