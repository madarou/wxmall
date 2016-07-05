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

import com.makao.dao.IAddressDao;
import com.makao.entity.Area;
import com.makao.entity.Testor;
import com.makao.entity.Address;
import com.makao.entity.User;

@Repository
@Transactional
public class AddressDaoImpl implements IAddressDao {
	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(AddressDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;

	@Override
	public int insert(Address address) {
		Session session = null;
		Transaction tx = null;
		int res = 0;// 返回0表示失败，成功的话返回id
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
//			//不再更新user表里的对应字段，因为这些字段不再作为默认地址
//			session.save(address);// 保存地址
//			//如果是默认地址，则更新user表里的对应字段
//			if("yes".equals(address.getIsDefault())){
//				User user = (User)session.get(User.class, address.getUserId());
//				user.setReceiveName(address.getUserName());
//				user.setPhoneNumber(address.getPhoneNumber());
//				user.setAddress(address.getDetailAddress());
//				user.setAddLabel(address.getLabel());
//				session.update(user);
//			}
			if("yes".equals(address.getIsDefault())){//如果新增的地址被设为默认
				//尝试找到该城市区域下原来是默认的地址，将其默认字段设为no
				Address defaultA = (Address) session.createQuery("from Address a where a.userId=? and a.cityId=? and a.areaId=? and a.isDefault=?").setInteger(0, address.getUserId())
						.setInteger(1, address.getCityId()).setInteger(2, address.getAreaId()).setString(3, "yes").uniqueResult();
				if(defaultA!=null){
					defaultA.setIsDefault("no");
					session.update(defaultA);
				}
			}
			res = (int) session.save(address);// 保存地址
			tx.commit();// 提交事务
		} catch (HibernateException e) {
			if (null != tx)
				tx.rollback();// 回滚
			res = 0;
			logger.error(e.getMessage(), e);
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
		return res;
	}

	@Override
	public Address getById(int id) {
		// return (Address) sessionFactory.getCurrentSession().createQuery(
		// "from Address u where u.id=?").setInteger(0, id)
		// .uniqueResult();
		Session session = null;
		Transaction tx = null;
		Address res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = (Address) session.get(Address.class, id);
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
			Address address = (Address) session.get(Address.class, id);
			session.delete(address);
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
	public int update(Address address) {
		Session session = null;
		Transaction tx = null;
		int res = 0;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
//			session.update(address);
//			//如果是默认地址，则更新user表里的对应字段
//			if("yes".equals(address.getIsDefault())){
//				User user = (User)session.get(User.class, address.getUserId());
//				user.setReceiveName(address.getUserName());
//				user.setPhoneNumber(address.getPhoneNumber());
//				user.setAddress(address.getDetailAddress());
//				user.setAddLabel(address.getLabel());
//				session.update(user);
//			}
			//不再更新user表里的对应字段，因为这些字段不再作为默认地址
			//现在:
			//1.如果原来的add是默认地址，新来的address也是默认，则照常进行
			//2.如果原来的add是默认地址，新来的address不是默认地址，判断新来的没有isDefault，则add的设为no
			//3.如果原来的add不是默认地址，新来的不是默认地址，照常进行
			//4.如果原来的add不是默认地址，新来的是默认地址，则要找到原来是默认地址的，设为no
			if(!"yes".equals(address.getIsDefault())){//新来的不是默认的，照常进行
				session.update(address);
			}
			else{//新来的是设为默认
				Address add = (Address)session.get(Address.class, address.getId());
				if(!"yes".equals(add.getIsDefault())){//原来的不是默认，试图找到原来是默认的
					Address defaultA = (Address) session.createQuery("from Address a where a.userId=? and a.cityId=? and a.areaId=? and a.isDefault=?").setInteger(0, address.getUserId())
							.setInteger(1, address.getCityId()).setInteger(2, address.getAreaId()).setString(3, "yes").uniqueResult();
					if(defaultA!=null){
						defaultA.setIsDefault("no");
						session.update(defaultA);
					}
				}
				add.setUserName(address.getUserName());
				add.setPhoneNumber(address.getPhoneNumber());
				add.setDetailAddress(address.getDetailAddress());
				add.setLabel(address.getLabel());
				add.setIsDefault(address.getIsDefault());
				session.update(add);
			}
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
	public List<Address> queryAll() {
		Session session = null;
		Transaction tx = null;
		List<Address> res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = session.createQuery("from Address").list();
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
	public List<Address> queryByName(String name) {
		Session session = null;
		Transaction tx = null;
		List<Address> res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = session.createQuery("from Address u where u.addressName like ?")
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
	
	@Override
	public List<Address> queryByUserId(int userid) {
		Session session = null;
		Transaction tx = null;
		List<Address> res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = session.createQuery("from Address a where a.userId=?").setInteger(0, userid).list();
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
	public List<Address> queryByCityAreaUserId(int cityid, int areaid,
			int userid) {
		Session session = null;
		Transaction tx = null;
		List<Address> res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = session.createQuery("from Address a where a.userId=? and a.cityId=? and a.areaId=?").setInteger(0, userid)
					.setInteger(1, cityid).setInteger(2, areaid).list();
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
