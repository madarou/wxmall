package com.makao.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.makao.dao.IPointDao;
import com.makao.entity.History;
import com.makao.entity.Point;
import com.makao.entity.PointLog;
import com.mysql.jdbc.Statement;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class PointDaoImpl implements IPointDao {
	private static final Logger logger = Logger.getLogger(PointDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(Point point) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Point point) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Point> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Point> queryByName(String name) {
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
	public int insertPointLog(PointLog pl) {
		String tableName = "PointLog_"+pl.getCityId();
		String sql = "INSERT INTO `"
				+ tableName
				+ "` (`name`,`point`,`getDate`,`comment`,`cityId`,`userId`)"
				+ " VALUES (?,?,?,?,?,?)";
		Session session = null;
		Transaction tx = null;
		List<Integer> res = new ArrayList<Integer>();// 返回0表示失败，成功则返回id
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
						ps.setString(1, pl.getName());
						ps.setInt(2, pl.getPoint());
						ps.setDate(3, pl.getGetDate());
						ps.setString(4, pl.getComment());
						ps.setInt(5, pl.getCityId());
						ps.setInt(6, pl.getUserId());
						int row = ps.executeUpdate();
						ResultSet rs = ps.getGeneratedKeys();  
					     if ( rs.next() ) {  
					    	 int key = rs.getInt(row);  
					         logger.info("插入的comment id:"+key);  
					         res.add(key);
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
		return (res.get(0)!=0)?res.get(0):0;
	}
	
	@Override
	public List<PointLog> queryPointLog(String tableName, int userid) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `userId`="+userid+" Order By `id` DESC";
		Session session = null;
		Transaction tx = null;
		List<PointLog> res = new LinkedList<PointLog>();
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
							PointLog p = new PointLog();
							p.setId(rs.getInt("id"));
							p.setName(rs.getString("name"));
							p.setPoint(rs.getInt("point"));
							p.setGetDate(rs.getDate("getDate"));
							p.setComment(rs.getString("comment"));
							p.setCityId(rs.getInt("cityId"));
							p.setUserId(rs.getInt("userId"));
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
