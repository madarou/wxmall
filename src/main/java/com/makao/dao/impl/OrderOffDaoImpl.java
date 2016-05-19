package com.makao.dao.impl;

import java.sql.Connection;
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

import com.makao.dao.IOrderOffDao;
import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;

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
		// TODO Auto-generated method stub
		return 0;
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
	public List<OrderOff> queryAll() {
		// TODO Auto-generated method stub
		return null;
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
	public List<OrderOff> queryDoneByAreaId(String tableName, int areaId) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `areaId`="+areaId+" AND `finalStatus`='已完成' Order By `finalTime`";
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
		String sql = "SELECT * FROM "+ tableName + " WHERE `areaId`="+areaId+" AND `finalStatus` IN ('已退货','卖家取消') Order By `finalTime`";
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
