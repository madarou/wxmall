package com.makao.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
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

import com.makao.dao.ICouponOffDao;
import com.makao.entity.CouponOff;
import com.makao.entity.CouponOn;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class CouponOffDaoImpl implements ICouponOffDao {
	private static final Logger logger = Logger.getLogger(CouponOffDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(CouponOff couponOff) {
		String tableName = "Coupon_"+couponOff.getCityId()+"_off";
		System.out.println(tableName);
		String sql1 = "INSERT INTO `"
				+ tableName
				+ "` (`name`,`amount`,`coverSUrl`,`coverBUrl`,`point`,`restrict`,`comment`,`cityName`,"
				+ "`cityId`,`userId`,`type`,`from`,`to`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Session session = null;
		Transaction tx = null;
		int res = 0;// 返回0表示成功，1表示失败
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.doWork(
			// 定义一个匿名类，实现了Work接口
			new Work() {
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(sql1);
						ps.setString(1, couponOff.getName());
						ps.setString(2, couponOff.getAmount());
						ps.setString(3, couponOff.getCoverSUrl());
						ps.setString(4, couponOff.getCoverBUrl());
						ps.setInt(5, couponOff.getPoint());
						ps.setInt(6, couponOff.getRestrict());
						ps.setString(7, couponOff.getComment());
						ps.setString(8, couponOff.getCityName());
						ps.setInt(9, couponOff.getCityId());
						ps.setInt(10, couponOff.getUserId());
						ps.setString(11, couponOff.getType());
						ps.setDate(12, new Date(System.currentTimeMillis()));
						ps.setDate(13, new Date(System.currentTimeMillis()+(24*60*60*1000*7)));
						ps.executeUpdate();
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
			res = 1;
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
		return res;
	}

	@Override
	public CouponOff getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(CouponOff couponOff) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CouponOff> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CouponOff> queryByName(String name) {
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

	@Override
	public List<CouponOff> queryAllByUserId(String tableName, int userid) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `userId`="+userid+" Order By `to`";
		Session session = null;
		Transaction tx = null;
		List<CouponOff> res = new LinkedList<CouponOff>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();
						//int col = rs.getMetaData().getColumnCount();
						while(rs.next()){
							CouponOff p = new CouponOff();
							p.setId(rs.getInt("id"));
							p.setName(rs.getString("name"));
							p.setAmount(rs.getString("amount"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setPoint(rs.getInt("point"));
							p.setFrom(rs.getDate("from"));
							p.setTo(rs.getDate("to"));
							p.setRestrict(rs.getInt("restrict"));
							p.setComment(rs.getString("comment"));
							p.setCityName(rs.getString("cityName"));
							p.setCityId(rs.getInt("cityId"));
							p.setUserId(rs.getInt("userId"));
							p.setType(rs.getString("type"));
							p.setOverdueDate(rs.getDate("overdueDate"));
							res.add(p);
						}
					}finally{
						doClose(ps);
					}
				}
				
			});
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
	public CouponOff queryByCouponId(String tableName, int couponid) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `id`="+couponid;
		Session session = null;
		Transaction tx = null;
		List<CouponOff> res = new LinkedList<CouponOff>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();
						//int col = rs.getMetaData().getColumnCount();
						while(rs.next()){
							CouponOff p = new CouponOff();
							p.setId(rs.getInt("id"));
							p.setName(rs.getString("name"));
							p.setAmount(rs.getString("amount"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setPoint(rs.getInt("point"));
							p.setFrom(rs.getDate("from"));
							p.setTo(rs.getDate("to"));
							p.setRestrict(rs.getInt("restrict"));
							p.setComment(rs.getString("comment"));
							p.setCityName(rs.getString("cityName"));
							p.setCityId(rs.getInt("cityId"));
							p.setUserId(rs.getInt("userId"));
							p.setType(rs.getString("type"));
							p.setOverdueDate(rs.getDate("overdueDate"));
							res.add(p);
						}
					}finally{
						doClose(ps);
					}
				}
				
			});
			tx.commit();// 提交事务
		} catch (HibernateException e) {
			if (null != tx)
				tx.rollback();// 回滚
			logger.error(e.getMessage(), e);
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
		return res.size()>0 ? res.get(0) : null;
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
