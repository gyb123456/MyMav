package com.aaa.ssh.dao;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
//	@Transactional
	public void gettest(){
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery("select * from user");
		List<Map> list = q.list();
		System.out.println("list:"+list);
		System.out.println("ssssssss");
	}
	
}
