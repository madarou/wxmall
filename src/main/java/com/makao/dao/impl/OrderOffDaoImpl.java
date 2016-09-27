package com.makao.dao.impl;

import java.sql.Connection;
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

import com.makao.dao.IOrderOffDao;
import com.makao.entity.Comment;
import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;
import com.makao.entity.OrderState;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class OrderOffDaoImpl implements IOrderOffDao {
	private static final Logger logger = Logger.getLogger(OrderOffDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(OrderOff orderOff) {
		String tableName = "Order_"+orderOff.getCityId()+"_off";
		String sql = "INSERT INTO `"
				+ tableName
				+ "` (`number`,`productIds`,`productNames`,`orderTime`,`receiverName`,`phoneNumber`,`address`,`payType`,"
				+ "`receiveType`,`receiveTime`,`couponId`,`couponPrice`,`totalPrice`,"
				+ "`freight`,`comment`,`vcomment`,`finalStatus`,`cityarea`,`finalTime`,`userId`,`areaId`,`cityId`,`refundStatus`,`history`,`point`,`sender,`senderPhone`,`pcomments`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
						ps.setString(1, orderOff.getNumber());
						ps.setString(2, orderOff.getProductIds());
						ps.setString(3, orderOff.getProductNames());
						ps.setTimestamp(4, orderOff.getOrderTime());
						ps.setString(5, orderOff.getReceiverName());
						ps.setString(6, orderOff.getPhoneNumber());
						ps.setString(7, orderOff.getAddress());
						ps.setString(8, orderOff.getPayType());
						ps.setString(9, orderOff.getReceiveType());
						ps.setString(10, orderOff.getReceiveTime());
						ps.setInt(11, orderOff.getCouponId());
						ps.setString(12, orderOff.getCouponPrice());
						ps.setString(13, orderOff.getTotalPrice());
						ps.setString(14, orderOff.getFreight());
						ps.setString(15, orderOff.getComment());
						ps.setString(16, orderOff.getVcomment());
						ps.setString(17, orderOff.getFinalStatus());
						ps.setString(18, orderOff.getCityarea());
						ps.setTimestamp(19, orderOff.getFinalTime());
						ps.setInt(20, orderOff.getUserId());
						ps.setInt(21, orderOff.getAreaId());
						ps.setInt(22, orderOff.getCityId());
						ps.setString(23, orderOff.getRefundStatus());
						ps.setString(24, orderOff.getHistory());
						ps.setInt(25, orderOff.getPoint());
						ps.setString(26, orderOff.getSender());
						ps.setString(27, orderOff.getSenderPhone());
						ps.setString(28, "");
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
	public OrderOff getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(OrderOff orderOff) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<OrderOff> queryByName(String name) {
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
	public List<OrderOff> queryConfirmGetByAreaId(String tableName, int areaId) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `areaId`="+areaId+" AND `finalStatus`='"+OrderState.RECEIVED.getCode()+"' Order By `finalTime`";
		Session session = null;
		Transaction tx = null;
		List<OrderOff> res = new LinkedList<OrderOff>();
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
							OrderOff p = new OrderOff();
							p.setId(rs.getInt("id"));
							p.setNumber(rs.getString("number"));
							p.setProductIds(rs.getString("productIds"));
							p.setProductNames(rs.getString("productNames"));
							p.setOrderTime(rs.getTimestamp("orderTime"));
							p.setReceiverName(rs.getString("receiverName"));
							p.setPhoneNumber(rs.getString("phoneNumber"));
							p.setAddress(rs.getString("address"));
							p.setPayType(rs.getString("payType"));
							p.setReceiveType(rs.getString("receiveType"));
							p.setReceiveTime(rs.getString("receiveTime"));
							p.setCouponId(rs.getInt("couponId"));
							p.setCouponPrice(rs.getString("couponPrice"));
							p.setTotalPrice(rs.getString("totalPrice"));
							p.setFreight(rs.getString("freight"));
							p.setComment(rs.getString("comment"));
							p.setVcomment(rs.getString("vcomment"));
							p.setFinalStatus(rs.getString("finalStatus"));
							p.setCityarea(rs.getString("cityarea"));
							p.setFinalTime(rs.getTimestamp("finalTime"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
							p.setPcomments(rs.getString("pcomments"));
							p.setCommented(rs.getInt("commented"));
							p.setInventBack(rs.getInt("inventBack"));
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
	public List<OrderOff> queryCancelByAreaId(String tableName, int areaId) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `areaId`="+areaId+" AND `finalStatus` IN ('"+OrderState.RETURNED.getCode()+"','"+OrderState.CANCELED.getCode()+"','"+OrderState.RETURN_CANCELED.getCode()+"') Order By `finalTime`";
		Session session = null;
		Transaction tx = null;
		List<OrderOff> res = new LinkedList<OrderOff>();
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
							OrderOff p = new OrderOff();
							p.setId(rs.getInt("id"));
							p.setNumber(rs.getString("number"));
							p.setProductIds(rs.getString("productIds"));
							p.setProductNames(rs.getString("productNames"));
							p.setOrderTime(rs.getTimestamp("orderTime"));
							p.setReceiverName(rs.getString("receiverName"));
							p.setPhoneNumber(rs.getString("phoneNumber"));
							p.setAddress(rs.getString("address"));
							p.setPayType(rs.getString("payType"));
							p.setReceiveType(rs.getString("receiveType"));
							p.setReceiveTime(rs.getString("receiveTime"));
							p.setCouponId(rs.getInt("couponId"));
							p.setCouponPrice(rs.getString("couponPrice"));
							p.setTotalPrice(rs.getString("totalPrice"));
							p.setFreight(rs.getString("freight"));
							p.setComment(rs.getString("comment"));
							p.setVcomment(rs.getString("vcomment"));
							p.setFinalStatus(rs.getString("finalStatus"));
							p.setCityarea(rs.getString("cityarea"));
							p.setFinalTime(rs.getTimestamp("finalTime"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
							p.setPcomments(rs.getString("pcomments"));
							p.setCommented(rs.getInt("commented"));
							p.setInventBack(rs.getInt("inventBack"));
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
	public List<OrderOff> queryRefundByAreaId(String tableName, int areaId) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `areaId`="+areaId+" AND `finalStatus` IN ('"+OrderState.RETURN_APPLYING.getCode()+"','"+OrderState.RETURNING.getCode()+"') Order By `finalTime`";
		Session session = null;
		Transaction tx = null;
		List<OrderOff> res = new LinkedList<OrderOff>();
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
							OrderOff p = new OrderOff();
							p.setId(rs.getInt("id"));
							p.setNumber(rs.getString("number"));
							p.setProductIds(rs.getString("productIds"));
							p.setProductNames(rs.getString("productNames"));
							p.setOrderTime(rs.getTimestamp("orderTime"));
							p.setReceiverName(rs.getString("receiverName"));
							p.setPhoneNumber(rs.getString("phoneNumber"));
							p.setAddress(rs.getString("address"));
							p.setPayType(rs.getString("payType"));
							p.setReceiveType(rs.getString("receiveType"));
							p.setReceiveTime(rs.getString("receiveTime"));
							p.setCouponId(rs.getInt("couponId"));
							p.setCouponPrice(rs.getString("couponPrice"));
							p.setTotalPrice(rs.getString("totalPrice"));
							p.setFreight(rs.getString("freight"));
							p.setComment(rs.getString("comment"));
							p.setVcomment(rs.getString("vcomment"));
							p.setFinalStatus(rs.getString("finalStatus"));
							p.setCityarea(rs.getString("cityarea"));
							p.setFinalTime(rs.getTimestamp("finalTime"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
							p.setPcomments(rs.getString("pcomments"));
							p.setCommented(rs.getInt("commented"));
							p.setInventBack(rs.getInt("inventBack"));
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
	public int refundOrder(int cityId, int orderid) {
		String tableName = "Order_"+cityId+"_off";
		String history = ","+OrderState.RETURNING.getText()+"="+new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `finalStatus`='"+OrderState.RETURNING.getCode()+"',`history`=concat(`history`,'"+history+"') WHERE `id`="+orderid;
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
	public int finishReturnOrder(int cityId, int orderid) {
		String tableName = "Order_"+cityId+"_off";
		String history = ","+OrderState.RETURNED.getText()+"="+new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `finalStatus`='"+OrderState.RETURNED.getCode()+"',`refundStatus`='待退款',`finalTime`=?,`history`=concat(`history`,'"+history+"') WHERE `id`="+orderid;
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
						ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
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
	public int finishRefundOrder(int cityId, int orderid) {
		String tableName = "Order_"+cityId+"_off";
		String history = ","+OrderState.REFUNDED.getText()+"="+new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `refundStatus`='已退款',`history`=concat(`history`,'"+history+"') WHERE `id`="+orderid;
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
	public int cancelRefundOrder(int cityId, int orderid, String vcomment) {
		String tableName = "Order_"+cityId+"_off";
		String history = ","+OrderState.RETURN_CANCELED.getText()+"="+new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `finalStatus`='"+OrderState.RETURN_CANCELED.getCode()+"',`refundStatus`='无需退款',`vcomment`='"+vcomment+"',`history`=concat(`history`,'"+history+"') WHERE `id`="+orderid;
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
	public List<OrderOff> queryAllCanceledAndReturned(String tableName) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `finalStatus` IN ('"+OrderState.RETURNED.getCode()+"','"+OrderState.CANCELED.getCode()+"') Order By `finalTime`";
		Session session = null;
		Transaction tx = null;
		List<OrderOff> res = new LinkedList<OrderOff>();
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
							OrderOff p = new OrderOff();
							p.setId(rs.getInt("id"));
							p.setNumber(rs.getString("number"));
							p.setProductIds(rs.getString("productIds"));
							p.setProductNames(rs.getString("productNames"));
							p.setOrderTime(rs.getTimestamp("orderTime"));
							p.setReceiverName(rs.getString("receiverName"));
							p.setPhoneNumber(rs.getString("phoneNumber"));
							p.setAddress(rs.getString("address"));
							p.setPayType(rs.getString("payType"));
							p.setReceiveType(rs.getString("receiveType"));
							p.setReceiveTime(rs.getString("receiveTime"));
							p.setCouponId(rs.getInt("couponId"));
							p.setCouponPrice(rs.getString("couponPrice"));
							p.setTotalPrice(rs.getString("totalPrice"));
							p.setFreight(rs.getString("freight"));
							p.setComment(rs.getString("comment"));
							p.setVcomment(rs.getString("vcomment"));
							p.setFinalStatus(rs.getString("finalStatus"));
							p.setCityarea(rs.getString("cityarea"));
							p.setFinalTime(rs.getTimestamp("finalTime"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
							p.setPcomments(rs.getString("pcomments"));
							p.setCommented(rs.getInt("commented"));
							p.setInventBack(rs.getInt("inventBack"));
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
	public List<OrderOff> queryAll(String tableName) {
		String sql = "SELECT * FROM "+ tableName;
		Session session = null;
		Transaction tx = null;
		List<OrderOff> res = new LinkedList<OrderOff>();
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
							OrderOff p = new OrderOff();
							p.setId(rs.getInt("id"));
							p.setNumber(rs.getString("number"));
							p.setProductIds(rs.getString("productIds"));
							p.setProductNames(rs.getString("productNames"));
							p.setOrderTime(rs.getTimestamp("orderTime"));
							p.setReceiverName(rs.getString("receiverName"));
							p.setPhoneNumber(rs.getString("phoneNumber"));
							p.setAddress(rs.getString("address"));
							p.setPayType(rs.getString("payType"));
							p.setReceiveType(rs.getString("receiveType"));
							p.setReceiveTime(rs.getString("receiveTime"));
							p.setCouponId(rs.getInt("couponId"));
							p.setCouponPrice(rs.getString("couponPrice"));
							p.setTotalPrice(rs.getString("totalPrice"));
							p.setFreight(rs.getString("freight"));
							p.setComment(rs.getString("comment"));
							p.setVcomment(rs.getString("vcomment"));
							p.setFinalStatus(rs.getString("finalStatus"));
							p.setCityarea(rs.getString("cityarea"));
							p.setFinalTime(rs.getTimestamp("finalTime"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
							p.setPcomments(rs.getString("pcomments"));
							p.setCommented(rs.getInt("commented"));
							p.setInventBack(rs.getInt("inventBack"));
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
	public List<OrderOff> queryByUserId(String tableName, int userid) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `userId`="+userid+" Order By `orderTime` DESC";
		Session session = null;
		Transaction tx = null;
		List<OrderOff> res = new LinkedList<OrderOff>();
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
							OrderOff p = new OrderOff();
							p.setId(rs.getInt("id"));
							p.setNumber(rs.getString("number"));
							p.setProductIds(rs.getString("productIds"));
							p.setProductNames(rs.getString("productNames"));
							p.setOrderTime(rs.getTimestamp("orderTime"));
							p.setReceiverName(rs.getString("receiverName"));
							p.setPhoneNumber(rs.getString("phoneNumber"));
							p.setAddress(rs.getString("address"));
							p.setPayType(rs.getString("payType"));
							p.setReceiveType(rs.getString("receiveType"));
							p.setReceiveTime(rs.getString("receiveTime"));
							p.setCouponId(rs.getInt("couponId"));
							p.setCouponPrice(rs.getString("couponPrice"));
							p.setTotalPrice(rs.getString("totalPrice"));
							p.setFreight(rs.getString("freight"));
							p.setComment(rs.getString("comment"));
							p.setVcomment(rs.getString("vcomment"));
							p.setFinalStatus(rs.getString("finalStatus"));
							p.setCityarea(rs.getString("cityarea"));
							p.setFinalTime(rs.getTimestamp("finalTime"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
							p.setPcomments(rs.getString("pcomments"));
							p.setCommented(rs.getInt("commented"));
							p.setInventBack(rs.getInt("inventBack"));
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
	public OrderOff queryByOrderId(String tableName, int orderid) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `id`="+orderid+" Order By `orderTime` DESC";
		Session session = null;
		Transaction tx = null;
		List<OrderOff> res = new LinkedList<OrderOff>();
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
							OrderOff p = new OrderOff();
							p.setId(rs.getInt("id"));
							p.setNumber(rs.getString("number"));
							p.setProductIds(rs.getString("productIds"));
							p.setProductNames(rs.getString("productNames"));
							p.setOrderTime(rs.getTimestamp("orderTime"));
							p.setReceiverName(rs.getString("receiverName"));
							p.setPhoneNumber(rs.getString("phoneNumber"));
							p.setAddress(rs.getString("address"));
							p.setPayType(rs.getString("payType"));
							p.setReceiveType(rs.getString("receiveType"));
							p.setReceiveTime(rs.getString("receiveTime"));
							p.setCouponId(rs.getInt("couponId"));
							p.setCouponPrice(rs.getString("couponPrice"));
							p.setTotalPrice(rs.getString("totalPrice"));
							p.setFreight(rs.getString("freight"));
							p.setComment(rs.getString("comment"));
							p.setVcomment(rs.getString("vcomment"));
							p.setFinalStatus(rs.getString("finalStatus"));
							p.setCityarea(rs.getString("cityarea"));
							p.setFinalTime(rs.getTimestamp("finalTime"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
							p.setPcomments(rs.getString("pcomments"));
							p.setCommented(rs.getInt("commented"));
							p.setInventBack(rs.getInt("inventBack"));
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
	public int returnOrder(int cityid, int orderid) {
		String tableName = "Order_"+cityid+"_off";
		String history = ","+OrderState.RETURN_APPLYING.getText()+"="+new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `finalStatus`='"+OrderState.RETURN_APPLYING.getCode()+"',`refundStatus`='无',`history`=concat(`history`,'"+history+"') WHERE `id`="+orderid;
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
	public int getConfirmRecordCount(int cityId, int areaId) {
		String tableName = "Order_"+cityId+"_off";
		String sql = "SELECT count(id) as count FROM "+tableName+ " WHERE `areaId`="+areaId+" AND `finalStatus`='"+OrderState.RECEIVED.getCode()+"'";
		Session session = null;
		Transaction tx = null;
		List<Integer> res = new ArrayList<Integer>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			//res = session.createQuery("from User").list();
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();
						//int col = rs.getMetaData().getColumnCount();
						rs.next();
						res.add(rs.getInt("count"));
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
		return  (res.size()>0 ? res.get(0) : 0);
	}
	
	@Override
	public int getReturnRecordCount(int cityId, int areaId) {
		String tableName = "Order_"+cityId+"_off";
		String sql = "SELECT count(id) as count FROM "+tableName+ " WHERE `areaId`="+areaId+" AND `finalStatus` IN ('"+OrderState.RETURN_APPLYING.getCode()+"','"+OrderState.RETURNING.getCode()+"')";
		Session session = null;
		Transaction tx = null;
		List<Integer> res = new ArrayList<Integer>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			//res = session.createQuery("from User").list();
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();
						//int col = rs.getMetaData().getColumnCount();
						rs.next();
						res.add(rs.getInt("count"));
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
		return  (res.size()>0 ? res.get(0) : 0);
	}

	@Override
	public int getCancelRecordCount(int cityId, int areaId) {
		String tableName = "Order_"+cityId+"_off";
		String sql = "SELECT count(id) as count FROM "+tableName+ " WHERE `areaId`="+areaId+" AND `finalStatus` IN ('"+OrderState.RETURNED.getCode()+"','"+OrderState.CANCELED.getCode()+"','"+OrderState.RETURN_CANCELED.getCode()+"')";
		Session session = null;
		Transaction tx = null;
		List<Integer> res = new ArrayList<Integer>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			//res = session.createQuery("from User").list();
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();
						//int col = rs.getMetaData().getColumnCount();
						rs.next();
						res.add(rs.getInt("count"));
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
		return  (res.size()>0 ? res.get(0) : 0);
	}
	
	@Override
	public int getRecordCount(int cityid) {
		String tableName = "Order_"+cityid+"_off";
		String sql = "SELECT count(id) as count FROM "+tableName;
		Session session = null;
		Transaction tx = null;
		List<Integer> res = new ArrayList<Integer>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			//res = session.createQuery("from User").list();
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();
						//int col = rs.getMetaData().getColumnCount();
						rs.next();
						res.add(rs.getInt("count"));
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
		return  (res.size()>0 ? res.get(0) : 0);
	}
	
	@Override
	public int getCanceledAndReturnedRecordCount(int cityid) {
		String tableName = "Order_"+cityid+"_off";
		String sql = "SELECT count(id) as count FROM "+tableName+" WHERE `finalStatus` IN ('"+OrderState.RETURNED.getCode()+"','"+OrderState.CANCELED.getCode()+"')";
		Session session = null;
		Transaction tx = null;
		List<Integer> res = new ArrayList<Integer>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			//res = session.createQuery("from User").list();
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();
						//int col = rs.getMetaData().getColumnCount();
						rs.next();
						res.add(rs.getInt("count"));
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
		return  (res.size()>0 ? res.get(0) : 0);
	}
	

	@Override
	public int updateComment(Comment comment) {
		String tableName = "Order_"+comment.getCityId()+"_off";
		String com = ","+comment.getProductId()+"="+comment.getId();
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `finalStatus`='"+OrderState.COMMETED.getCode()+", `pcomments`=concat(IFNULL(`pcomments`,''),'"+com+"') WHERE `id`="+comment.getOrderId();
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
	
	/* (non-Javadoc)
	 * @see com.makao.dao.IOrderOffDao#inventoryBackCanceledAndReturned(java.lang.String)
	 * 将取消或退货的的订单的inventBack字段设为1
	 */
	@Override
	public List<OrderOff> inventoryBackCanceledAndReturned(String tableName) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `finalStatus` IN ('"+OrderState.RETURNED.getCode()+"','"+OrderState.CANCELED.getCode()+"') AND `inventBack`=0";
		Session session = null;
		Transaction tx = null;
		List<OrderOff> res = new LinkedList<OrderOff>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;PreparedStatement ps2 = null;
					try {
						ps = connection.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();
						while(rs.next()){
							OrderOff p = new OrderOff();
							p.setId(rs.getInt("id"));
							p.setNumber(rs.getString("number"));
							p.setProductIds(rs.getString("productIds"));
							p.setProductNames(rs.getString("productNames"));
							p.setOrderTime(rs.getTimestamp("orderTime"));
							p.setReceiverName(rs.getString("receiverName"));
							p.setPhoneNumber(rs.getString("phoneNumber"));
							p.setAddress(rs.getString("address"));
							p.setPayType(rs.getString("payType"));
							p.setReceiveType(rs.getString("receiveType"));
							p.setReceiveTime(rs.getString("receiveTime"));
							p.setCouponId(rs.getInt("couponId"));
							p.setCouponPrice(rs.getString("couponPrice"));
							p.setTotalPrice(rs.getString("totalPrice"));
							p.setFreight(rs.getString("freight"));
							p.setComment(rs.getString("comment"));
							p.setVcomment(rs.getString("vcomment"));
							p.setFinalStatus(rs.getString("finalStatus"));
							p.setCityarea(rs.getString("cityarea"));
							p.setFinalTime(rs.getTimestamp("finalTime"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
							p.setPcomments(rs.getString("pcomments"));
							p.setCommented(rs.getInt("commented"));
							p.setInventBack(rs.getInt("inventBack"));
							res.add(p);
							String sql2 = "UPDATE `"
									+ tableName
									+ "` SET `inventBack`=1 WHERE `id`="+rs.getInt("id");
							ps2 = connection.prepareStatement(sql2);
							ps2.executeUpdate();
							String added = rs.getInt("cityId")+"_"+rs.getInt("areaId")+"_"+rs.getString("number");
							logger.info("将取消或退货订单(cityid_areaid_number)的库存加回："+added);
						}
					}finally{
						doClose(ps);doClose(ps2);
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
