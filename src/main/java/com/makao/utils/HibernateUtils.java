package com.makao.utils;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.makao.dao.impl.VendorDaoImpl;
import com.makao.entity.Vendor;

public class HibernateUtils {
	private static final Logger logger = Logger.getLogger(HibernateUtils.class);
	private static SessionFactory sessionFactory=null;
	static{
		Configuration cfg = new Configuration().configure("applicationContext.xml");
		sessionFactory=cfg.buildSessionFactory();
	}
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtils.sessionFactory = sessionFactory;
	}
	public static Session openSession()
	{
		return sessionFactory.openSession();
	}
	
	public static Vendor getById(int id) {
		Session session = null;
		Transaction tx = null;
		Vendor res = null;
		logger.info("HibernateUtils getById,"+id);
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = (Vendor) session.get(Vendor.class, id);
			tx.commit();// 提交事务
		} catch (HibernateException e) {
			if (null != tx)
				tx.rollback();// 回滚
			logger.error(e.getMessage(), e);
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
		return res;
	}
}
