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
	 * 我的订单
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
	 * 显示首页功能
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
	 * 商品的详细信息
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

		// 获得客户端携带cookie---获得名字是pids的cookie
		String pids = pid;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					pids = cookie.getValue();
					// 1-3-2 本次访问商品pid是8----->8-1-3-2
					// 1-3-2 本次访问商品pid是3----->3-1-2
					// 1-3-2 本次访问商品pid是2----->2-1-3
					// 将pids拆成一个数组
					String[] split = pids.split("-");// {3,1,2}
					List<String> asList = Arrays.asList(split);// [3,1,2]
					LinkedList<String> list = new LinkedList<String>(asList);// [3,1,2]
					// 判断集合中是否存在当前pid
					if (list.contains(pid)) {
						// 包含当前查看商品的pid
						list.remove(pid);
						list.addFirst(pid);
					} else {
						// 不包含当前查看商品的pid 直接将该pid放到头上
						list.addFirst(pid);
					}
					// 将[3,1,2]转成3-1-2字符串
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < list.size() && i < 7; i++) {
						sb.append(list.get(i));
						sb.append("-");// 3-1-2-
					}
					// 去掉3-1-2-后的-
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
	 * 根据商品类别获取商品列表
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

		// 定义一个记录历史商品信息的集合
		List<Product> historyProductList = new ArrayList<Product>();

		// 获得客户端携带名字叫pids的cookie
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

		// 将历史记录的集合放到域中
		request.setAttribute("historyProductList", historyProductList);

		request.getRequestDispatcher("/product_list.jsp").forward(request,
				response);
	}

	/**
	 * 刪除購物車的條目
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
	 * 清空购物车
	 */
	public void clearCart(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		session.removeAttribute("cart");
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	/**
	 * 购物车添加
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

		// 获得要放到购物车的商品的pid
		String pid = request.getParameter("pid");
		// 获得该商品的购买数量
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));

		// 获得product对象
		Product product = service.findProductByPid(pid);
		// 计算小计
		double subtotal = product.getShop_price() * buyNum;
		// 封装CartItem
		CartItem item = new CartItem();
		item.setProduct(product);
		item.setBuyNum(buyNum);
		item.setSubtotal(subtotal);

		// 获得购物车---判断是否在session中已经存在购物车
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
		}

		// 将购物项放到车中---key是pid
		// 先判断购物车中是否已将包含此购物项了 ----- 判断key是否已经存在
		// 如果购物车中已经存在该商品----将现在买的数量与原有的数量进行相加操作
		Map<String, CartItem> cartItems = cart.getCartItems();

		double newsubtotal = 0.0;

		if (cartItems.containsKey(pid)) {
			// 取出原有商品的数量
			CartItem cartItem = cartItems.get(pid);
			int oldBuyNum = cartItem.getBuyNum();
			oldBuyNum += buyNum;
			cartItem.setBuyNum(oldBuyNum);
			cart.setCartItems(cartItems);
			// 修改小计
			// 原来该商品的小计
			double oldsubtotal = cartItem.getSubtotal();
			// 新买的商品的小计
			newsubtotal = buyNum * product.getShop_price();
			cartItem.setSubtotal(oldsubtotal + newsubtotal);

		} else {
			// 如果车中没有该商品
			cart.getCartItems().put(product.getPid(), item);
			newsubtotal = buyNum * product.getShop_price();
		}

		// 计算总计
		double total = cart.getTotal() + newsubtotal;
		cart.setTotal(total);

		// 将车再次访问session
		session.setAttribute("cart", cart);

		// 直接跳转到购物车页面
		response.sendRedirect(request.getContextPath() + "/cart.jsp");

	}
	

	/**
	 * 提交订单
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
		
		//页面跳转
		session.setAttribute("order", order);
		
		response.sendRedirect(request.getContextPath()+"/order_info.jsp");
		
		
	}
	
	/**
	 * 确认订单
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
		//String money = order.getTotal()+"";
		String money = "0.01";//
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
