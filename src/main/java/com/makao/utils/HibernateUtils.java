package com.makao.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
	private static SessionFactory sessionFactory=null;
	static{
		Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
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
}
