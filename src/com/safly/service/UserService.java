package com.safly.service;

import java.sql.SQLException;

import com.safly.dao.UserDao;
import com.safly.domain.User;

public class UserService {

	public boolean regist(User user) {
		// TODO Auto-generated method stub
		UserDao dao = new UserDao();
		int count = 0;
		try {
			count = dao.regist(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count>0?true:false;
	}

	/**
	 * ÓÊÏäÓÃ»§¼¤»î
	 * @param acitiveCode
	 */
	public void active(String acitiveCode) {
		// TODO Auto-generated method stub
		
		UserDao dao = new UserDao();
		try {
			dao.active(acitiveCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean checkUsername(String username) {
		UserDao userDao = new UserDao();
		Long checkUsername = 0l;
		try {
			checkUsername = userDao.checkUsername(username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return checkUsername>0?true:false;
	}
	public User login(String username, String password) throws SQLException {
		UserDao dao = new UserDao();
		return dao.login(username,password);
	}


	
	

}
