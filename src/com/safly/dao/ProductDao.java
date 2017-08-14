package com.safly.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.safly.domain.Category;
import com.safly.domain.Order;
import com.safly.domain.OrderItem;
import com.safly.domain.Product;
import com.safly.utils.DataSourceUtils;

public class ProductDao {

	public List<Product> findHotProduct() throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where is_hot=? limit ?,?";
		
		
		return queryRunner.query(sql, new BeanListHandler<Product>(Product.class), 1,0,9);
	}

	public List<Product> findNewProducts() throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product order by pdate desc limit ?,?";
		
		
		return queryRunner.query(sql, new BeanListHandler<Product>(Product.class), 0,9);
	}

	public List<Category> findAllCategory() throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
       String sql = "select * from category";
		
		
		return queryRunner.query(sql, new BeanListHandler<Category>(Category.class)); 
	}

	public int getCount(String cid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		// TODO Auto-generated method stub
		String sql = "select count(*) from product where cid=?";
		Long query = (Long) queryRunner.query(sql, new ScalarHandler(),cid);
		return query.intValue();
	}

	public List<Product> findProductByPage(String cid, int index,
			int currentCount) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where cid=? limit ?,?";
		List<Product> query = queryRunner.query(sql, new BeanListHandler<Product>(Product.class), cid,index,currentCount);
		return query;
	}

	public Product findProductByPid(String pid) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where pid=?";
		Product query = queryRunner.query(sql, new BeanHandler<Product>(Product.class), pid);
		
		return query;
	}

		public void addOrders(Order order) throws SQLException {
			QueryRunner runner = new QueryRunner();
			String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
			Connection conn = DataSourceUtils.getConnection();
			runner.update(conn,sql, order.getOid(),order.getOrdertime(),order.getTotal(),order.getState(),
					order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid());
		}

		public void addOrderItem(Order order) throws SQLException {
			QueryRunner runner = new QueryRunner();
			String sql = "insert into orderitem values(?,?,?,?,?)";
			Connection conn = DataSourceUtils.getConnection();
			List<OrderItem> orderItems = order.getOrderItems();
			for(OrderItem item : orderItems){
				runner.update(conn,sql,item.getItemid(),item.getCount(),item.getSubtotal(),item.getProduct().getPid(),item.getOrder().getOid());
			}
			
			
		}

		public void updateOrderAdrr(Order order) throws SQLException {
			QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
			
			String sql = "update orders set address=?,name=?,telephone=? where oid=?";
			runner.update(sql, order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
		}

		public void updateOrderState(String r6_Order) throws SQLException {
			// TODO Auto-generated method stub
			System.out.println("--r6_Order---"+r6_Order);
			QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
			String sql = "update orders set state=? where oid=?";
			runner.update(sql, 1,r6_Order);
		}

		public List<Order> findAllOrders(String uid) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orders where uid=?";
		return runner.query(sql, new BeanListHandler<Order>(Order.class), uid);
		
		
	}
		public List<Map<String, Object>> findAllOrderItemByOid(String oid) throws SQLException {
			QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
			String sql = "select i.count,i.subtotal,p.pimage,p.pname,p.shop_price from orderitem i,product p where i.pid=p.pid and i.oid=?";
			List<Map<String, Object>> mapList = runner.query(sql, new MapListHandler(), oid);
			return mapList;
		}
	

}
