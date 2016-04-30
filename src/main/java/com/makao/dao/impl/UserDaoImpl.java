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

import com.makao.controller.UserController;
import com.makao.dao.IUserDao;
import com.makao.entity.User;

@Repository
@Transactional
public class UserDaoImpl implements IUserDao {
	/** 日志实例 */
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory; 

	@Override
	public int insert(User user) {
		Session session = null;  
        Transaction tx = null;
        int res = 0;//返回0表示成功，1表示失败
		try{  
            session = sessionFactory.openSession();//获取和数据库的回话  
            tx = session.beginTransaction();//事务开始  
            session.save(user);//保存用户  
            tx.commit();//提交事务
        }catch(HibernateException e){  
            if(null != tx)  
                tx.rollback();//回滚  
            res = 1;
            logger.warn(e.getMessage(), e);
        }finally{  
            if(null != session)  
                session.close();//关闭回话  
        }  
		return res;
	}

	@Override
	public User selectByPrimaryKey(Integer id) {
//		return (User) sessionFactory.getCurrentSession().createQuery(
//			    "from User u where u.id=?").setInteger(0, id)
//			    .uniqueResult();
		Session session = null;  
        Transaction tx = null;
        User res = null;
		try{  
            session = sessionFactory.openSession();//获取和数据库的回话  
            tx = session.beginTransaction();//事务开始  
            res = (User)session.get(User.class, id);
            tx.commit();//提交事务
        }catch(HibernateException e){  
            if(null != tx)  
                tx.rollback();//回滚  
            	logger.warn(e.getMessage(), e);
        }finally{  
            if(null != session)  
                session.close();//关闭回话  
        }
		return res;
	}
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		Session session = null;  
        Transaction tx = null;
        int res = 0;
		try{  
            session = sessionFactory.openSession();//获取和数据库的回话  
            tx = session.beginTransaction();//事务开始  
            User user = (User)session.get(User.class, id);
            session.delete(user);
            tx.commit();//提交事务
        }catch(HibernateException e){  
            if(null != tx)  
                tx.rollback();//回滚  
            logger.warn(e.getMessage(), e);
            res = 1;
        }finally{  
            if(null != session)  
                session.close();//关闭回话  
        }
		return res;
	}

	@Override
	public int update(User user) {
		Session session = null;  
        Transaction tx = null;
        int res = 0;
		try{  
            session = sessionFactory.openSession();//获取和数据库的回话  
            tx = session.beginTransaction();//事务开始  
            session.update(user);
            tx.commit();//提交事务
        }catch(HibernateException e){  
            if(null != tx)  
                tx.rollback();//回滚  
            logger.warn(e.getMessage(), e);
            res = 1;
        }finally{  
            if(null != session)  
                session.close();//关闭回话  
        }
		return res;
	}

	@Override
	public List<User> queryAllUser() {
		Session session = null;  
        Transaction tx = null;
        List<User> res = null;
		try{  
            session = sessionFactory.openSession();//获取和数据库的回话  
            tx = session.beginTransaction();//事务开始  
            res = session.createQuery("from User").list();
            tx.commit();//提交事务
        }catch(HibernateException e){  
            if(null != tx)  
                tx.rollback();//回滚  
            logger.warn(e.getMessage(), e);
        }finally{  
            if(null != session)  
                session.close();//关闭回话  
        }
		return res;
	}

	@Override
	public List<User> queryUserByName(String name) {
		Session session = null;  
        Transaction tx = null;
        List<User> res = null;
		try{  
            session = sessionFactory.openSession();//获取和数据库的回话  
            tx = session.beginTransaction();//事务开始  
            res = session.createQuery("from User u where u.userName like ?").setString(0, "%"+name+"%").list();
            tx.commit();//提交事务
        }catch(HibernateException e){  
            if(null != tx)  
                tx.rollback();//回滚  
            logger.warn(e.getMessage(), e);
        }finally{  
            if(null != session)  
                session.close();//关闭回话  
        }
		return res;
	}

}
