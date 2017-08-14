package com.safly.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.safly.dao.AdminDao;
import com.safly.domain.Category;
import com.safly.domain.Order;
import com.safly.domain.Product;
import com.safly.service.AdminService;

public class AdminServiceImpl implements AdminService {

	@Override
	public List<Category> findAllCategory() {
		// TODO Auto-generated method stub
		AdminDao dao = new AdminDao();
		try {
			return dao.findAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void saveProduct(Product product) {
		AdminDao dao = new AdminDao();
		try {
			dao.saveProduct(product);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public List<Order> findAllOrders() {
		AdminDao dao = new AdminDao();
		try {
			return dao.findAllOrders();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> findOrderByOid(String oid) {
		AdminDao dao = new AdminDao();
		List<Map<String, Object>> list = null;
		try {
			list = dao.findOrderByOid(oid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return list;
	}

}
