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

import com.makao.dao.ICouponOnDao;
import com.makao.entity.Coupon;
import com.makao.entity.CouponOn;
import com.makao.entity.OrderOn;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class CouponOnDaoImpl implements ICouponOnDao {
	private static final Logger logger = Logger.getLogger(CouponOnDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(CouponOn couponOn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CouponOn getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(CouponOn couponOn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CouponOn> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CouponOn> queryByName(String name) {
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
	public List<CouponOn> queryAllByUserId(String tableName, int userid) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `userId`="+userid+" Order By `from`";
		Session session = null;
		Transaction tx = null;
		List<CouponOn> res = new LinkedList<CouponOn>();
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
							CouponOn p = new CouponOn();
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
	public CouponOn queryByCouponId(String tableName, int couponid) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `id`="+couponid;
		Session session = null;
		Transaction tx = null;
		List<CouponOn> res = new LinkedList<CouponOn>();
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
							CouponOn p = new CouponOn();
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

	@Override
	public List<CouponOn> expireCoupons(int cityid) {
		String tableName = "Coupon_"+cityid+"_on";
		String tableName2 = "Coupon_"+cityid+"_off";
		String sql = "SELECT * FROM "+ tableName + " WHERE `to`<CURDATE()";
		String sql2 = "INSERT INTO `"
				+ tableName2
				+ "` (`name`,`amount`,`coverSUrl`,`coverBUrl`,`point`,`restrict`,`comment`,`cityName`,"
				+ "`cityId`,`userId`,`type`,`from`,`to`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		Session session = null;
		Transaction tx = null;
		List<CouponOn> res = new LinkedList<CouponOn>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;PreparedStatement ps2 = null;PreparedStatement ps3 = null;
					try {
						ps = connection.prepareStatement(sql);
						ps2 = connection.prepareStatement(sql2);
						ResultSet rs = ps.executeQuery();
						//int col = rs.getMetaData().getColumnCount();
						while(rs.next()){
							CouponOn p = new CouponOn();
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
							res.add(p);
							
							//ps2 = connection.prepareStatement(sql2);
							ps2.setString(1, p.getName());
							ps2.setString(2, p.getAmount());
							ps2.setString(3, p.getCoverSUrl());
							ps2.setString(4, p.getCoverBUrl());
							ps2.setInt(5, p.getPoint());
							ps2.setInt(6, p.getRestrict());
							ps2.setString(7, p.getComment());
							ps2.setString(8, p.getCityName());
							ps2.setInt(9, p.getCityId());
							ps2.setInt(10, p.getUserId());
							ps2.setString(11, p.getType());
							ps2.setDate(12, p.getFrom());
							ps2.setDate(13, p.getTo());
							//ps2.setDate(14, new Date(System.currentTimeMillis()));//自动过期的不设置overDueDate
							ps2.executeUpdate();
							
							String sql3 = "DELETE FROM `"+tableName+"` WHERE `id`="+p.getId();
							ps3 = connection.prepareStatement(sql3);
							ps3.executeUpdate();
						}
					}finally{
						doClose(ps);doClose(ps2);doClose(ps3);
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
		return res.size()>0 ? res : null;
	}

}
