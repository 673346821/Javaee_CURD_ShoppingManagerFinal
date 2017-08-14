package com.safly.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.safly.dao.ProductDao;
import com.safly.domain.Category;
import com.safly.domain.Order;
import com.safly.domain.PageBean;
import com.safly.domain.Product;
import com.safly.utils.DataSourceUtils;

public class ProductService {

	public List<Product> findHotProduct() {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		List<Product> hotProducts = null;
		try {
			hotProducts = dao.findHotProduct();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hotProducts;
	}

	public List<Product> findNewProducts() {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		List<Product> newProducts = null;
		try {
			newProducts = dao.findNewProducts();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newProducts;
	}

	public List<Category> findAllCategory() {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		List<Category> categories = null;
		try {
			categories = dao.findAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return categories;
	}

	public PageBean findProductByCid(String cid,int currentPage,int currentCount) {
		// TODO Auto-generated method stub
		PageBean<Product> pageBean = new PageBean<>();
		
		pageBean.setCurrentPage(currentPage);
		pageBean.setCurrentCount(currentCount);
		
		
		ProductDao dao = new ProductDao();
		int totalCount = 0;
		try {
			totalCount = dao.getCount(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		
		int index = (currentPage-1)* currentCount;
		List<Product> list = null;
		try {
			list = dao.findProductByPage(cid,index,currentCount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageBean.setList(list);
		return pageBean;
	}

	public Product findProductByPid(String pid) {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		Product product = null;
		try {
			product = dao.findProductByPid(pid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return product;
	}

	/**
	 * 提交訂單
	 * @param order
	 */
	public void submitOrder(Order order) {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		
		try {
			DataSourceUtils.startTransaction();
			dao.addOrders(order);
			dao.addOrderItem(order);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void updateOrderAdrr(Order order) {
		ProductDao dao = new ProductDao();
		try {
			dao.updateOrderAdrr(order);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateOrderState(String r6_Order) {
		// TODO Auto-generated method stub
		ProductDao dao = new ProductDao();
		try {
			dao.updateOrderState(r6_Order);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//获得指定用户的订单集合
		public List<Order> findAllOrders(String uid) {
			ProductDao dao = new ProductDao();
			List<Order> orderList = null;
			try {
				orderList = dao.findAllOrders(uid);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return orderList;
		}

		public List<Map<String, Object>> findAllOrderItemByOid(String oid) {
		ProductDao dao = new ProductDao();
		List<Map<String, Object>> mapList = null;
		try {
			mapList = dao.findAllOrderItemByOid(oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapList;
	}
}





