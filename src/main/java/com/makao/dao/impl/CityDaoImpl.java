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

import com.makao.dao.ICityDao;
import com.makao.entity.Area;
import com.makao.entity.City;
import com.makao.entity.User;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class CityDaoImpl implements ICityDao {
	private static final Logger logger = Logger.getLogger(CityDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(City city) {
		Session session = null;
		Transaction tx = null;
		int res = 0;// 返回0表示成功，1表示失败
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.save(city);// 保存
			//为Order_cityId_on建表
			String tableName = "Order_"+city.getId()+"_on";
			String sql = "CREATE TABLE IF NOT EXISTS `"
					+ tableName
					+ "` (`id` int(11) NOT NULL AUTO_INCREMENT,"
					+ "`number` varchar(20) NOT NULL,"
					+ "`productIds` varchar(512),"
					+ "`productNames` varchar(512),"
					+ "`orderTime` datetime,"
					+ "`receiverName` varchar(30),"
					+ "`phoneNumber` varchar(12),"
					+ "`address` varchar(120),"
					+ "`payType` varchar(20),"
					+ "`receiveType` varchar(20),"
					+ "`receiveTime` varchar(30),"
					+ "`couponId` int(11),"
					+ "`couponPrice` varchar(10),"
					+ "`totalPrice` varchar(10),"
					+ "`freight` varchar(10),"
					+ "`comment` varchar(255),"
					+ "`vcomment` varchar(255),"
					+ "`status` varchar(10),"
					+ "`cityarea` varchar(30),"
					+ "`userId` int(11),"
					+ "`areaId` int(11),"
					+ "`cityId` int(11),"
					+ "`refundStatus` varchar(10),"
					+ "`history` varchar(400),"
					+ "`point` int(11),"
					+ "`sender` varchar(30),"
					+ "`senderPhone` varchar(12),"
					+ "PRIMARY KEY (`id`))";
			session.doWork(
					// 定义一个匿名类，实现了Work接口
					new Work() {
						public void execute(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(sql);
								ps.execute();
							} finally {
								doClose(ps);
							}
						}
					});
			//为Order_cityId_off建表
			String tableName2 = "Order_"+city.getId()+"_off";
			String sql2 = "CREATE TABLE IF NOT EXISTS `"
					+ tableName2
					+ "` (`id` int(11) NOT NULL AUTO_INCREMENT,"
					+ "`number` varchar(20) NOT NULL,"
					+ "`productIds` varchar(512),"
					+ "`productNames` varchar(512),"
					+ "`orderTime` datetime,"
					+ "`receiverName` varchar(30),"
					+ "`phoneNumber` varchar(12),"
					+ "`address` varchar(120),"
					+ "`payType` varchar(20),"
					+ "`receiveType` varchar(20),"
					+ "`receiveTime` varchar(30),"
					+ "`couponId` int(11),"
					+ "`couponPrice` varchar(10),"
					+ "`totalPrice` varchar(10),"
					+ "`freight` varchar(10),"
					+ "`comment` varchar(255),"
					+ "`vcomment` varchar(255),"
					+ "`finalStatus` varchar(10),"
					+ "`cityarea` varchar(30),"
					+ "`finalTime` datetime,"
					+ "`userId` int(11),"
					+ "`areaId` int(11),"
					+ "`cityId` int(11),"
					+ "`refundStatus` varchar(10),"
					+ "`history` varchar(400),"
					+ "`point` int(11),"
					+ "`sender` varchar(30),"
					+ "`senderPhone` varchar(12),"
					+ "`pcomments` varchar(255),"
					+ "`commented` int(11),"
					+ "`inventBack` int(11),"
					+ "PRIMARY KEY (`id`))";
			session.doWork(
					// 定义一个匿名类，实现了Work接口
					new Work() {
						public void execute(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(sql2);
								ps.execute();
							} finally {
								doClose(ps);
							}
						}
					});
			//为Coupon_cityId建表
			String tableName4 = "Coupon_"+city.getId();
			String sql4 = "CREATE TABLE IF NOT EXISTS `"
					+ tableName4
					+ "` (`id` int(11) NOT NULL AUTO_INCREMENT,"
					+ "`name` varchar(30) NOT NULL,"
					+ "`amount` varchar(10),"
					+ "`coverSUrl` varchar(50),"
					+ "`coverBUrl` varchar(50),"
					+ "`point` int(11),"
					+ "`restrict` int(11),"
					+ "`comment` varchar(50),"
					+ "`cityName` varchar(30),"
					+ "`cityId` int(11),"
					+ "`isShow` varchar(5),"
					+ "`type` varchar(20),"
					+ "PRIMARY KEY (`id`))";
			session.doWork(
					// 定义一个匿名类，实现了Work接口
					new Work() {
						public void execute(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(sql4);
								ps.execute();
							} finally {
								doClose(ps);
							}
						}
					});
			//为Coupon_cityId_on建表
			String tableName5 = "Coupon_"+city.getId()+"_on";
			String sql5 = "CREATE TABLE IF NOT EXISTS `"
					+ tableName5
					+ "` (`id` int(11) NOT NULL AUTO_INCREMENT,"
					+ "`name` varchar(30) NOT NULL,"
					+ "`amount` varchar(10),"
					+ "`coverSUrl` varchar(50),"
					+ "`coverBUrl` varchar(50),"
					+ "`point` int(11),"
					+ "`from` date,"
					+ "`to` date,"
					+ "`restrict` int(11),"
					+ "`comment` varchar(50),"
					+ "`cityName` varchar(30),"
					+ "`cityId` int(11),"
					+ "`userId` int(11),"
					+ "`type` varchar(20),"
					+ "PRIMARY KEY (`id`))";
			session.doWork(
					// 定义一个匿名类，实现了Work接口
					new Work() {
						public void execute(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(sql5);
								ps.execute();
							} finally {
								doClose(ps);
							}
						}
					});
			//为Coupon_cityId_off建表
			String tableName6 = "Coupon_"+city.getId()+"_off";
			String sql6 = "CREATE TABLE IF NOT EXISTS `"
					+ tableName6
					+ "` (`id` int(11) NOT NULL AUTO_INCREMENT,"
					+ "`name` varchar(30) NOT NULL,"
					+ "`amount` varchar(10),"
					+ "`coverSUrl` varchar(50),"
					+ "`coverBUrl` varchar(50),"
					+ "`point` int(11),"
					+ "`from` date,"
					+ "`to` date,"
					+ "`restrict` int(11),"
					+ "`comment` varchar(50),"
					+ "`cityName` varchar(30),"
					+ "`cityId` int(11),"
					+ "`userId` int(11),"
					+ "`type` varchar(20),"
					+ "`overdueDate` date,"
					+ "PRIMARY KEY (`id`))";
			session.doWork(
					// 定义一个匿名类，实现了Work接口
					new Work() {
						public void execute(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(sql6);
								ps.execute();
							} finally {
								doClose(ps);
							}
						}
					});
			//为Gift_cityId建表
			String tableName3 = "Gift_"+city.getId();
			String sql3 = "CREATE TABLE IF NOT EXISTS `"
					+ tableName3
					+ "` (`id` int(11) NOT NULL AUTO_INCREMENT,"
					+ "`name` varchar(30) NOT NULL,"
					+ "`coverSUrl` varchar(50),"
					+ "`coverBUrl` varchar(50),"
					+ "`point` int(11),"
					+ "`comment` varchar(50),"
					+ "`from` date,"
					+ "`to` date,"
					+ "`inventory` int(11),"
					+ "`remain` int(11),"
					+ "`type` varchar(20),"
					+ "`areaId` int(11),"
					+ "`areaName` varchar(30),"
					+ "`cityId` int(11),"
					+ "PRIMARY KEY (`id`))";
			session.doWork(
					// 定义一个匿名类，实现了Work接口
					new Work() {
						public void execute(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(sql3);
								ps.execute();
							} finally {
								doClose(ps);
							}
						}
					});
			//为Exchange_cityId建表
			String tableName7 = "Exchange_"+city.getId();
			String sql7 = "CREATE TABLE IF NOT EXISTS `"
					+ tableName7
					+ "` (`id` int(11) NOT NULL AUTO_INCREMENT,"
					+ "`name` varchar(30) NOT NULL,"
					+ "`amount` varchar(10),"
					+ "`point` int(11),"
					+ "`restrict` int(11),"
					+ "`cityId` int(11),"
					+ "`userId` int(11),"
					+ "`from` date,"
					+ "`to` date,"
					+ "PRIMARY KEY (`id`))";
			session.doWork(
					// 定义一个匿名类，实现了Work接口
					new Work() {
						public void execute(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(sql7);
								ps.execute();
							} finally {
								doClose(ps);
							}
						}
					});
			//为PointLog_cityId建表
			String tableName8 = "PointLog_"+city.getId();
			String sql8 = "CREATE TABLE IF NOT EXISTS `"
					+ tableName8
					+ "` (`id` int(11) NOT NULL AUTO_INCREMENT,"
					+ "`name` varchar(30) NOT NULL,"
					+ "`point` int(11),"
					+ "`getDate` date,"
					+ "`comment` varchar(100),"
					+ "`cityId` int(11),"
					+ "`userId` int(11),"
					+ "PRIMARY KEY (`id`))";
			session.doWork(
					// 定义一个匿名类，实现了Work接口
					new Work() {
						public void execute(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(sql8);
								ps.execute();
							} finally {
								doClose(ps);
							}
						}
					});
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
	public City getById(int id) {
		Session session = null;
		Transaction tx = null;
		City res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = (City) session.get(City.class, id);
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
	public int update(City city) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<City> queryAll() {
		Session session = null;
		Transaction tx = null;
		List<City> res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = session.createQuery("from City").list();
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
	public List<City> queryByName(String name) {
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

}
