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

import com.makao.dao.IProductDao;
import com.makao.entity.Product;
import com.makao.entity.User;
import com.mysql.jdbc.Statement;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class ProductDaoImpl implements IProductDao {
	private static final Logger logger = Logger.getLogger(ProductDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(Product product) {
		String tableName = "Product_"+product.getCityId()+"_"+product.getAreaId();
		String sql = "INSERT INTO `"
				+ tableName
				+ "` (`productName`,`catalog`,`label`,`standard`,`price`,`marketPrice`,`inventory`,`isShow`,"
				+ "`showWay`,`sequence`,`description`,`origin`,`status`,"
				+ "`salesVolume`,`likes`,`coverSUrl`,`coverBUrl`,`subdetailUrl`,`detailUrl`,`areaId`,`cityId`,`threhold`,"
				+ "`prethrehold`,`supply`,`restrict`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
						ps.setString(1, product.getProductName());
						ps.setString(2, product.getCatalog());
						ps.setString(3, product.getLabel());
						ps.setString(4, product.getStandard());
						ps.setString(5, product.getPrice());
						ps.setString(6, product.getMarketPrice());
						ps.setInt(7, product.getInventory());
						ps.setString(8, product.getIsShow());
						ps.setString(9, product.getShowWay());
						ps.setInt(10, product.getSequence());
						ps.setString(11, product.getDescription());
						ps.setString(12, product.getOrigin());
						ps.setString(13, product.getStatus());
						ps.setInt(14, product.getSalesVolume());
						ps.setInt(15, product.getLikes());
						ps.setString(16, product.getCoverSUrl());
						ps.setString(17, product.getCoverBUrl());
						ps.setString(18, product.getSubdetailUrl());
						ps.setString(19, product.getDetailUrl());
						ps.setInt(20, product.getAreaId());
						ps.setInt(21, product.getCityId());
						ps.setInt(22, product.getThrehold());
						ps.setInt(23, product.getPrethrehold());
						ps.setInt(24, product.getSupply());
						ps.setInt(25, product.getRestrict());
						int row = ps.executeUpdate();
						ResultSet rs = ps.getGeneratedKeys();  
					     if ( rs.next() ) {  
					    	 int key = rs.getInt(row);  
					         logger.info("插入的product id:"+key);  
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
	public Product getById(int id, int cityId, int areaId) {
		String tableName = "Product_"+cityId+"_"+areaId;
		String sql = "SELECT * FROM `"
				+ tableName
				+ "` WHERE id = ?";
		Session session = null;
		Transaction tx = null;
		List<Product> products = new ArrayList<Product>();
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
							Product p = new Product();
							p.setId(rs.getInt("id"));
							p.setProductName(rs.getString("productName"));
							p.setCatalog(rs.getString("catalog"));
							p.setShowWay(rs.getString("showWay"));
							p.setPrice(rs.getString("price"));
							p.setStandard(rs.getString("standard"));
							p.setMarketPrice(rs.getString("marketPrice"));
							p.setLabel(rs.getString("label"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setInventory(rs.getInt("inventory"));
							p.setSequence(rs.getInt("sequence"));
							p.setStatus(rs.getString("status"));
							p.setDescription(rs.getString("description"));
							p.setOrigin(rs.getString("origin"));
							p.setSalesVolume(rs.getInt("salesVolume"));
							p.setLikes(rs.getInt("likes"));
							p.setSubdetailUrl(rs.getString("subdetailUrl"));
							p.setDetailUrl(rs.getString("detailUrl"));
							p.setIsShow(rs.getString("isShow"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setThrehold(rs.getInt("threhold"));
							p.setPrethrehold(rs.getInt("prethrehold"));
							p.setSupply(rs.getInt("supply"));
							p.setRestrict(rs.getInt("restrict"));
							products.add(p);
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
		return products.size()>0 ? products.get(0) : null;
	}

	@Override
	public int update(Product product) {
		String tableName = "Product_"+product.getCityId()+"_"+product.getAreaId();
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `productName`='"+product.getProductName()+"',"
						+ "`catalog`='"+product.getCatalog()+"',"
								+ "`label`='"+product.getLabel()+"',"
										+ "`standard`='"+product.getStandard()+"',"
												+ "`price`='"+product.getPrice()+"',"
														+ "`marketPrice`='"+product.getMarketPrice()+"',"
																+ "`inventory`="+product.getInventory()+","
																		+ "`isShow`='"+product.getIsShow()+"',"
				+ "`showWay`='"+product.getShowWay()+"',"
						+ "`sequence`="+product.getSequence()+","
								+ "`description`='"+product.getDescription()+"',"
										+ "`origin`='"+product.getOrigin()+"',"
												+ "`status`='"+product.getStatus()+"',"
				+ "`salesVolume`="+product.getSalesVolume()+","
						+ "`likes`="+product.getLikes()+","
								+ "`coverSUrl`='"+product.getCoverSUrl()+"',"
										+ "`coverBUrl`='"+product.getCoverBUrl()+"',"
												+ "`subdetailUrl`='"+product.getSubdetailUrl()+"',"
														+ "`detailUrl`='"+product.getDetailUrl()+"',"
																+ "`areaId`="+product.getAreaId()+","
																		+ "`cityId`="+product.getCityId()+","
																		+ "`threhold`="+product.getThrehold()+","
																		+ "`prethrehold`="+product.getPrethrehold()+","
																		+ "`supply`="+product.getSupply()+","
																		+ "`restrict`="+product.getRestrict()
																				+ " WHERE `id`=" + product.getId();
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
	public List<Product> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> queryByName(String name) {
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
	public List<Product> queryByCityAreaId(int cityId, int areaId) {
		String tableName = "Product_"+cityId+"_"+areaId;
		String sql = "SELECT * FROM "+tableName + " WHERE `isShow`='yes'";
		Session session = null;
		Transaction tx = null;
		List<Product> res = new LinkedList<Product>();
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
						while(rs.next()){
							Product p = new Product();
							p.setId(rs.getInt("id"));
							p.setProductName(rs.getString("productName"));
							p.setCatalog(rs.getString("catalog"));
							p.setShowWay(rs.getString("showWay"));
							p.setPrice(rs.getString("price"));
							p.setStandard(rs.getString("standard"));
							p.setMarketPrice(rs.getString("marketPrice"));
							p.setLabel(rs.getString("label"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setInventory(rs.getInt("inventory"));
							p.setSequence(rs.getInt("sequence"));
							p.setStatus(rs.getString("status"));
							p.setDescription(rs.getString("description"));
							p.setOrigin(rs.getString("origin"));
							p.setSalesVolume(rs.getInt("salesVolume"));
							p.setLikes(rs.getInt("likes"));
							p.setDetailUrl(rs.getString("detailUrl"));
							p.setIsShow(rs.getString("isShow"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setThrehold(rs.getInt("threhold"));
							p.setPrethrehold(rs.getInt("prethrehold"));
							p.setSupply(rs.getInt("supply"));
							p.setRestrict(rs.getInt("restrict"));
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
		//return (res.size()>0 ? res : null);
		return res;
	}
	
	/* (non-Javadoc)
	 * @see com.makao.dao.IProductDao#queryRepProducts()
	 * 从总商品库中查询所有商品
	 */
	@Override
	public List<Product> queryRepProducts() {
		String tableName = "Product";
		String sql = "SELECT * FROM "+tableName;
		Session session = null;
		Transaction tx = null;
		List<Product> res = new LinkedList<Product>();
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
						while(rs.next()){
							Product p = new Product();
							p.setId(rs.getInt("id"));
							p.setProductName(rs.getString("productName"));
							p.setCatalog(rs.getString("catalog"));
							p.setShowWay(rs.getString("showWay"));
							p.setPrice(rs.getString("price"));
							p.setStandard(rs.getString("standard"));
							p.setMarketPrice(rs.getString("marketPrice"));
							p.setLabel(rs.getString("label"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setInventory(rs.getInt("inventory"));
							p.setSequence(rs.getInt("sequence"));
							p.setStatus(rs.getString("status"));
							p.setDescription(rs.getString("description"));
							p.setOrigin(rs.getString("origin"));
							p.setSalesVolume(rs.getInt("salesVolume"));
							p.setLikes(rs.getInt("likes"));
							p.setDetailUrl(rs.getString("detailUrl"));
							p.setIsShow(rs.getString("isShow"));
							p.setAreaId(rs.getInt("areaId"));
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
	
	/* (non-Javadoc)
	 * @see com.makao.dao.IProductDao#notShowProduct(java.lang.String, int)
	 * 下架产品
	 */
	@Override
	public int notShowProduct(String tableName, int prodcutId) {
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `isShow`='no' WHERE `id`="+prodcutId;
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
	 * @see com.makao.dao.IProductDao#notShowProduct(java.lang.String, int)
	 * 上架产品
	 */
	@Override
	public int showProduct(String tableName, int prodcutId) {
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `isShow`='yes' WHERE `id`="+prodcutId;
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
	 * @see com.makao.dao.IProductDao#updateRepProduct(com.makao.entity.Product)
	 * 更新总表
	 */
	@Override
	public int updateRepProduct(Product product) {
		String tableName = "Product";
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `productName`='"+product.getProductName()+"',"
						+ "`catalog`='"+product.getCatalog()+"',"
								+ "`label`='"+product.getLabel()+"',"
										+ "`standard`='"+product.getStandard()+"',"
												+ "`price`='"+product.getPrice()+"',"
														+ "`marketPrice`='"+product.getMarketPrice()+"',"
																+ "`inventory`="+product.getInventory()+","
																		+ "`isShow`='"+product.getIsShow()+"',"
						+ "`sequence`="+product.getSequence()+","
								+ "`description`='"+product.getDescription()+"',"
										+ "`origin`='"+product.getOrigin()+"',"
												+ "`status`='"+product.getStatus()+"',"
				+ "`salesVolume`="+product.getSalesVolume()+","
						+ "`likes`="+product.getLikes()+","
								+ "`coverSUrl`='"+product.getCoverSUrl()+"',"
										+ "`coverBUrl`='"+product.getCoverBUrl()+"',"
												+ "`subdetailUrl`='"+product.getSubdetailUrl()+"',"
														+ "`detailUrl`='"+product.getDetailUrl()+"',"
																+ "`areaId`="+product.getAreaId()+","
																		+ "`cityId`="+product.getCityId()
																				+ " WHERE `id`=" + product.getId();
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
	public int getRecordCount(int cityId, int areaId) {
		String tableName = "Product_"+cityId+"_"+areaId;
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
	public List<Product> queryFromToIndex(int cityId, int areaId, int from,
			int to) {
		String tableName = "Product_"+cityId+"_"+areaId;
		String sql = "SELECT * FROM "+tableName+" WHERE `id` BETWEEN "+from+" AND "+to;
		Session session = null;
		Transaction tx = null;
		List<Product> res = new LinkedList<Product>();
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
						while(rs.next()){
							Product p = new Product();
							p.setId(rs.getInt("id"));
							p.setProductName(rs.getString("productName"));
							p.setCatalog(rs.getString("catalog"));
							p.setShowWay(rs.getString("showWay"));
							p.setPrice(rs.getString("price"));
							p.setStandard(rs.getString("standard"));
							p.setMarketPrice(rs.getString("marketPrice"));
							p.setLabel(rs.getString("label"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setInventory(rs.getInt("inventory"));
							p.setSequence(rs.getInt("sequence"));
							p.setStatus(rs.getString("status"));
							p.setDescription(rs.getString("description"));
							p.setOrigin(rs.getString("origin"));
							p.setSalesVolume(rs.getInt("salesVolume"));
							p.setLikes(rs.getInt("likes"));
							p.setDetailUrl(rs.getString("detailUrl"));
							p.setIsShow(rs.getString("isShow"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setThrehold(rs.getInt("threhold"));
							p.setPrethrehold(rs.getInt("prethrehold"));
							p.setSupply(rs.getInt("supply"));
							p.setRestrict(rs.getInt("restrict"));
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
	

	@Override
	public int getRecordCount() {
		String tableName = "Product";
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
	public List<Product> queryFromToIndex(int from, int to) {
		String tableName = "Product";
		String sql = "SELECT * FROM "+tableName+" WHERE `id` BETWEEN "+from+" AND "+to;
		Session session = null;
		Transaction tx = null;
		List<Product> res = new LinkedList<Product>();
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
						while(rs.next()){
							Product p = new Product();
							p.setId(rs.getInt("id"));
							p.setProductName(rs.getString("productName"));
							p.setCatalog(rs.getString("catalog"));
							p.setShowWay(rs.getString("showWay"));
							p.setPrice(rs.getString("price"));
							p.setStandard(rs.getString("standard"));
							p.setMarketPrice(rs.getString("marketPrice"));
							p.setLabel(rs.getString("label"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setInventory(rs.getInt("inventory"));
							p.setSequence(rs.getInt("sequence"));
							p.setStatus(rs.getString("status"));
							p.setDescription(rs.getString("description"));
							p.setOrigin(rs.getString("origin"));
							p.setSalesVolume(rs.getInt("salesVolume"));
							p.setLikes(rs.getInt("likes"));
							p.setDetailUrl(rs.getString("detailUrl"));
							p.setIsShow(rs.getString("isShow"));
							p.setAreaId(rs.getInt("areaId"));
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
	
	@Override
	public int like(String tableName, int productId) {
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `likes`=`likes`+1 WHERE `id`=" + productId;
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
	 * @see com.makao.dao.IProductDao#getInventory(int, int, java.lang.String)
	 * 从Product_cityId_areaId中获取产品id的库存
	 */
	@Override
	public int getInventory(int cityId, int areaId, String id) {
		String tableName = "Product_"+cityId+"_"+areaId;
		String sql = "SELECT `inventory` FROM "+tableName+ " WHERE `id`="+id;
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
						rs.next();
						res.add(rs.getInt("inventory"));
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
	public int insertToWhole(Product product) {
		String tableName = "Product";
		String sql = "INSERT INTO `"
				+ tableName
				+ "` (`productName`,`catalog`,`label`,`standard`,`price`,`marketPrice`,`inventory`,`isShow`,"
				+ "`showWay`,`sequence`,`description`,`origin`,`status`,"
				+ "`salesVolume`,`likes`,`coverSUrl`,`coverBUrl`,`subdetailUrl`,`detailUrl`,`areaId`,`cityId`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
						ps.setString(1, product.getProductName());
						ps.setString(2, product.getCatalog());
						ps.setString(3, product.getLabel());
						ps.setString(4, product.getStandard());
						ps.setString(5, product.getPrice());
						ps.setString(6, product.getMarketPrice());
						ps.setInt(7, product.getInventory());
						ps.setString(8, product.getIsShow());
						ps.setString(9, product.getShowWay());
						ps.setInt(10, product.getSequence());
						ps.setString(11, product.getDescription());
						ps.setString(12, product.getOrigin());
						ps.setString(13, product.getStatus());
						ps.setInt(14, product.getSalesVolume());
						ps.setInt(15, product.getLikes());
						ps.setString(16, product.getCoverSUrl());
						ps.setString(17, product.getCoverBUrl());
						ps.setString(18, product.getSubdetailUrl());
						ps.setString(19, product.getDetailUrl());
						ps.setInt(20, product.getAreaId());
						ps.setInt(21, product.getCityId());
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
	public int updateInventory(String tableName, String productid,
			String inventN) {
		logger.info("更新产品inventory(tableName-productId-inventory): "+tableName+"-"+productid+"-"+inventN);
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `inventory`="+Integer.valueOf(inventN)+" WHERE `id`=" + Integer.valueOf(productid);
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
	public List<Product> searchProduct(int cityId, int areaId, String keyword,
			String cat) {
		String tableName = "Product_"+cityId+"_"+areaId;
		Session session = null;
		Transaction tx = null;
		List<Product> res = new LinkedList<Product>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			//res = session.createQuery("from User").list();
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					String sql = "";
					if(cat!=null&&"选择分类...".equals(cat)){
						sql = "SELECT * FROM "+tableName+ " WHERE `productName` like '%"+keyword+"%'";
					}
					else{
						sql = "SELECT * FROM "+tableName+ " WHERE `productName` like '%"+keyword+"%' and `catalog`='"+cat+"'";
					}
					try {
						ps = connection.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();
						//int col = rs.getMetaData().getColumnCount();
						while(rs.next()){
							Product p = new Product();
							p.setId(rs.getInt("id"));
							p.setProductName(rs.getString("productName"));
							p.setCatalog(rs.getString("catalog"));
							p.setShowWay(rs.getString("showWay"));
							p.setPrice(rs.getString("price"));
							p.setStandard(rs.getString("standard"));
							p.setMarketPrice(rs.getString("marketPrice"));
							p.setLabel(rs.getString("label"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setInventory(rs.getInt("inventory"));
							p.setSequence(rs.getInt("sequence"));
							p.setStatus(rs.getString("status"));
							p.setDescription(rs.getString("description"));
							p.setOrigin(rs.getString("origin"));
							p.setSalesVolume(rs.getInt("salesVolume"));
							p.setLikes(rs.getInt("likes"));
							p.setDetailUrl(rs.getString("detailUrl"));
							p.setIsShow(rs.getString("isShow"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setThrehold(rs.getInt("threhold"));
							p.setPrethrehold(rs.getInt("prethrehold"));
							p.setSupply(rs.getInt("supply"));
							p.setRestrict(rs.getInt("restrict"));
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
	public List<Product> queryThreholds(String tableName) {
		String sql = "SELECT * FROM "+tableName+" t WHERE t.inventory<=t.threhold";
		Session session = null;
		Transaction tx = null;
		List<Product> res = new LinkedList<Product>();
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
						while(rs.next()){
							Product p = new Product();
							p.setId(rs.getInt("id"));
							p.setProductName(rs.getString("productName"));
							p.setCatalog(rs.getString("catalog"));
							p.setShowWay(rs.getString("showWay"));
							p.setPrice(rs.getString("price"));
							p.setStandard(rs.getString("standard"));
							p.setMarketPrice(rs.getString("marketPrice"));
							p.setLabel(rs.getString("label"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setInventory(rs.getInt("inventory"));
							p.setSequence(rs.getInt("sequence"));
							p.setStatus(rs.getString("status"));
							p.setDescription(rs.getString("description"));
							p.setOrigin(rs.getString("origin"));
							p.setSalesVolume(rs.getInt("salesVolume"));
							p.setLikes(rs.getInt("likes"));
							p.setDetailUrl(rs.getString("detailUrl"));
							p.setIsShow(rs.getString("isShow"));
							p.setAreaId(rs.getInt("areaId"));
							p.setCityId(rs.getInt("cityId"));
							p.setThrehold(rs.getInt("threhold"));
							p.setPrethrehold(rs.getInt("prethrehold"));
							p.setSupply(rs.getInt("supply"));
							p.setRestrict(rs.getInt("restrict"));
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
	public List<Product> searchRepProduct(String keyword) {
		String tableName = "Product";
		Session session = null;
		Transaction tx = null;
		List<Product> res = new LinkedList<Product>();
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			//res = session.createQuery("from User").list();
			session.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement ps = null;
					String sql = "SELECT * FROM "+tableName+ " WHERE `productName` like '%"+keyword+"%'";
					try {
						ps = connection.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();
						while(rs.next()){
							Product p = new Product();
							p.setId(rs.getInt("id"));
							p.setProductName(rs.getString("productName"));
							p.setCatalog(rs.getString("catalog"));
							p.setShowWay(rs.getString("showWay"));
							p.setPrice(rs.getString("price"));
							p.setStandard(rs.getString("standard"));
							p.setMarketPrice(rs.getString("marketPrice"));
							p.setLabel(rs.getString("label"));
							p.setCoverSUrl(rs.getString("coverSUrl"));
							p.setCoverBUrl(rs.getString("coverBUrl"));
							p.setInventory(rs.getInt("inventory"));
							p.setSequence(rs.getInt("sequence"));
							p.setStatus(rs.getString("status"));
							p.setDescription(rs.getString("description"));
							p.setOrigin(rs.getString("origin"));
							p.setSalesVolume(rs.getInt("salesVolume"));
							p.setLikes(rs.getInt("likes"));
							p.setDetailUrl(rs.getString("detailUrl"));
							p.setIsShow(rs.getString("isShow"));
							p.setAreaId(rs.getInt("areaId"));
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
		return res;
	}

	@Override
	public int supplyProduct(String tableName, int productId, int num) {
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `supply`="+num+" WHERE `id`=" + productId;
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
	public int suppliedProduct(String tableName, int productId, int num) {
		logger.info("完成补货，更新产品inventory(tableName-productId-inventory): "+tableName+"-"+productId+"-"+num);
		String sql = "UPDATE `"
				+ tableName
				+ "` SET `supply`=0,`inventory`="+num+" WHERE `id`=" + productId;
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
	public int updateSalesVolume(String tableName, String productid, int saled) {
		logger.info("更新销量，更新产品salesVolume(tableName-productId-saled): "+tableName+"-"+productid+"-"+saled);
		
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
					String sql = "UPDATE `"
							+ tableName
							+ "` SET `salesVolume`=`salesVolume`+"+saled+" WHERE `id`=" + productid;
//					if(saled>0)
//						sql = "UPDATE `"
//							+ tableName
//							+ "` SET `salesVolume`=`salesVolume`+"+saled+" WHERE `id`=" + productid;
//					else
//						sql = "UPDATE `"
//								+ tableName
//								+ "` SET `salesVolume`=`salesVolume`-"+saled+" WHERE `id`=" + productid;
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
	public int deleteProduct(String tableName, int prodcutId) {
		String sql = "DELETE FROM `"
				+ tableName
				+ "` WHERE `id`="+prodcutId;
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

}
