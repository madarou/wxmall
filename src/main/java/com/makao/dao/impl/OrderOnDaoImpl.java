package com.makao.dao.impl;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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
import com.makao.entity.OrderState;
import com.makao.entity.Product;
import com.makao.utils.MakaoConstants;
import com.makao.utils.TimeUtil;
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
				+ "`freight`,`comment`,`vcomment`,`status`,`cityarea`,`userId`,`areaId`,`cityId`,`refundStatus`,`history`,`point`,`sender`,`senderPhone`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
						DecimalFormat fnum = new  DecimalFormat("##0"); //保留整数
						fnum.setRoundingMode(RoundingMode.HALF_UP);
						int point = Integer.valueOf(fnum.format(Float.valueOf(orderOn.getTotalPrice())*MakaoConstants.POINT_PROPORTION));
						ps.setInt(24, point);
						ps.setString(25, orderOn.getSender());
						ps.setString(26, orderOn.getSenderPhone());
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
		String sql = "SELECT * FROM "+ tableName+ " WHERE status<>'"+OrderState.NOT_PAID.getCode()+"'";
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
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
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
		String sql = "SELECT * FROM "+ tableName + " WHERE `areaId`="+areaId+" AND `status`='"+OrderState.QUEUE.getCode()+"' Order By `orderTime`";
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
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
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
		String sql = "SELECT * FROM "+ tableName + " WHERE `areaId`="+areaId+" AND `status` IN ('"+OrderState.PROCESS_WAITING.getCode()+"','"+OrderState.DISTRIBUTING.getCode()+"') Order By `receiveTime`";
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
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
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
		//先从Order_on查出订单内容，在写入到Order_off中，并追加finalStatus和vcomment
		String Order_cityId_on = "Order_"+cityId+"_on";
		String Order_cityId_off = "Order_"+cityId+"_off";
		
		String sql1 = "SELECT * FROM `" + Order_cityId_on + "` WHERE `id`="+orderid;
		String sql2 = "INSERT INTO `"
				+ Order_cityId_off
				+ "` (`number`,`productIds`,`productNames`,`orderTime`,`receiverName`,`phoneNumber`,`address`,`payType`,"
				+ "`receiveType`,`receiveTime`,`couponId`,`couponPrice`,`totalPrice`,"
				+ "`freight`,`comment`,`vcomment`,`finalStatus`,`cityarea`,`finalTime`,`userId`,`areaId`,`cityId`,`refundStatus`,`history`,`point`,`sender`,`senderPhone`,`pcomments`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
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
									if(orderOn.getVcomment()!=null&&!"".equals(orderOn.getVcomment())){
										ps.setString(16, orderOn.getVcomment()+";"+vcomment);}
									else{
										ps.setString(16, vcomment);}
									ps.setString(17, OrderState.CANCELED.getCode()+"");
									ps.setString(18, orderOn.getCityarea());
									ps.setTimestamp(19, new Timestamp(System.currentTimeMillis()));
									ps.setInt(20, orderOn.getUserId());
									ps.setInt(21, orderOn.getAreaId());
									ps.setInt(22, orderOn.getCityId());
									ps.setString(23, "待退款");
									ps.setString(24,orderOn.getHistory()+","+OrderState.CANCELED.getCode()+"="+new Timestamp(System.currentTimeMillis()));
									ps.setInt(25, orderOn.getPoint());
									ps.setString(26, orderOn.getSender());
									ps.setString(27, orderOn.getSenderPhone());
									ps.setString(28, "");
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
	public OrderOn distributeOrder(int cityId, int orderid) {
		String tableName = "Order_"+cityId+"_on";
		String history = ","+OrderState.DISTRIBUTING.getCode()+"="+new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `status`='"+OrderState.DISTRIBUTING.getCode()+"',`history`=concat(`history`,'"+history+"') WHERE `id`="+orderid;
		String sql2 = "SELECT * FROM "+tableName+ " WHERE `id`="+orderid;
		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new ArrayList<OrderOn>();
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.doWork(
			// 定义一个匿名类，实现了Work接口
			new Work() {
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;PreparedStatement ps2 = null;
					try {
						ps = connection.prepareStatement(sql);
						int returncount = ps.executeUpdate();
						if(returncount!=0){//如果执行成功，则查询order对象并返回
							ps2=connection.prepareStatement(sql2);
							ResultSet rs = ps2.executeQuery();
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
								p.setPoint(rs.getInt("point"));
								p.setSender(rs.getString("sender"));
								p.setSenderPhone(rs.getString("senderPhone"));
								res.add(p);
							}
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
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
		return res.size()>0 ? res.get(0) : null;
	}
	
	@Override
	public OrderOn finishOrder(int cityId, int orderid) {
		String tableName = "Order_"+cityId+"_on";
//		String sql = "UPDATE `"
//				+ tableName
//				+ "` SET `status`='已配送' WHERE `id`="+orderid;
		String history = ","+OrderState.DISTRIBUTED.getCode()+"="+new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `status`='"+OrderState.DISTRIBUTED.getCode()+"',`history`=concat(`history`,'"+history+"') WHERE `id`="+orderid;
		String sql2 = "SELECT * FROM "+tableName+ " WHERE `id`="+orderid;
		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new ArrayList<OrderOn>();
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.doWork(
			// 定义一个匿名类，实现了Work接口
			new Work() {
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					PreparedStatement ps2 = null;
					try {
						ps = connection.prepareStatement(sql);
						int returncount = ps.executeUpdate();
						if(returncount!=0){//如果执行成功，则查询order对象并返回
							ps2=connection.prepareStatement(sql2);
							ResultSet rs = ps2.executeQuery();
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
								p.setPoint(rs.getInt("point"));
								p.setSender(rs.getString("sender"));
								p.setSenderPhone(rs.getString("senderPhone"));
								res.add(p);
							}
						}
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
		return res.size()>0 ? res.get(0) : null;
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
				+ "`freight`,`comment`,`vcomment`,`finalStatus`,`cityarea`,`finalTime`,`userId`,`areaId`,`cityId`,`refundStatus`,`history`,`point`,`sender`,`senderPhone`,`pcomments`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
							if(!"5".equals(rs.getString("status")))//如果当前的状态不是5，不能继续操作
								return;
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
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
							oo.add(p);
						}
					} finally {
						doClose(ps);
					}
				}
			});
			if(oo.size()==0){
				return 1;
			}
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
									ps.setString(17, OrderState.RECEIVED.getCode()+"");
									ps.setString(18, orderOn.getCityarea());
									ps.setTimestamp(19, new Timestamp(System.currentTimeMillis()));
									ps.setInt(20, orderOn.getUserId());
									ps.setInt(21, orderOn.getAreaId());
									ps.setInt(22, orderOn.getCityId());
									ps.setString(23, "无需退款");//正常完成的订单，退款状态为无
									ps.setString(24, orderOn.getHistory()+","+OrderState.RECEIVED.getCode()+"="+new Timestamp(System.currentTimeMillis()));
									ps.setInt(25, orderOn.getPoint());
									ps.setString(26, orderOn.getSender());
									ps.setString(27, orderOn.getSenderPhone());
									ps.setString(28, "");
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
		String sql = "SELECT * FROM "+ tableName + " WHERE `areaId`="+areaId+" AND `status`='"+OrderState.DISTRIBUTED.getCode()+"' Order By `receiveTime`";
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
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
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
		String sql = "SELECT * FROM "+ tableName + " WHERE `userId`="+userid+" AND status<>'"+OrderState.NOT_PAID.getCode()+"' Order By `orderTime` DESC";
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
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
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
							p.setPoint(rs.getInt("point"));
							p.setSender(rs.getString("sender"));
							p.setSenderPhone(rs.getString("senderPhone"));
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
		String sql = "SELECT count(id) as count FROM "+tableName+ " WHERE `areaId`="+areaid+" AND `status`='"+OrderState.QUEUE.getCode()+"'";
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
		String sql = "SELECT count(id) as count FROM "+tableName+ " WHERE `areaId`="+areaId+" AND `status` IN ('"+OrderState.PROCESS_WAITING.getCode()+"','"+OrderState.DISTRIBUTING.getCode()+"')";
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
		String sql = "SELECT count(id) as count FROM "+tableName+ " WHERE `areaId`="+areaId+" AND `status`='"+OrderState.DISTRIBUTED.getCode()+"'";
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
		String sql = "SELECT count(id) as count FROM "+tableName + " WHERE status<>'"+OrderState.NOT_PAID.getCode()+"'";
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
	public OrderOn confirmMoney(String cityid, String orderNumber) {
		String tableName = "Order_"+cityid+"_on";
		String sql2 = "SELECT * FROM "+tableName+ " WHERE `number`='"+orderNumber+"'";
		
		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new LinkedList<OrderOn>();
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.doWork(
			// 定义一个匿名类，实现了Work接口
			new Work() {
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps0 = null;
					PreparedStatement ps = null;
					int returncount = 0;
					try {
						//先查询出订单，找到配送时间是不是立即配送
						ps0 = connection.prepareStatement(sql2);
						ResultSet rs = ps0.executeQuery();
						while(rs.next()){
						if("立即配送".equals(rs.getString("receiveTime"))){
							String history = ","+OrderState.PROCESS_WAITING.getCode()+"="+new Timestamp(System.currentTimeMillis());
							String sql = "UPDATE `"
									+ tableName
									+ "` SET `status`='"+OrderState.PROCESS_WAITING.getCode()+"',`history`=concat(`history`,'"+history+"') WHERE `number`='"+orderNumber+"'";
							ps = connection.prepareStatement(sql);
							returncount = ps.executeUpdate();
						}
						else{
							String history = ","+OrderState.QUEUE.getCode()+"="+new Timestamp(System.currentTimeMillis());
							String sql = "UPDATE `"
									+ tableName
									+ "` SET `status`='"+OrderState.QUEUE.getCode()+"',`history`=concat(`history`,'"+history+"') WHERE `number`='"+orderNumber+"'";
							ps = connection.prepareStatement(sql);
							returncount = ps.executeUpdate();
						}
						if(returncount!=0){//如果执行成功，则查询order对象并返回
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
								if("立即配送".equals(rs.getString("receiveTime"))){
									p.setStatus(OrderState.PROCESS_WAITING.getCode()+"");
									p.setHistory(rs.getString("history")+","+OrderState.PROCESS_WAITING.getCode()+"="+new Timestamp(System.currentTimeMillis()));
								}
								else{
									p.setStatus(OrderState.QUEUE.getCode()+"");
									p.setHistory(rs.getString("history")+","+OrderState.QUEUE.getCode()+"="+new Timestamp(System.currentTimeMillis()));
								}
								p.setCityarea(rs.getString("cityarea"));
								p.setUserId(rs.getInt("userId"));
								p.setAreaId(rs.getInt("areaId"));
								p.setCityId(rs.getInt("cityId"));
								p.setRefundStatus(rs.getString("refundStatus"));
								p.setPoint(rs.getInt("point"));
								p.setSender(rs.getString("sender"));
								p.setSenderPhone(rs.getString("senderPhone"));
								res.add(p);
							}
						}
					} finally {
						doClose(ps);doClose(ps0);
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
		return (res.size()>0 ? res.get(0) : null);
	}
	
	/* (non-Javadoc)
	 * @see com.makao.dao.IOrderOnDao#isExist(int, int, java.lang.String)
	 * 根据订单号查询订单是否存在
	 */
	@Override
	public boolean isExist(int cityId, String orderNum) {
		String tableName = "Order_"+cityId+"_on";
		String sql = "SELECT count(id) as count FROM "+tableName+ " WHERE `number`='"+orderNum+"' AND `status`='"+OrderState.NOT_PAID.getCode()+"'";
		Session session = null;
		Transaction tx = null;
		List<Integer> res = new ArrayList<Integer>();
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
		if(res.size()<=0){
			return false;
		}
		if(res.get(0)>0){
			return true;
		}
		return false;
	}
	

	/* (non-Javadoc)
	 * @see com.makao.dao.IOrderOnDao#processOrder(int, java.lang.String)
	 * 将排队中的订单状态改为待处理
	 */
	@Override
	public OrderOn processOrder(int cityId, String orderid) {
		String tableName = "Order_"+cityId+"_on";
		String history = ","+OrderState.PROCESS_WAITING.getCode()+"="+new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `status`='"+OrderState.PROCESS_WAITING.getCode()+"',`history`=concat(`history`,'"+history+"') WHERE `id`="+orderid;
		String sql2 = "SELECT * FROM "+tableName+ " WHERE `id`="+orderid;
		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new LinkedList<OrderOn>();
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.doWork(
			// 定义一个匿名类，实现了Work接口
			new Work() {
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;PreparedStatement ps2 = null;
					try {
						ps = connection.prepareStatement(sql);
						int returncount = ps.executeUpdate();
						if(returncount!=0){//如果执行成功，则查询order对象并返回
							ps2=connection.prepareStatement(sql2);
							ResultSet rs = ps2.executeQuery();
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
								p.setPoint(rs.getInt("point"));
								p.setSender(rs.getString("sender"));
								p.setSenderPhone(rs.getString("senderPhone"));
								res.add(p);
							}
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
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
		return (res.size()>0 ? res.get(0) : null);
	}
	
	/* (non-Javadoc)
	 * @see com.makao.dao.IOrderOnDao#appoachOrders()
	 * 从数据库中找出需要将状态从排队中改为待处理的订单，将其状态设为待处理
    	当配送时间起点-准备时间<=当前时间时的订单满足条件
	 */
	@Override
	public List<OrderOn> appoachOrders(int cityid) {
		String tableName = "Order_"+cityid+"_on";
		String sql1 = "SELECT * FROM "+ tableName + " WHERE `status`='"+OrderState.QUEUE.getCode()+"'";

		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new ArrayList<OrderOn>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(sql1);
						ResultSet rs = ps.executeQuery();
						while(rs.next()){
							String receiveTime = rs.getString("receiveTime");
							String receiveTime_begin = receiveTime.substring(0, 16);//取得收货的开始时间
							int min_diff = TimeUtil.minitesDiff(receiveTime_begin);//收货开始时间与当前时间的分钟差
							logger.info("city_area_id:"+cityid+"_"+rs.getInt("areaId")+"_"+rs.getInt("id")+"; receiveTime:"+receiveTime+"; min_diff: "+min_diff);
							if(min_diff<=MakaoConstants.PRETIME){
								int o_id = rs.getInt("id");
								String history = ","+OrderState.PROCESS_WAITING.getCode()+"="+new Timestamp(System.currentTimeMillis());
								String sql2 = "UPDATE `"
										+ tableName
										+ "` SET `status`='"+OrderState.PROCESS_WAITING.getCode()+"',`history`=concat(`history`,'"+history+"') WHERE `id`="+o_id;
								PreparedStatement ps2 = null;
								try {
									ps2 = connection.prepareStatement(sql2);
									int returncount = ps2.executeUpdate();
									if(returncount!=0){//如果执行成功，则order返回
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
										p.setPoint(rs.getInt("point"));
										p.setSender(rs.getString("sender"));
										p.setSenderPhone(rs.getString("senderPhone"));
										res.add(p);
										logger.info("成功将订单移到待处理列表："+"area:"+rs.getInt("areaId")+"; orderid:"+o_id);
									}
								} finally {
									doClose(ps2);
								}
							}
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
		return res.size()>0 ? res : null;
	}
	
	/* (non-Javadoc)
	 * @see com.makao.dao.IOrderOnDao#confirmOrders(int)
	 * 订单配送完一定时间后，如果其状态还是已配送(即用户没有手动确认收货)，自动将其状态改为已收货
	 */
	@Override
	public List<String> confirmOrders(int cityid) {
		String tableName = "Order_"+cityid+"_on";
		String Order_cityId_on = "Order_"+cityid+"_on";
		String Order_cityId_off = "Order_"+cityid+"_off";
		String sql1 = "SELECT * FROM "+ tableName + " WHERE `status`='"+OrderState.DISTRIBUTED.getCode()+"'";
		String sql2 = "INSERT INTO `"
				+ Order_cityId_off
				+ "` (`number`,`productIds`,`productNames`,`orderTime`,`receiverName`,`phoneNumber`,`address`,`payType`,"
				+ "`receiveType`,`receiveTime`,`couponId`,`couponPrice`,`totalPrice`,"
				+ "`freight`,`comment`,`vcomment`,`finalStatus`,`cityarea`,`finalTime`,`userId`,`areaId`,`cityId`,`refundStatus`,`history`,`point`,`sender`,`senderPhone`,`pcomments`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Session session = null;
		Transaction tx = null;
		List<String> res = new ArrayList<String>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					PreparedStatement ps2 = null;
					PreparedStatement ps3 = null;
					try {
						ps = connection.prepareStatement(sql1);
						ResultSet rs = ps.executeQuery();
						while(rs.next()){
							String hist = rs.getString("history");
							String ditributed_time = "";
							for(String str:hist.split(",")){
								if(str.indexOf(OrderState.DISTRIBUTED.getCode()+"=")==0){//取出'已配送=xxxx:xx:xx'的时间
									ditributed_time=str.split("=")[1];
									break;
								}
							}
							if(!"".equals(ditributed_time)){
								int min_diff = TimeUtil.minitesDiff(ditributed_time);//完成配送的时间与当前时间的分钟差
								//如果时间差(是负数)小于预先定义的COMFIRMTIME，系统帮助确认收货
								if(min_diff<=MakaoConstants.COMFIRMTIME){
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
									p.setPoint(rs.getInt("point"));
									p.setSender(rs.getString("sender"));
									p.setSenderPhone(rs.getString("senderPhone"));
									
									String sql3 = "DELETE FROM `"+Order_cityId_on+"` WHERE `id`="+rs.getInt("id");
									ps2 = connection.prepareStatement(sql2);
									ps2.setString(1, rs.getString("number"));
									ps2.setString(2, rs.getString("productIds"));
									ps2.setString(3, rs.getString("productNames"));
									ps2.setTimestamp(4, rs.getTimestamp("orderTime"));
									ps2.setString(5, rs.getString("receiverName"));
									ps2.setString(6, rs.getString("phoneNumber"));
									ps2.setString(7, rs.getString("address"));
									ps2.setString(8, rs.getString("payType"));
									ps2.setString(9, rs.getString("receiveType"));
									ps2.setString(10, rs.getString("receiveTime"));
									ps2.setInt(11, rs.getInt("couponId"));
									ps2.setString(12, rs.getString("couponPrice"));
									ps2.setString(13, rs.getString("totalPrice"));
									ps2.setString(14, rs.getString("freight"));
									ps2.setString(15, rs.getString("comment"));
									ps2.setString(16, rs.getString("vcomment"));
									ps2.setString(17, OrderState.RECEIVED.getCode()+"");
									ps2.setString(18, rs.getString("cityarea"));
									ps2.setTimestamp(19, new Timestamp(System.currentTimeMillis()));
									ps2.setInt(20, rs.getInt("userId"));
									ps2.setInt(21, rs.getInt("areaId"));
									ps2.setInt(22, rs.getInt("cityId"));
									ps2.setString(23, "无需退款");//正常完成的订单，退款状态为无
									ps2.setString(24, rs.getString("history")+","+OrderState.RECEIVED.getCode()+"="+new Timestamp(System.currentTimeMillis()));
									ps2.setInt(25, rs.getInt("point"));
									ps2.setString(26,rs.getString("sender"));
									ps2.setString(27, rs.getString("senderPhone"));
									ps2.setString(28, "");
									ps2.executeUpdate();
									
									ps3 = connection.prepareStatement(sql3);
									ps3.executeUpdate();
									String comfirmed = rs.getInt("cityId")+"_"+rs.getInt("areaId")+"_"+rs.getString("number");
									res.add(comfirmed);
									logger.info("自动确认收货订单(cityid_areaid_number)："+comfirmed);
								}
							}
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
	
	@Override
	public int vcommentOrder(int cityId, int orderid, String vcomment) {
		String tableName = "Order_"+cityId+"_on";
		String vComment = vcomment;
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `vcomment`='"+vComment+"' WHERE `id`="+orderid;
		Session session = null;
		Transaction tx = null;
		int res = 0;
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
	 * @see com.makao.dao.IOrderOnDao#unPaidOrders(int)
	 * 数据库中查到所有15分钟内未支付或支付失败的订单，同时删除它们，并且返回订单列表
	 */
	@Override
	public List<OrderOn> unPaidOrders(int id) {
		String tableName = "Order_"+id+"_on";
		String sql1 = "SELECT * FROM "+ tableName + " WHERE `status`='"+OrderState.NOT_PAID.getCode()+"'";

		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new ArrayList<OrderOn>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;PreparedStatement ps2 = null;
					try {
						ps = connection.prepareStatement(sql1);
						ResultSet rs = ps.executeQuery();
						while(rs.next()){
							String orderTime = rs.getTimestamp("orderTime").toString();
							int min_diff = TimeUtil.minitesDiff(orderTime);//下单时间与当前时间的分钟差
							if(min_diff<=MakaoConstants.REMOVETIME){
								logger.info("[unPaidOrders]city_area_id:"+id+"_"+rs.getInt("areaId")+"_"+rs.getInt("id")+"; orderTime:"+orderTime+"; min_diff: "+min_diff);
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
								p.setPoint(rs.getInt("point"));
								p.setSender(rs.getString("sender"));
								p.setSenderPhone(rs.getString("senderPhone"));
								res.add(p);
								logger.info("成功将订单删除："+"area:"+rs.getInt("areaId")+"; orderid:"+rs.getInt("id"));
								
								String sql3 = "DELETE FROM `"+tableName+"` WHERE `id`="+rs.getInt("id");
								ps2 = connection.prepareStatement(sql3);
								ps2.executeUpdate();
							}
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
		return res.size()>0 ? res : null;
	}
	
	/* (non-Javadoc)
	 * @see com.makao.dao.IOrderOnDao#userCancelOrder(int, int)
	 * 用户取消订单
	 */
	@Override
	public OrderOn userCancelOrder(int cityid, int orderid) {
		String tableName = "Order_"+cityid+"_on";
		String Order_cityId_on = "Order_"+cityid+"_on";
		String Order_cityId_off = "Order_"+cityid+"_off";
		String sql1 = "SELECT * FROM "+ tableName + " WHERE `id`="+orderid;
		String sql2 = "INSERT INTO `"
				+ Order_cityId_off
				+ "` (`number`,`productIds`,`productNames`,`orderTime`,`receiverName`,`phoneNumber`,`address`,`payType`,"
				+ "`receiveType`,`receiveTime`,`couponId`,`couponPrice`,`totalPrice`,"
				+ "`freight`,`comment`,`vcomment`,`finalStatus`,`cityarea`,`finalTime`,`userId`,`areaId`,`cityId`,`refundStatus`,`history`,`point`,`sender`,`senderPhone`,`pcomments`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Session session = null;
		Transaction tx = null;
		List<OrderOn> res = new ArrayList<OrderOn>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					PreparedStatement ps2 = null;
					PreparedStatement ps3 = null;
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
									p.setPoint(rs.getInt("point"));
									p.setSender(rs.getString("sender"));
									p.setSenderPhone(rs.getString("senderPhone"));
									
									String sql3 = "DELETE FROM `"+Order_cityId_on+"` WHERE `id`="+rs.getInt("id");
									ps2 = connection.prepareStatement(sql2);
									ps2.setString(1, rs.getString("number"));
									ps2.setString(2, rs.getString("productIds"));
									ps2.setString(3, rs.getString("productNames"));
									ps2.setTimestamp(4, rs.getTimestamp("orderTime"));
									ps2.setString(5, rs.getString("receiverName"));
									ps2.setString(6, rs.getString("phoneNumber"));
									ps2.setString(7, rs.getString("address"));
									ps2.setString(8, rs.getString("payType"));
									ps2.setString(9, rs.getString("receiveType"));
									ps2.setString(10, rs.getString("receiveTime"));
									ps2.setInt(11, rs.getInt("couponId"));
									ps2.setString(12, rs.getString("couponPrice"));
									ps2.setString(13, rs.getString("totalPrice"));
									ps2.setString(14, rs.getString("freight"));
									ps2.setString(15, rs.getString("comment"));
									ps2.setString(16, rs.getString("vcomment"));
									ps2.setString(17, OrderState.CANCELED.getCode()+"");
									ps2.setString(18, rs.getString("cityarea"));
									ps2.setTimestamp(19, new Timestamp(System.currentTimeMillis()));
									ps2.setInt(20, rs.getInt("userId"));
									ps2.setInt(21, rs.getInt("areaId"));
									ps2.setInt(22, rs.getInt("cityId"));
									if("1".equals(rs.getString("status")))//未付款的无需退款
										ps2.setString(23, "无需退款");
									else
										ps2.setString(23, "待退款");
									ps2.setString(24, rs.getString("history")+","+OrderState.CANCELED.getCode()+"="+new Timestamp(System.currentTimeMillis()));
									ps2.setInt(25, rs.getInt("point"));
									ps2.setString(26,rs.getString("sender"));
									ps2.setString(27, rs.getString("senderPhone"));
									ps2.setString(28, "");
									ps2.executeUpdate();
									
									ps3 = connection.prepareStatement(sql3);
									ps3.executeUpdate();
									String comfirmed = rs.getInt("cityId")+"_"+rs.getInt("areaId")+"_"+rs.getString("number");
									res.add(p);
									logger.info("自动确认收货订单(cityid_areaid_number)："+comfirmed);
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
		return res.size()>0 ? res.get(0) : null;
	}
	

	/* (non-Javadoc)
	 * @see com.makao.dao.IOrderOnDao#queryProcessAndReturnByAreaId(int, int)
	 * 获取待处理和待退货的订单的数量
	 */
	@Override
	public String queryProcessAndReturnByAreaId(int cityId, int areaId) {
		String tableName1 = "Order_"+cityId+"_on";
		String tableName2 = "Order_"+cityId+"_off";
		String sql = "SELECT count(id) as count FROM "+ tableName1 + " WHERE `areaId`="+areaId+" AND `status`='"+OrderState.PROCESS_WAITING.getCode()+"' Order By `receiveTime`";
		String sql2 = "SELECT count(id) as count FROM "+ tableName2 + " WHERE `areaId`="+areaId+" AND `finalStatus`='"+OrderState.RETURN_APPLYING.getCode()+"' Order By `receiveTime`";
		Session session = null;
		Transaction tx = null;
		List<Integer> res = new LinkedList<Integer>();
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
						rs.next();
						res.add(rs.getInt("count"));
						ps2 = connection.prepareStatement(sql2);
						ResultSet rs2 = ps2.executeQuery();
						rs2.next();
						res.add(rs2.getInt("count"));
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
		if(res.size()<=1)
			return "0_0";
		else
			return res.get(0)+"_"+res.get(1);
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
