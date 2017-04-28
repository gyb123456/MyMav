package com.aaa.ssh.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aaa.ssh.dao.UserDao;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;
	
	/**
	 *    sfdsafdsafs fdd
	 */
//	@Transactional
	public void test(){
		userDao.gettest();
	}
}
