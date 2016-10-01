package com.makao.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.ISupervisorDao;
import com.makao.entity.Supervisor;
import com.makao.entity.Vendor;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class SupervisorDaoImpl implements ISupervisorDao {
	private static final Logger logger = Logger.getLogger(SupervisorDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(Supervisor supervisor) {
		Session session = null;
		Transaction tx = null;
		int res = 0;// 返回0表示失败，成功返回插入的id
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = (int)session.save(supervisor);// 保存用户
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

	@Override
	public Supervisor getById(int id) {
		Session session = null;
		Transaction tx = null;
		Supervisor res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = (Supervisor) session.get(Supervisor.class, id);
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

	@Override
	public int update(Supervisor supervisor) {
		Session session = null;
		Transaction tx = null;
		int res = 0;// 返回0表示成功，1表示失败
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.update(supervisor);// 保存用户
			tx.commit();// 提交事务
		} catch (HibernateException e) {
			if (null != tx)
				tx.rollback();// 回滚
			res = 1;
			logger.error(e.getMessage(), e);
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
		return res;
	}

	@Override
	public List<Supervisor> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Supervisor queryByName(String userName) {
		Session session = null;
		Transaction tx = null;
		Supervisor res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = (Supervisor)session.createQuery("from Supervisor s where s.userName=?").setString(0, userName).uniqueResult();
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

	@Override
	public void testor() {
		// TODO Auto-generated method stub

	}

	@Override
	public int deleteById(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
