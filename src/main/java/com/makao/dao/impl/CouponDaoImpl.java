package com.makao.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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

import com.makao.dao.ICouponDao;
import com.makao.entity.Coupon;
import com.makao.entity.CouponOn;
import com.makao.entity.History;
import com.makao.entity.OrderOn;
import com.makao.entity.Product;
import com.makao.entity.User;
import com.makao.utils.MakaoConstants;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class CouponDaoImpl implements ICouponDao {
	private static final Logger logger = Logger.getLogger(CouponDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(Coupon coupon) {
		String tableName = "Coupon_"+coupon.getCityId();
		String sql = "INSERT INTO `"
				+ tableName
				+ "` (`name`,`amount`,`coverSUrl`,`coverBUrl`,`point`,`restrict`,`comment`,`cityName`,"
				+ "`cityId`,`isShow`,`type`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
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
						ps = connection.prepareStatement(sql);
						ps.setString(1, coupon.getName());
						ps.setString(2, coupon.getAmount());
						ps.setString(3, coupon.getCoverSUrl());
						ps.setString(4, coupon.getCoverBUrl());
						ps.setInt(5, coupon.getPoint());
						ps.setInt(6, coupon.getRestrict());
						ps.setString(7, coupon.getComment());
						ps.setString(8, coupon.getCityName());
						ps.setInt(9, coupon.getCityId());
						ps.setString(10, coupon.getIsShow());
						ps.setString(11, coupon.getType());
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
	public Coupon getById(int id, int cityid) {
		String tableName = "Coupon_"+cityid;
		String sql = "SELECT * FROM `"
				+ tableName
				+ "` WHERE id = ?";
		Session session = null;
		Transaction tx = null;
		List<Coupon> coupons = new ArrayList<Coupon>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(sql);
						ps.setInt(1, id);
						ResultSet rs = ps.executeQuery();
						//int col = rs.getMetaData().getColumnCount();
						while(rs.next()){
							Coupon p = new Coupon();
							p.setId(rs.getInt("id"));
							p.setName(rs.getString("name"));
							p.setAmount(rs.getString("amount"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setPoint(rs.getInt("point"));
							p.setRestrict(rs.getInt("restrict"));
							p.setComment(rs.getString("comment"));
							p.setCityName(rs.getString("cityName"));
							p.setIsShow(rs.getString("isShow"));
							p.setType(rs.getString("type"));
							p.setCityId(rs.getInt("cityId"));
							coupons.add(p);
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
		return coupons.size()>0 ? coupons.get(0) : null;
	}

	@Override
	public int update(Coupon coupon) {
		String tableName = "Coupon_"+coupon.getCityId();
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `name`='"+coupon.getName()+"',"
						+ "`amount`='"+coupon.getAmount()+"',"
								+ "`point`="+coupon.getPoint()+","
										+ "`restrict`="+coupon.getRestrict()+","
												+ "`comment`='"+coupon.getComment()+"',"
																		+ "`isShow`='"+coupon.getIsShow()+"',"
								+ "`coverSUrl`='"+coupon.getCoverSUrl()+"',"
										+ "`coverBUrl`='"+coupon.getCoverBUrl()+"',"
												+ "`cityName`='"+coupon.getCityName()+"',"
														+ "`type`='"+coupon.getType()+"',"
																		+ "`cityId`="+coupon.getCityId()
																				+ " WHERE `id`=" + coupon.getId();
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
						ps = connection.prepareStatement(sql);
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
	public List<Coupon> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Coupon> queryByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void testor() {
		// TODO Auto-generated method stub

	}

	@Override
	public int deleteById(int id, int cityId) {
		String tableName = "Coupon_"+cityId;
		String sql = "DELETE FROM `"+tableName+"` WHERE `id`="+id;
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
						ps = connection.prepareStatement(sql);
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
	public List<Coupon> queryAll(String tableName) {
		String sql = "SELECT * FROM "+ tableName;
		Session session = null;
		Transaction tx = null;
		List<Coupon> res = new LinkedList<Coupon>();
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
							Coupon p = new Coupon();
							p.setId(rs.getInt("id"));
							p.setName(rs.getString("name"));
							p.setAmount(rs.getString("amount"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setPoint(rs.getInt("point"));
							p.setRestrict(rs.getInt("restrict"));
							p.setComment(rs.getString("comment"));
							p.setCityName(rs.getString("cityName"));
							p.setCityId(rs.getInt("cityId"));
							p.setIsShow(rs.getString("isShow"));
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
	public Coupon queryByCouponId(String tableName, int couponid) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `id`="+couponid+" Order By `point`";
		Session session = null;
		Transaction tx = null;
		List<Coupon> res = new LinkedList<Coupon>();
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
							Coupon p = new Coupon();
							p.setId(rs.getInt("id"));
							p.setName(rs.getString("name"));
							p.setAmount(rs.getString("amount"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setPoint(rs.getInt("point"));
							p.setRestrict(rs.getInt("restrict"));
							p.setComment(rs.getString("comment"));
							p.setCityName(rs.getString("cityName"));
							p.setCityId(rs.getInt("cityId"));
							p.setIsShow(rs.getString("isShow"));
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
	
	@Override
	public int exchangeCoupon(Coupon coupon, User user) {
		String tableName = "Coupon_"+coupon.getCityId()+"_on";
		String tableName2 = "Exchange_"+coupon.getCityId();
		String sql1 = "INSERT INTO `"
				+ tableName
				+ "` (`name`,`amount`,`coverSUrl`,`coverBUrl`,`point`,`restrict`,`comment`,`cityName`,"
				+ "`cityId`,`userId`,`type`,`from`,`to`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sql2 = "INSERT INTO `"
				+ tableName2
				+ "` (`name`,`amount`,`point`,`restrict`,"
				+ "`cityId`,`userId`,`from`,`to`)"
				+ " VALUES (?,?,?,?,?,?,?,?)";
		Session session = null;
		Transaction tx = null;
		int res = 0;// 返回0表示成功，1表示失败
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			user.setPoint(user.getPoint()-coupon.getPoint());
			session.update(user);
			session.doWork(
			// 定义一个匿名类，实现了Work接口
			new Work() {
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;PreparedStatement ps2 =null;
					try {
						ps = connection.prepareStatement(sql1);
						ps.setString(1, coupon.getName());
						ps.setString(2, coupon.getAmount());
						ps.setString(3, coupon.getCoverSUrl());
						ps.setString(4, coupon.getCoverBUrl());
						ps.setInt(5, coupon.getPoint());
						ps.setInt(6, coupon.getRestrict());
						ps.setString(7, coupon.getComment());
						ps.setString(8, coupon.getCityName());
						ps.setInt(9, coupon.getCityId());
						ps.setInt(10, user.getId());
						ps.setString(11, coupon.getType());
						Date from = new Date(System.currentTimeMillis());
						Date to = new Date(System.currentTimeMillis()+(24*60*60*1000*MakaoConstants.COUPON_EXPIRE_DAY));
						ps.setDate(12, from);
						ps.setDate(13, to);
						int res = ps.executeUpdate();
						
						//插入兑换历史
						if(res>0){
							ps2 = connection.prepareStatement(sql2);
							ps2.setString(1, coupon.getName());
							ps2.setString(2, coupon.getAmount());
							ps2.setInt(3, coupon.getPoint());
							ps2.setInt(4, coupon.getRestrict());
							ps2.setInt(5, coupon.getCityId());
							ps2.setInt(6, user.getId());
							ps2.setDate(7, from);
							ps2.setDate(8, to);
							ps2.executeUpdate();
						}
					} finally {
						doClose(ps);doClose(ps2);
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
	public List<History> queryHistory(String tableName, int userid) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `userId`="+userid+" Order By `from` DESC";
		Session session = null;
		Transaction tx = null;
		List<History> res = new LinkedList<History>();
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
						while(rs.next()){
							History p = new History();
							p.setId(rs.getInt("id"));
							p.setName(rs.getString("name"));
							p.setAmount(rs.getString("amount"));
							p.setPoint(rs.getInt("point"));
							p.setRestrict(rs.getInt("restrict"));
							p.setCityId(rs.getInt("cityId"));
							p.setUserId(rs.getInt("userId"));
							p.setFrom(rs.getDate("from"));
							p.setTo(rs.getDate("to"));
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
