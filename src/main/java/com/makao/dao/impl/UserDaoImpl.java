package com.makao.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.IUserDao;
import com.makao.entity.Testor;
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
		int res = 0;// 返回0表示成功，1表示失败
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.save(user);// 保存用户
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
	public User getById(int id) {
		// return (User) sessionFactory.getCurrentSession().createQuery(
		// "from User u where u.id=?").setInteger(0, id)
		// .uniqueResult();
		Session session = null;
		Transaction tx = null;
		User res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = (User) session.get(User.class, id);
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
	public int deleteById(int id) {
		Session session = null;
		Transaction tx = null;
		int res = 0;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			User user = (User) session.get(User.class, id);
			session.delete(user);
			tx.commit();// 提交事务
		} catch (HibernateException e) {
			if (null != tx)
				tx.rollback();// 回滚
			logger.error(e.getMessage(), e);
			res = 1;
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
		return res;
	}

	@Override
	public int update(User user) {
		Session session = null;
		Transaction tx = null;
		int res = 0;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.update(user);
			tx.commit();// 提交事务
		} catch (HibernateException e) {
			if (null != tx)
				tx.rollback();// 回滚
			logger.error(e.getMessage(), e);
			res = 1;
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
		return res;
	}

	@Override
	public List<User> queryAll() {
		Session session = null;
		Transaction tx = null;
		List<User> res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = session.createQuery("from User").list();
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
	public List<User> queryByName(String name) {
		Session session = null;
		Transaction tx = null;
		List<User> res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = session.createQuery("from User u where u.userName like ?")
					.setString(0, "%" + name + "%").list();
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

	public void creatNewTable(String tableName) {
		String sql = "CREATE TABLE IF NOT EXISTS `"
				+ tableName
				+ "` (`id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',`name` varchar(10) COMMENT 'name',PRIMARY KEY (`id`))";
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.doWork(
			// 定义一个匿名类，实现了Work接口
			new Work() {
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						// ResultSetMetaData metadata = rs.getMetaData();
						// 经由过程JDBC API执行SQL语句
						ps = connection.prepareStatement(sql);
						ps.execute();
					} finally {
						doClose(ps);
					}
				}
			});
			tx.commit(); // 使用 Hibernate事务处理边界
		} catch (HibernateException e) {
			if (null != tx)
				tx.rollback();// 回滚
			logger.error(e.getMessage(), e);
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
	}
	
	public void insertProduct(Testor testor, String tableName){
		String sql = "INSERT INTO `"+tableName+"` (`name`) VALUES (?)";
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.doWork(
			// 定义一个匿名类，实现了Work接口
			new Work() {
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						// ResultSetMetaData metadata = rs.getMetaData();
						// 经由过程JDBC API执行SQL语句
						ps = connection.prepareStatement(sql);
						ps.setString(1, testor.getName());
						ps.executeUpdate();
					}catch(SQLException e){
						logger.error(e.getMessage(), e);
						throw e;
					} finally {
						doClose(ps);
					}
				}
			});
			tx.commit(); // 使用 Hibernate事务处理边界
		} catch (HibernateException e) {
			if (null != tx)
				tx.rollback();// 回滚
			logger.error(e.getMessage(), e);
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
	}

	@Override
	public List<User> queryByAreaId(int areaId) {
		Session session = null;
		Transaction tx = null;
		List<User> res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = session.createQuery("from User u where u.areaId = ?").setInteger(0, areaId).list();
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
	
	protected void doClose(PreparedStatement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				rs = null;
			} catch (Exception ex) {
				rs = null;
				logger.error(ex.getMessage(), ex);
				ex.printStackTrace();
			}
		}
		// Statement对象关闭时,会自动释放其管理的一个ResultSet对象
		if (stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (Exception ex) {
				stmt = null;
				logger.error(ex.getMessage(), ex);
			}
		}
		// 当Hibernate的事务由Spring接管时,session的关闭由Spring管理.不用手动关闭
		// if(session != null){
		// session.close();
		// }
	}

	protected void doClose(PreparedStatement stmt) {
		// Statement对象关闭时,会自动释放其管理的一个ResultSet对象
		if (stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (Exception ex) {
				stmt = null;
				logger.error(ex.getMessage(), ex);
			}
		}
	}
	


	@Override
	public void testor() {
//		Testor um = new Testor();
//		um.setName("name1");
//		Session s = sessionFactory.openSession();
//		Transaction t = s.beginTransaction();
//		s.save(um);
//		t.commit();
		String tableName = "Test";
		creatNewTable(tableName);
		Testor testor = new Testor();
		testor.setName("madarou");
		insertProduct(testor,tableName);
	}

	

}
