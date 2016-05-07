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

import com.makao.dao.IAreaDao;
import com.makao.entity.Area;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class AreaDaoImpl implements IAreaDao {
	private static final Logger logger = Logger.getLogger(AreaDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(Area area) {
		Session session = null;
		Transaction tx = null;
		int res = 0;// 返回0表示成功，1表示失败
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.save(area);// 保存用户
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
	public Area getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Area area) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Area> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Area> queryByName(String name) {
		// TODO Auto-generated method stub
		return null;
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
