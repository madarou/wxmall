package com.makao.dao.impl;

import java.sql.Connection;
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

import com.makao.dao.ICommentDao;
import com.makao.entity.Comment;
import com.makao.entity.CouponOn;
import com.mysql.jdbc.Statement;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class CommentDaoImpl implements ICommentDao{
	private static final Logger logger = Logger.getLogger(CommentDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(Comment comment) {
		String tableName = "Comment_"+comment.getCityId()+"_"+comment.getAreaId();
		String sql = "INSERT INTO `"
				+ tableName
				+ "` (`userName`,`userId`,`userImgUrl`,`date`,`likes`,`content`,`productId`,`cityId`,"
				+ "`areaId`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?)";
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
						ps.setString(1, comment.getUserName());
						ps.setInt(2, comment.getUserId());
						ps.setString(3, comment.getUserImgUrl());
						ps.setDate(4, comment.getDate());
						ps.setInt(5, comment.getLikes());
						ps.setString(6, comment.getContent());
						ps.setInt(7, comment.getProductId());
						ps.setInt(8, comment.getCityId());
						ps.setInt(9, comment.getAreaId());
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
			res.add(0);
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
		return (res.get(0)!=0)?res.get(0):0;
	}

	@Override
	public Comment getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Comment comment) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Comment> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Comment> queryByName(String name) {
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
	public int like(int cityId, int areaId, int commentId) {
		String tableName = "Comment_"+cityId+"_"+areaId;
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `likes`=`likes`+1 WHERE `id`=" + commentId;
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
	public List<Comment> queryProductComments(String tableName,
			Integer productId) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `productId`="+productId+" Order By `date` Desc";
		Session session = null;
		Transaction tx = null;
		List<Comment> res = new LinkedList<Comment>();
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
							Comment p = new Comment();
							p.setId(rs.getInt("id"));
							p.setUserName(rs.getString("userName"));
							p.setUserId(rs.getInt("userId"));
							p.setUserImgUrl(rs.getString("userImgUrl"));
							p.setDate(rs.getDate("date"));
							p.setLikes(rs.getInt("likes"));
							p.setContent(rs.getString("content"));
							p.setProductId(rs.getInt("productId"));
							p.setCityId(rs.getInt("cityId"));
							p.setAreaId(rs.getInt("areaId"));
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
	

	/* (non-Javadoc)
	 * @see com.makao.dao.ICommentDao#queryUserComments(java.lang.String, java.lang.Integer, java.lang.Integer)
	 * 加载用户userId对商品productId的所有评论
	 */
	@Override
	public List<Comment> queryUserComments(String tableName, Integer userId,
			Integer productId) {
		String sql = "SELECT * FROM "+ tableName + " WHERE `productId`="+productId+" AND `userId`="+userId+" Order By `date` Desc";
		Session session = null;
		Transaction tx = null;
		List<Comment> res = new LinkedList<Comment>();
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
							Comment p = new Comment();
							p.setId(rs.getInt("id"));
							p.setUserName(rs.getString("userName"));
							p.setUserId(rs.getInt("userId"));
							p.setUserImgUrl(rs.getString("userImgUrl"));
							p.setDate(rs.getDate("date"));
							p.setLikes(rs.getInt("likes"));
							p.setContent(rs.getString("content"));
							p.setProductId(rs.getInt("productId"));
							p.setCityId(rs.getInt("cityId"));
							p.setAreaId(rs.getInt("areaId"));
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
