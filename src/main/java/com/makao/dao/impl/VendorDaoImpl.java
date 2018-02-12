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

import com.makao.dao.IVendorDao;
import com.makao.entity.Area;
import com.makao.entity.City;
import com.makao.entity.User;
import com.makao.entity.Vendor;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class VendorDaoImpl implements IVendorDao {
	private static final Logger logger = Logger.getLogger(VendorDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(Vendor vendor) {
		Session session = null;
		Transaction tx = null;
		int res = 0;// 返回0表示失败
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = (int) session.save(vendor);// 保存用户
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
	public Vendor getById(int id) {
		Session session = null;
		Transaction tx = null;
		Vendor res = null;
		logger.info("VendorDaoImp getById,"+id);
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

	@Override
	public int update(Vendor vendor) {
		Session session = null;
		Transaction tx = null;
		int res = 0;// 返回0表示成功，1表示失败
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.update(vendor);// 保存用户
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
	public List<Vendor> queryAll() {
		Session session = null;
		Transaction tx = null;
		List<Vendor> res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = session.createQuery("from Vendor v where v.isDelete='no'").list();
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
	public List<Vendor> queryByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void testor() {
		// TODO Auto-generated method stub

	}

	@Override
	public int deleteById(int id) {
		Session session = null;
		Transaction tx = null;
		int res = 0;// 返回0表示成功，1表示失败
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			//session.createQuery("delete Vendor v where v.id=?").setInteger(0, id).executeUpdate();
			//这里不真正删除
			session.createQuery("update Vendor v set v.isDelete='yes' where v.id=?").setInteger(0, id).executeUpdate();
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
	public List<Vendor> getByAreaId(int areaId) {
		Session session = null;
		Transaction tx = null;
		List<Vendor> res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = session.createQuery("from Vendor v where v.isDelete='no' and v.areaId=?").setInteger(0, areaId).list();
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
	public Vendor queryVendorByName(String name) {
		Session session = null;
		Transaction tx = null;
		Vendor res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = (Vendor) session.createQuery("from Vendor v where v.userName=? and v.isDelete='no'").setString(0, name).uniqueResult();
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
