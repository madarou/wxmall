package com.makao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.makao.entity.Testor;

import junit.framework.TestCase;

public class HiberXmlTest extends TestCase {
	private SessionFactory sessionFactory;
	protected void setUp() throws Exception {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }
 
    protected void tearDown() throws Exception {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
    
    public void testTableCreation(){
    	Testor um = new Testor();
        um.setName("name1");
        Session s = sessionFactory.openSession();
        Transaction t = s.beginTransaction();
        s.save(um);
        t.commit();
    }
}
