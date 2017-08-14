package com.safly.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.safly.domain.User;
import com.safly.utils.DataSourceUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class UserDao {

	public int regist(User user) throws SQLException{
		// TODO Auto-generated method stub
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
		int update = runner.update(sql, user.getUid(),user.getUsername(),user.getPassword()
				,user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday()
				,user.getSex(),user.getState(),user.getCode());
		
		
		return update;
	}

	public void active(String acitiveCode) throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update user set state=? where code=?";
		
		runner.update(sql, 1,acitiveCode);
	}

	public Long checkUsername(String username) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql ="select count(*) from user where username=?";
		long count = (long) queryRunner.query(sql, new ScalarHandler(),username);
		// TODO Auto-generated method stub
		return count;
	}

	public User login(String username, String password) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from user where username=? and password=?";
		return runner.query(sql, new BeanHandler<User>(User.class), username,password);
	}
	
	
}
