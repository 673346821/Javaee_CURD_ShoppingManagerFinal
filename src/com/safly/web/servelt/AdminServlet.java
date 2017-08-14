package com.safly.web.servelt;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.safly.domain.Category;
import com.safly.domain.Order;
import com.safly.service.AdminService;
import com.safly.service.impl.AdminServiceImpl;
import com.safly.utils.BeanFactory;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
	public void findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminService service = new AdminServiceImpl();
		List<Category> categories = service.findAllCategory();
		
		Gson gson = new Gson();
		String json = gson.toJson(categories);
		response.setContentType("text/html;charset=UTF-8");
		System.out.println("json---"+json);
		response.getWriter().write(json);;
	}
	
	public void findAllOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminService service = new AdminServiceImpl();
		List<Order> Orders = service.findAllOrders();
		request.setAttribute("orderList", Orders);
		request.getRequestDispatcher("/admin/order/list.jsp").forward(request, response);
		
	}
	
	
	
	public void findOrderByOid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String oid = request.getParameter("oid");
		System.out.println("oid------"+oid);
		
//		AdminService service = new AdminServiceImpl();
		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		
		List<Map<String, Object>>  lists = service.findOrderByOid(oid);
		Gson gson = new Gson();
		String json = gson.toJson(lists);
		System.out.println(json);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(json);
		
		
	}

}
