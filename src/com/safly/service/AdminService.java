package com.safly.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.safly.dao.AdminDao;
import com.safly.domain.Category;
import com.safly.domain.Order;
import com.safly.domain.Product;

public interface AdminService {

	public List<Category> findAllCategory();


	public void saveProduct(Product product);

	public List<Order> findAllOrders() ;

	public List<Map<String, Object>> findOrderByOid(String oid);

}
