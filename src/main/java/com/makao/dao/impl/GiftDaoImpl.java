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

import com.makao.dao.IGiftDao;
import com.makao.entity.Gift;
import com.makao.entity.Product;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class GiftDaoImpl implements IGiftDao {
	private static final Logger logger = Logger.getLogger(GiftDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(Gift gift) {
		String tableName = "Gift_"+gift.getCityId();
		String sql = "INSERT INTO `"
				+ tableName
				+ "` (`name`,`coverSUrl`,`coverBUrl`,`point`,`comment`,`from`,`to`,`inventory`,"
				+ "`remain`,`type`,`areaId`,`areaName`,`cityId`)"
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
						ps = connection.prepareStatement(sql);
						ps.setString(1, gift.getName());
						ps.setString(2, gift.getCoverSUrl());
						ps.setString(3, gift.getCoverBUrl());
						ps.setInt(4, gift.getPoint());
						ps.setString(5, gift.getComment());
						ps.setDate(6, gift.getFrom());
						ps.setDate(7, gift.getTo());
						ps.setInt(8, gift.getInventory());
						ps.setInt(9, gift.getRemain());
						ps.setString(10, gift.getType());
						ps.setInt(11, gift.getAreaId());
						ps.setString(12, gift.getAreaName());
						ps.setInt(13, gift.getCityId());
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
	public Gift getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Gift gift) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Gift> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Gift> queryByName(String name) {
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
	public List<Gift> queryByCityAreaId(int cityId, int areaId) {
		String tableName = "Gift_"+cityId;
		String sql = "SELECT * FROM "+tableName+" WHERE areaId="+areaId;
		Session session = null;
		Transaction tx = null;
		List<Gift> res = new LinkedList<Gift>();
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
							Gift p = new Gift();
							p.setId(rs.getInt("id"));
							p.setName(rs.getString("name"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setPoint(rs.getInt("point"));
							p.setComment(rs.getString("comment"));
							p.setFrom(rs.getDate("from"));
							p.setTo(rs.getDate("to"));
							p.setInventory(rs.getInt("inventory"));
							p.setRemain(rs.getInt("remain"));
							p.setType(rs.getString("type"));
							p.setAreaId(rs.getInt("areaId"));
							p.setAreaName(rs.getString("areaName"));
							p.setCityId(rs.getInt("cityId"));
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
		return (res.size()>0 ? res : null);
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
