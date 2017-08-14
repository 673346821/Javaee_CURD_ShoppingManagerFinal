package com.safly.web.servelt;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.safly.domain.Cart;
import com.safly.domain.CartItem;
import com.safly.domain.Category;
import com.safly.domain.Order;
import com.safly.domain.OrderItem;
import com.safly.domain.PageBean;
import com.safly.domain.Product;
import com.safly.domain.User;
import com.safly.service.ProductService;
import com.safly.utils.CommonUtils;
import com.safly.utils.PaymentUtil;

/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * �ҵĶ���
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void myOrders(HttpServletRequest request, HttpServletResponse
				 response) throws ServletException, IOException {
			HttpSession session = request.getSession();

			User user = (User) session.getAttribute("user");
			
			ProductService service = new ProductService();
			List<Order> orderList = service.findAllOrders(user.getUid());
			if(orderList!=null){
				for(Order order : orderList){
					String oid = order.getOid();
					List<Map<String, Object>> mapList = service.findAllOrderItemByOid(oid);
					for(Map<String,Object> map : mapList){
						
						try {
							OrderItem item = new OrderItem();
							BeanUtils.populate(item, map);
							Product product = new Product();
							BeanUtils.populate(product, map);
							item.setProduct(product);
							order.getOrderItems().add(item);
						} catch (IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace();
						}
						
						
					}

				}
			}
			
			
			request.setAttribute("orderList", orderList);
			
			request.getRequestDispatcher("/order_list.jsp").forward(request, response);
			
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// protected void doGet(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// // TODO Auto-generated method stub
	// String methodName = request.getParameter("method");
	// if ("productList".equals(methodName)) {
	// productList(request, response);
	// }else if("categoryList".equals(methodName)){
	// categoryList(request, response);
	// }else if("index".equals(methodName)){
	// index(request, response);
	// }else if("productInfo".equals(methodName)){
	// productInfo(request, response);
	// }
	// }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// protected void doPost(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	// // TODO Auto-generated method stub
	// doGet(request, response);
	// }

	public void categoryList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ProductService service = new ProductService();
		List<Category> categories = service.findAllCategory();
		response.setContentType("text/html;charset=UTF-8");
		Gson gson = new Gson();
		String json = gson.toJson(categories);
		response.getWriter().write(json);
	}

	/**
	 * ��ʾ��ҳ����
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void index(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		ProductService service = new ProductService();
		List<Product> hotProducts = service.findHotProduct();

		List<Product> newProducts = service.findNewProducts();

		request.setAttribute("hotProducts", hotProducts);
		request.setAttribute("newProducts", newProducts);
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	/**
	 * ��Ʒ����ϸ��Ϣ
	 */

	public void productInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		String pid = request.getParameter("pid");
		String cid = request.getParameter("cid");
		String currentPage = request.getParameter("currentPage");

		ProductService service = new ProductService();
		Product product = service.findProductByPid(pid);
		request.setAttribute("product", product);
		request.setAttribute("cid", cid);
		request.setAttribute("currentPage", currentPage);

		// ��ÿͻ���Я��cookie---���������pids��cookie
		String pids = pid;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					pids = cookie.getValue();
					// 1-3-2 ���η�����Ʒpid��8----->8-1-3-2
					// 1-3-2 ���η�����Ʒpid��3----->3-1-2
					// 1-3-2 ���η�����Ʒpid��2----->2-1-3
					// ��pids���һ������
					String[] split = pids.split("-");// {3,1,2}
					List<String> asList = Arrays.asList(split);// [3,1,2]
					LinkedList<String> list = new LinkedList<String>(asList);// [3,1,2]
					// �жϼ������Ƿ���ڵ�ǰpid
					if (list.contains(pid)) {
						// ������ǰ�鿴��Ʒ��pid
						list.remove(pid);
						list.addFirst(pid);
					} else {
						// ��������ǰ�鿴��Ʒ��pid ֱ�ӽ���pid�ŵ�ͷ��
						list.addFirst(pid);
					}
					// ��[3,1,2]ת��3-1-2�ַ���
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < list.size() && i < 7; i++) {
						sb.append(list.get(i));
						sb.append("-");// 3-1-2-
					}
					// ȥ��3-1-2-���-
					pids = sb.substring(0, sb.length() - 1);
				}
			}
		}

		Cookie cookie_pids = new Cookie("pids", pids);
		response.addCookie(cookie_pids);

		request.getRequestDispatcher("/product_info.jsp").forward(request,
				response);

	}

	/**
	 * ������Ʒ����ȡ��Ʒ�б�
	 */
	public void productList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String cid = request.getParameter("cid");
		String currentPageStr = request.getParameter("currentPage");
		if (currentPageStr == null) {
			currentPageStr = "1";
		}
		int currentPage = Integer.parseInt(currentPageStr);
		int currentCount = 12;

		ProductService productService = new ProductService();
		PageBean PageBean = productService.findProductByCid(cid, currentPage,
				currentCount);

		request.setAttribute("pageBean", PageBean);
		request.setAttribute("cid", cid);

		// ����һ����¼��ʷ��Ʒ��Ϣ�ļ���
		List<Product> historyProductList = new ArrayList<Product>();

		// ��ÿͻ���Я�����ֽ�pids��cookie
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					String pids = cookie.getValue();// 3-2-1
					String[] split = pids.split("-");
					for (String pid : split) {
						Product pro = productService.findProductByPid(pid);
						historyProductList.add(pro);
					}
				}
			}
		}

		// ����ʷ��¼�ļ��Ϸŵ�����
		request.setAttribute("historyProductList", historyProductList);

		request.getRequestDispatcher("/product_list.jsp").forward(request,
				response);
	}

	/**
	 * �h��ُ��܇�ėlĿ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void delProFromCart(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String pid = request.getParameter("pid");
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart!=null) {
			Map<String, CartItem> cartItems = cart.getCartItems();
			double total = cart.getTotal();
			cart.setTotal(total-cartItems.get(pid).getSubtotal());
			cartItems.remove(pid);
			
		}
		
		session.setAttribute("cart", cart);
		
		
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
		
	}

	
	/**
	 * ��չ��ﳵ
	 */
	public void clearCart(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		session.removeAttribute("cart");
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	/**
	 * ���ﳵ���
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addProductToCart(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

		ProductService service = new ProductService();

		// ���Ҫ�ŵ����ﳵ����Ʒ��pid
		String pid = request.getParameter("pid");
		// ��ø���Ʒ�Ĺ�������
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));

		// ���product����
		Product product = service.findProductByPid(pid);
		// ����С��
		double subtotal = product.getShop_price() * buyNum;
		// ��װCartItem
		CartItem item = new CartItem();
		item.setProduct(product);
		item.setBuyNum(buyNum);
		item.setSubtotal(subtotal);

		// ��ù��ﳵ---�ж��Ƿ���session���Ѿ����ڹ��ﳵ
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
		}

		// ��������ŵ�����---key��pid
		// ���жϹ��ﳵ���Ƿ��ѽ������˹������� ----- �ж�key�Ƿ��Ѿ�����
		// ������ﳵ���Ѿ����ڸ���Ʒ----���������������ԭ�е�����������Ӳ���
		Map<String, CartItem> cartItems = cart.getCartItems();

		double newsubtotal = 0.0;

		if (cartItems.containsKey(pid)) {
			// ȡ��ԭ����Ʒ������
			CartItem cartItem = cartItems.get(pid);
			int oldBuyNum = cartItem.getBuyNum();
			oldBuyNum += buyNum;
			cartItem.setBuyNum(oldBuyNum);
			cart.setCartItems(cartItems);
			// �޸�С��
			// ԭ������Ʒ��С��
			double oldsubtotal = cartItem.getSubtotal();
			// �������Ʒ��С��
			newsubtotal = buyNum * product.getShop_price();
			cartItem.setSubtotal(oldsubtotal + newsubtotal);

		} else {
			// �������û�и���Ʒ
			cart.getCartItems().put(product.getPid(), item);
			newsubtotal = buyNum * product.getShop_price();
		}

		// �����ܼ�
		double total = cart.getTotal() + newsubtotal;
		cart.setTotal(total);

		// �����ٴη���session
		session.setAttribute("cart", cart);

		// ֱ����ת�����ﳵҳ��
		response.sendRedirect(request.getContextPath() + "/cart.jsp");

	}
	

	/**
	 * �ύ����
	 */
	public void submitOrder(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		Order order = new Order();
		String oid = CommonUtils.getUUid();
		order.setOid(oid);
		
		order.setOrdertime(new Date());
		
		
		Cart cart = (Cart) session.getAttribute("cart");
		double total = cart.getTotal();
		order.setTotal(total);
		
		order.setState(0);
		
		order.setAddress(null);
		order.setName(null);
		order.setTelephone(null);
		order.setUser(user);
		
		Map<String, CartItem> cartItems = cart.getCartItems();
		
		for (Map.Entry<String, CartItem> entry : cartItems.entrySet()) {
			CartItem cartItem = entry.getValue();
			OrderItem orderItem = new OrderItem();
			orderItem.setItemid(CommonUtils.getUUid());
			orderItem.setCount(cartItem.getBuyNum());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(order);
			
			order.getOrderItems().add(orderItem);
		
		}
		
		
		ProductService service = new ProductService();
		service.submitOrder(order);
		
		//ҳ����ת
		session.setAttribute("order", order);
		
		response.sendRedirect(request.getContextPath()+"/order_info.jsp");
		
		
	}
	
	/**
	 * ȷ�϶���
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void confirmOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Map<String, String[]> properties = request.getParameterMap();
		Order order = new Order();
		try {
			BeanUtils.populate(order, properties);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		ProductService service = new ProductService();
		service.updateOrderAdrr(order);

		//.......

		String orderid = request.getParameter("oid");
		//String money = order.getTotal()+"";��
		String money = "0.01";//��
		String pd_FrpId = request.getParameter("pd_FrpId");

		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = orderid;
		String p3_Amt = money;
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
				"keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);


		String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId="+pd_FrpId+
				"&p0_Cmd="+p0_Cmd+
				"&p1_MerId="+p1_MerId+
				"&p2_Order="+p2_Order+
				"&p3_Amt="+p3_Amt+
				"&p4_Cur="+p4_Cur+
				"&p5_Pid="+p5_Pid+
				"&p6_Pcat="+p6_Pcat+
				"&p7_Pdesc="+p7_Pdesc+
				"&p8_Url="+p8_Url+
				"&p9_SAF="+p9_SAF+
				"&pa_MP="+pa_MP+
				"&pr_NeedResponse="+pr_NeedResponse+
				"&hmac="+hmac;

		response.sendRedirect(url);


	}

	

}
