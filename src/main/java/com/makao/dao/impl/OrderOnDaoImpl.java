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

import com.makao.dao.IOrderOnDao;
import com.makao.entity.City;
import com.makao.entity.CouponOn;
import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;
import com.makao.entity.Product;
import com.mysql.jdbc.Statement;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class OrderOnDaoImpl implements IOrderOnDao {
	private static final Logger logger = Logger.getLogger(OrderOnDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(OrderOn orderOn) {
		String tableName = "Order_"+orderOn.getCityId()+"_on";
		String sql = "INSERT INTO `"
				+ tableName
				+ "` (`number`,`productIds`,`productNames`,`orderTime`,`receiverName`,`phoneNumber`,`address`,`payType`,"
				+ "`receiveType`,`receiveTime`,`couponId`,`couponPrice`,`totalPrice`,"
				+ "`freight`,`comment`,`vcomment`,`status`,`cityarea`,`userId`,`areaId`,`cityId`,`refundStatus`,`history`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<CouponOn> co = new ArrayList<CouponOn>();
		Session session = null;
		Transaction tx = null;
		List<Integer> res = new ArrayList<Integer>();// 返回0表示失败，成功则返回orderid
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.doWork(
			// 定义一个匿名类，实现了Work接口
			new Work() {
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, orderOn.getNumber());
						ps.setString(2, orderOn.getProductIds());
						ps.setString(3, orderOn.getProductNames());
						ps.setTimestamp(4, orderOn.getOrderTime());
						ps.setString(5, orderOn.getReceiverName());
						ps.setString(6, orderOn.getPhoneNumber());
						ps.setString(7, orderOn.getAddress());
						ps.setString(8, orderOn.getPayType());
						ps.setString(9, orderOn.getReceiveType());
						ps.setString(10, orderOn.getReceiveTime());
						ps.setInt(11, orderOn.getCouponId());
						ps.setString(12, orderOn.getCouponPrice());
						ps.setString(13, orderOn.getTotalPrice());
						ps.setString(14, orderOn.getFreight());
						ps.setString(15, orderOn.getComment());
						ps.setString(16, orderOn.getVcomment());
						ps.setString(17, orderOn.getStatus());
						ps.setString(18, orderOn.getCityarea());
						ps.setInt(19, orderOn.getUserId());
						ps.setInt(20, orderOn.getAreaId());
						ps.setInt(21, orderOn.getCityId());
						ps.setString(22, orderOn.getRefundStatus());
						ps.setString(23, orderOn.getStatus()+"="+orderOn.getOrderTime());
						int row = ps.executeUpdate();
						ResultSet rs = ps.getGeneratedKeys();  
					     if ( rs.next() ) {  
					    	 int key = rs.getInt(row);  
					         logger.info("插入的order id:"+key);  
					         res.add(key);
					     }  
					} finally {
						doClose(ps);
					}
				}
			});
			//如果优惠券不为空，则要记录优惠券被使用的信息，即从Coupon_cityid_on移到Coupon_cityid_off中去
			int couponid = orderOn.getCouponId();
			if(couponid>0){
				session.doWork(
						// 定义一个匿名类，实现了Work接口
						new Work() {
							public void execute(Connection connection) throws SQLException {
								PreparedStatement ps = null;
								try {
									String couponTable = "Coupon_"+orderOn.getCityId()+"_on";
									String sql1 = "SELECT * FROM `" + couponTable + "` WHERE `id`="+couponid;
									ps = connection.prepareStatement(sql1);
									ResultSet rs = ps.executeQuery();
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
										co.add(p);
									}
								} finally {
									doClose(ps);
								}
							}
						});
				if(co.size()>0){//从CouponOn里查到了，写入off表里
					session.doWork(
							// 定义一个匿名类，实现了Work接口
							new Work() {
								public void execute(Connection connection) throws SQLException {
									PreparedStatement ps = null;
									CouponOn couponOn = co.get(0);
									String tableName = "Coupon_"+orderOn.getCityId()+"_off";
									String sql2 = "INSERT INTO `"
											+ tableName
											+ "` (`name`,`amount`,`coverSUrl`,`coverBUrl`,`point`,`restrict`,`comment`,`cityName`,"
											+ "`cityId`,`userId`,`type`,`from`,`to`,`overdueDate`)"
											+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
									try {
										ps = connection.prepareStatement(sql2);
										ps.setString(1, couponOn.getName());
										ps.setString(2, couponOn.getAmount());
										ps.setString(3, couponOn.getCoverSUrl());
										ps.setString(4, couponOn.getCoverBUrl());
										ps.setInt(5, couponOn.getPoint());
										ps.setInt(6, couponOn.getRestrict());
										ps.setString(7, couponOn.getComment());
										ps.setString(8, couponOn.getCityName());
										ps.setInt(9, couponOn.getCityId());
										ps.setInt(10, couponOn.getUserId());
										ps.setString(11, couponOn.getType());
										ps.setDate(12, couponOn.getFrom());
										ps.setDate(13, couponOn.getTo());
										ps.setDate(14, new Date(System.currentTimeMillis()));
										ps.executeUpdate();
									} finally {
										doClose(ps);
									}
								}
							});
					session.doWork(//在Coupon_cityId_on中删除该记录
							// 定义一个匿名类，实现了Work接口
							new Work() {
								public void execute(Connection connection) throws SQLException {
									PreparedStatement ps = null;
									try {
										String tableName = "Coupon_"+orderOn.getCityId()+"_on";
										String sql3 = "DELETE FROM `"+tableName+"` WHERE `id`="+couponid;
										ps = connection.prepareStatement(sql3);
										ps.executeUpdate();
									} finally {
										doClose(ps);
									}
								}
							});
					
				}
			}
			tx.commit(); // 使用 Hibernate事务处理边界
		} catch (HibernateException e) {
			if (null != tx)
				tx.rollback();// 回滚
			logger.error(e.getMessage(), e);
			res.add(0);
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
		return (res.get(0)!=0)?res.get(0):0;
	}

	@Override
	public OrderOn getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(OrderOn orderOn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<OrderOn> queryAll(String tableName) {
		String sql = "SELECT * FROM "+ tableName;
		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new LinkedList<OrderOn>();
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
							OrderOn p = new OrderOn();
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
							p.setStatus(rs.getString("status"));
							p.setCityarea(rs.getString("cityarea"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
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
	public List<OrderOn> queryByName(String name) {
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
	
	/* (non-Javadoc)
	 * @see com.makao.dao.IOrderOnDao#queryQueueByAreaId(int)
	 * 查询该area下所有排队订单
	 */
	@Override
	public List<OrderOn> queryQueueByAreaId(String tableName, int areaId) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `areaId`="+areaId+" AND `status`='排队中' Order By `orderTime`";
		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new LinkedList<OrderOn>();
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
							OrderOn p = new OrderOn();
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
							p.setStatus(rs.getString("status"));
							p.setCityarea(rs.getString("cityarea"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
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
	public List<OrderOn> queryProcessByAreaId(String tableName, int areaId) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `areaId`="+areaId+" AND `status` IN ('待处理','配送中') Order By `receiveTime`";
		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new LinkedList<OrderOn>();
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
							OrderOn p = new OrderOn();
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
							p.setStatus(rs.getString("status"));
							p.setCityarea(rs.getString("cityarea"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
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
	public int cancelOrder(int cityId, int orderid, String vcomment) {
//		String sql = "UPDATE `"
//				+ tableName
//				+ "` SET `vcomment`="+vcomment+" AND `status`='卖家取消' WHERE `id`="+orderid;
		//先从Order_on查出订单内容，在写入到Order_off中，并追加finalStatus和vcomment
		String Order_cityId_on = "Order_"+cityId+"_on";
		String Order_cityId_off = "Order_"+cityId+"_off";
		
		String sql1 = "SELECT * FROM `" + Order_cityId_on + "` WHERE `id`="+orderid;
		String sql2 = "INSERT INTO `"
				+ Order_cityId_off
				+ "` (`number`,`productIds`,`productNames`,`orderTime`,`receiverName`,`phoneNumber`,`address`,`payType`,"
				+ "`receiveType`,`receiveTime`,`couponId`,`couponPrice`,`totalPrice`,"
				+ "`freight`,`comment`,`vcomment`,`finalStatus`,`cityarea`,`finalTime`,`userId`,`areaId`,`cityId`,`refundStatus`,`history`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sql3 = "DELETE FROM `"+Order_cityId_on+"` WHERE `id`="+orderid;
		List<OrderOn> oo = new ArrayList<OrderOn>();
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
						ResultSet rs = ps.executeQuery();
						while(rs.next()){
							OrderOn p = new OrderOn();
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
							p.setStatus(rs.getString("status"));
							p.setCityarea(rs.getString("cityarea"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
							oo.add(p);
						}
					} finally {
						doClose(ps);
					}
				}
			});
			if(oo.size()>0){//从OrderOn里查到了，写入off表里
				session.doWork(
						// 定义一个匿名类，实现了Work接口
						new Work() {
							public void execute(Connection connection) throws SQLException {
								PreparedStatement ps = null;
								OrderOn orderOn = oo.get(0);
								try {
									ps = connection.prepareStatement(sql2);
									ps.setString(1, orderOn.getNumber());
									ps.setString(2, orderOn.getProductIds());
									ps.setString(3, orderOn.getProductNames());
									ps.setTimestamp(4, orderOn.getOrderTime());
									ps.setString(5, orderOn.getReceiverName());
									ps.setString(6, orderOn.getPhoneNumber());
									ps.setString(7, orderOn.getAddress());
									ps.setString(8, orderOn.getPayType());
									ps.setString(9, orderOn.getReceiveType());
									ps.setString(10, orderOn.getReceiveTime());
									ps.setInt(11, orderOn.getCouponId());
									ps.setString(12, orderOn.getCouponPrice());
									ps.setString(13, orderOn.getTotalPrice());
									ps.setString(14, orderOn.getFreight());
									ps.setString(15, orderOn.getComment());
									ps.setString(16, orderOn.getVcomment());
									ps.setString(17, "已取消");
									ps.setString(18, orderOn.getCityarea());
									ps.setTimestamp(19, new Timestamp(System.currentTimeMillis()));
									ps.setInt(20, orderOn.getUserId());
									ps.setInt(21, orderOn.getAreaId());
									ps.setInt(22, orderOn.getCityId());
									ps.setString(23, "待退款");
									ps.setString(24,orderOn.getHistory()+",已取消="+new Timestamp(System.currentTimeMillis()));
									ps.executeUpdate();
								} finally {
									doClose(ps);
								}
							}
						});
				session.doWork(//在Order_cityId_on中删除该记录
						// 定义一个匿名类，实现了Work接口
						new Work() {
							public void execute(Connection connection) throws SQLException {
								PreparedStatement ps = null;
								try {
									ps = connection.prepareStatement(sql3);
									ps.executeUpdate();
								} finally {
									doClose(ps);
								}
							}
						});
				
			}
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
	 * @see com.makao.dao.IOrderOnDao#distributeOrder(int, int)
	 * 配送订单
	 */
	@Override
	public int distributeOrder(int cityId, int orderid) {
		String tableName = "Order_"+cityId+"_on";
		String history = ",配送中="+new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `status`='配送中',`history`=concat(`history`,'"+history+"') WHERE `id`="+orderid;
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
	public int finishOrder(int cityId, int orderid) {
		String tableName = "Order_"+cityId+"_on";
//		String sql = "UPDATE `"
//				+ tableName
//				+ "` SET `status`='已配送' WHERE `id`="+orderid;
		String history = ",已配送="+new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `status`='已配送',`history`=concat(`history`,'"+history+"') WHERE `id`="+orderid;
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
	public int confirmGetOrder(int cityId, int orderid) {
		String Order_cityId_on = "Order_"+cityId+"_on";
		String Order_cityId_off = "Order_"+cityId+"_off";
		
		String sql1 = "SELECT * FROM `" + Order_cityId_on + "` WHERE `id`="+orderid;
		String sql2 = "INSERT INTO `"
				+ Order_cityId_off
				+ "` (`number`,`productIds`,`productNames`,`orderTime`,`receiverName`,`phoneNumber`,`address`,`payType`,"
				+ "`receiveType`,`receiveTime`,`couponId`,`couponPrice`,`totalPrice`,"
				+ "`freight`,`comment`,`vcomment`,`finalStatus`,`cityarea`,`finalTime`,`userId`,`areaId`,`cityId`,`refundStatus`,`history`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sql3 = "DELETE FROM `"+Order_cityId_on+"` WHERE `id`="+orderid;
		List<OrderOn> oo = new ArrayList<OrderOn>();
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
						ResultSet rs = ps.executeQuery();
						while(rs.next()){
							OrderOn p = new OrderOn();
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
							p.setStatus(rs.getString("status"));
							p.setCityarea(rs.getString("cityarea"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
							oo.add(p);
						}
					} finally {
						doClose(ps);
					}
				}
			});
			if(oo.size()>0){//从OrderOn里查到了，写入off表里
				session.doWork(
						// 定义一个匿名类，实现了Work接口
						new Work() {
							public void execute(Connection connection) throws SQLException {
								PreparedStatement ps = null;
								OrderOn orderOn = oo.get(0);
								try {
									ps = connection.prepareStatement(sql2);
									ps.setString(1, orderOn.getNumber());
									ps.setString(2, orderOn.getProductIds());
									ps.setString(3, orderOn.getProductNames());
									ps.setTimestamp(4, orderOn.getOrderTime());
									ps.setString(5, orderOn.getReceiverName());
									ps.setString(6, orderOn.getPhoneNumber());
									ps.setString(7, orderOn.getAddress());
									ps.setString(8, orderOn.getPayType());
									ps.setString(9, orderOn.getReceiveType());
									ps.setString(10, orderOn.getReceiveTime());
									ps.setInt(11, orderOn.getCouponId());
									ps.setString(12, orderOn.getCouponPrice());
									ps.setString(13, orderOn.getTotalPrice());
									ps.setString(14, orderOn.getFreight());
									ps.setString(15, orderOn.getComment());
									ps.setString(16, orderOn.getVcomment());
									ps.setString(17, "已收货");
									ps.setString(18, orderOn.getCityarea());
									ps.setTimestamp(19, new Timestamp(System.currentTimeMillis()));
									ps.setInt(20, orderOn.getUserId());
									ps.setInt(21, orderOn.getAreaId());
									ps.setInt(22, orderOn.getCityId());
									ps.setString(23, "无需退款");//正常完成的订单，退款状态为无
									ps.setString(24, orderOn.getHistory()+",已收货="+new Timestamp(System.currentTimeMillis()));
									ps.executeUpdate();
								} finally {
									doClose(ps);
								}
							}
						});
				session.doWork(//在Order_cityId_on中删除该记录
						// 定义一个匿名类，实现了Work接口
						new Work() {
							public void execute(Connection connection) throws SQLException {
								PreparedStatement ps = null;
								try {
									ps = connection.prepareStatement(sql3);
									ps.executeUpdate();
								} finally {
									doClose(ps);
								}
							}
						});
				
			}
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
	public List<OrderOn> queryDistributedByAreaId(String tableName, int areaId) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `areaId`="+areaId+" AND `status`='已配送' Order By `receiveTime`";
		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new LinkedList<OrderOn>();
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
							OrderOn p = new OrderOn();
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
							p.setStatus(rs.getString("status"));
							p.setCityarea(rs.getString("cityarea"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
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
//	@Override
//	public int finishOrder(int cityId, int orderid) {
//		String Order_cityId_on = "Order_"+cityId+"_on";
//		String Order_cityId_off = "Order_"+cityId+"_off";
//		
//		String sql1 = "SELECT * FROM `" + Order_cityId_on + "` WHERE `id`="+orderid;
//		String sql2 = "INSERT INTO `"
//				+ Order_cityId_off
//				+ "` (`number`,`productIds`,`productNames`,`orderTime`,`receiverName`,`phoneNumber`,`address`,`payType`,"
//				+ "`receiveType`,`receiveTime`,`couponId`,`couponPrice`,`totalPrice`,"
//				+ "`freight`,`comment`,`vcomment`,`finalStatus`,`cityarea`,`finalTime`,`userId`,`areaId`,`cityId`,`refundStatus`)"
//				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		String sql3 = "DELETE FROM `"+Order_cityId_on+"` WHERE `id`="+orderid;
//		List<OrderOn> oo = new ArrayList<OrderOn>();
//		Session session = null;
//		Transaction tx = null;
//		int res = 0;// 返回0表示成功，1表示失败
//		try {
//			session = sessionFactory.openSession();
//			tx = session.beginTransaction();
//			session.doWork(
//			// 定义一个匿名类，实现了Work接口
//			new Work() {
//				public void execute(Connection connection) throws SQLException {
//					PreparedStatement ps = null;
//					try {
//						ps = connection.prepareStatement(sql1);
//						ResultSet rs = ps.executeQuery();
//						while(rs.next()){
//							OrderOn p = new OrderOn();
//							p.setId(rs.getInt("id"));
//							p.setNumber(rs.getString("number"));
//							p.setProductIds(rs.getString("productIds"));
//							p.setProductNames(rs.getString("productNames"));
//							p.setOrderTime(rs.getTimestamp("orderTime"));
//							p.setReceiverName(rs.getString("receiverName"));
//							p.setPhoneNumber(rs.getString("phoneNumber"));
//							p.setAddress(rs.getString("address"));
//							p.setPayType(rs.getString("payType"));
//							p.setReceiveType(rs.getString("receiveType"));
//							p.setReceiveTime(rs.getString("receiveTime"));
//							p.setCouponId(rs.getInt("couponId"));
//							p.setCouponPrice(rs.getString("couponPrice"));
//							p.setTotalPrice(rs.getString("totalPrice"));
//							p.setFreight(rs.getString("freight"));
//							p.setComment(rs.getString("comment"));
//							p.setVcomment(rs.getString("vcomment"));
//							p.setStatus(rs.getString("status"));
//							p.setCityarea(rs.getString("cityarea"));
//							p.setUserId(rs.getInt("userId"));
//							p.setAreaId(rs.getInt("areaId"));
//							p.setCityId(rs.getInt("cityId"));
//							p.setRefundStatus(rs.getString("refundStatus"));
//							oo.add(p);
//						}
//					} finally {
//						doClose(ps);
//					}
//				}
//			});
//			if(oo.size()>0){//从OrderOn里查到了，写入off表里
//				session.doWork(
//						// 定义一个匿名类，实现了Work接口
//						new Work() {
//							public void execute(Connection connection) throws SQLException {
//								PreparedStatement ps = null;
//								OrderOn orderOn = oo.get(0);
//								try {
//									ps = connection.prepareStatement(sql2);
//									ps.setString(1, orderOn.getNumber());
//									ps.setString(2, orderOn.getProductIds());
//									ps.setString(3, orderOn.getProductNames());
//									ps.setTimestamp(4, orderOn.getOrderTime());
//									ps.setString(5, orderOn.getReceiverName());
//									ps.setString(6, orderOn.getPhoneNumber());
//									ps.setString(7, orderOn.getAddress());
//									ps.setString(8, orderOn.getPayType());
//									ps.setString(9, orderOn.getReceiveType());
//									ps.setString(10, orderOn.getReceiveTime());
//									ps.setInt(11, orderOn.getCouponId());
//									ps.setString(12, orderOn.getCouponPrice());
//									ps.setString(13, orderOn.getTotalPrice());
//									ps.setString(14, orderOn.getFreight());
//									ps.setString(15, orderOn.getComment());
//									ps.setString(16, orderOn.getVcomment());
//									ps.setString(17, "已完成");
//									ps.setString(18, orderOn.getCityarea());
//									ps.setTimestamp(19, new Timestamp(System.currentTimeMillis()));
//									ps.setInt(20, orderOn.getUserId());
//									ps.setInt(21, orderOn.getAreaId());
//									ps.setInt(22, orderOn.getCityId());
//									ps.setString(23, "无");//正常完成的订单，退款状态为无
//									ps.executeUpdate();
//								} finally {
//									doClose(ps);
//								}
//							}
//						});
//				session.doWork(//在Order_cityId_on中删除该记录
//						// 定义一个匿名类，实现了Work接口
//						new Work() {
//							public void execute(Connection connection) throws SQLException {
//								PreparedStatement ps = null;
//								try {
//									ps = connection.prepareStatement(sql3);
//									ps.executeUpdate();
//								} finally {
//									doClose(ps);
//								}
//							}
//						});
//				
//			}
//			tx.commit(); // 使用 Hibernate事务处理边界
//		} catch (HibernateException e) {
//			if (null != tx)
//				tx.rollback();// 回滚
//			logger.error(e.getMessage(), e);
//			res = 1;
//		} finally {
//			if (null != session)
//				session.close();// 关闭回话
//		}
//		return res;
//	}

	@Override
	public List<OrderOn> queryByUserId(String tableName, int userid) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `userId`="+userid+" Order By `orderTime` DESC";
		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new LinkedList<OrderOn>();
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
							OrderOn p = new OrderOn();
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
							p.setStatus(rs.getString("status"));
							p.setCityarea(rs.getString("cityarea"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
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
	public OrderOn queryByOrderId(String tableName, int orderid) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `id`="+orderid;
		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new LinkedList<OrderOn>();
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
							OrderOn p = new OrderOn();
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
							p.setStatus(rs.getString("status"));
							p.setCityarea(rs.getString("cityarea"));
							p.setUserId(rs.getInt("userId"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setRefundStatus(rs.getString("refundStatus"));
							p.setHistory(rs.getString("history"));
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
	public int getQueueRecordCount(int cityid, int areaid) {
		String tableName = "Order_"+cityid+"_on";
		String sql = "SELECT count(id) as count FROM "+tableName+ " WHERE `areaId`="+areaid+" AND `status`='排队中'";
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
	public int getProcessRecordCount(int cityId, int areaId) {
		String tableName = "Order_"+cityId+"_on";
		String sql = "SELECT count(id) as count FROM "+tableName+ " WHERE `areaId`="+areaId+" AND `status` IN ('待处理','配送中')";
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
	public int getDistributedRecordCount(int cityId, int areaId) {
		String tableName = "Order_"+cityId+"_on";
		String sql = "SELECT count(id) as count FROM "+tableName+ " WHERE `areaId`="+areaId+" AND `status`='已配送'";
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
		String tableName = "Order_"+cityid+"_on";
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
	public int confirmMoney(String cityid, String orderid) {
		String tableName = "Order_"+cityid+"_on";
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `status`='排队中' WHERE `id`="+orderid;
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
