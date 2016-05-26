package com.makao.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.makao.dao.IAreaDao;
import com.makao.entity.Address;
import com.makao.entity.Area;
import com.makao.entity.Banner;
import com.makao.entity.City;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class AreaDaoImpl implements IAreaDao {
	private static final Logger logger = Logger.getLogger(AreaDaoImpl.class);
	@Resource
	private SessionFactory sessionFactory;
	@Override
	public int insert(Area area) {
		Session session = null;
		Transaction tx = null;
		int res = 0;// 返回0表示成功，1表示失败
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.save(area);// 保存区域
			//这是在数据库中建立AreaName_cityId_product表，要放到同一事务中，保证区域点增加时，其数据库表同时建立
			String tableName = "Product_"+area.getCityId()+"_"+area.getId();
			area.setProductTable(tableName);//这是才插入表明，因为前面save area之后就能通过area.getId()获取其表名了
			session.update(area);
			//同时更新所属的city表里的areas字段
			City city = (City) session.get(City.class, area.getCityId());
			String areas = city.getAreas();
			if(areas==null||"".equals(areas)){
				city.setAreas(area.getId()+"="+area.getAreaName());
			}
			else{
				city.setAreas(areas+","+area.getId()+"="+area.getAreaName());
			}
			String sql = "CREATE TABLE IF NOT EXISTS `"
					+ tableName
					+ "` (`id` int(11) NOT NULL AUTO_INCREMENT,"
					+ "`productName` varchar(30) NOT NULL,"
					+ "`catalog` varchar(30),"
					+ "`showWay` varchar(2) DEFAULT 's',"
					+ "`price` varchar(10),"
					+ "`standard` varchar(50),"
					+ "`marketPrice` varchar(10),"
					+ "`label` varchar(16),"
					+ "`coverSUrl` varchar(50),"
					+ "`coverBUrl` varchar(50),"
					+ "`inventory` int(11),"
					+ "`sequence` int(11),"
					+ "`status` varchar(30),"
					+ "`description` varchar(100),"
					+ "`origin` varchar(30),"
					+ "`salesVolume` int(11),"
					+ "`likes` int(11),"
					+ "`subdetailUrl` varchar(50),"
					+ "`detailUrl` varchar(50),"
					+ "`isShow` varchar(5) DEFAULT 'yes',"
					+ "`areaId` int(11),"
					+ "`cityId` int(11),"
					+ "PRIMARY KEY (`id`))";
			session.doWork(
					// 定义一个匿名类，实现了Work接口
					new Work() {
						public void execute(Connection connection) throws SQLException {
							PreparedStatement ps = null;
							try {
								ps = connection.prepareStatement(sql);
								ps.execute();
							} finally {
								doClose(ps);
							}
						}
					});
			tx.commit();// 提交事务
		} catch (HibernateException e) {
			if (null != tx)
				tx.rollback();// 回滚
			res = 1;
			logger.error(e.getMessage(), e);
		} finally {
			if (null != session)
				session.close();// 关闭回话
		}
		return res;
	}

	@Override
	public Area getById(int id) {
		Session session = null;
		Transaction tx = null;
		Area res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = (Area) session.get(Area.class, id);
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
	public int update(Area area) {
		Session session = null;
		Transaction tx = null;
		int res = 0;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			session.update(area);
			tx.commit();// 提交事务
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
	public List<Area> queryAll() {
		Session session = null;
		Transaction tx = null;
		List<Area> res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = session.createQuery("from Area").list();
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
	public List<Area> queryByName(String name) {
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
	public int editCatalog(Area area, String oldName, String newName,
			String sequenceNew, String productTable) {
		Session session = null;
		Transaction tx = null;
		String sql = "UPDATE `"
				+ productTable
				+ "` SET `catalog`='"+newName+"' WHERE `catalog`='"+oldName+"'";
		int res = 0;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			String[] catalogStr = area.getCatalogs().split(",");
			StringBuilder sb = new StringBuilder();
			for(String s : catalogStr){
				if(oldName.equals(s.split("=")[0].trim())){
					sb.append(newName+"="+sequenceNew+",");
				}
				else{
					sb.append(s+",");
				}
			}
			String catalogs = sb.toString();
			area.setCatalogs(catalogs.substring(0, catalogs.length()-1));
			session.update(area);
			session.createQuery("update Banner as b set b.catalogName=? where b.catalogName=? and b.areaId=?").
				setString(0,newName).setString(1,oldName).setInteger(2, area.getId()).executeUpdate();
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
			tx.commit();// 提交事务
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
	public int deleteCatalog(Area area, String catalogName, String productTable) {
		Session session = null;
		Transaction tx = null;
		String sql = "UPDATE `"
				+ productTable
				+ "` SET `catalog`='默认分类' WHERE `catalog`='"+catalogName+"'";
		int res = 0;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			String[] catalogStr = area.getCatalogs().split(",");
			StringBuilder sb = new StringBuilder();
			for(String s : catalogStr){
				if(catalogName.equals(s.split("=")[0].trim())){
					continue;
				}
				else{
					sb.append(s+",");
				}
			}
			String catalogs = sb.toString();
			area.setCatalogs(catalogs.substring(0, catalogs.length()-1));
			session.update(area);
			session.createQuery("delete from Banner as b where b.catalogName=? and b.areaId=?").setString(0,catalogName).setInteger(1, area.getId()).executeUpdate();
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
			tx.commit();// 提交事务
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
	 * @see com.makao.dao.IAreaDao#newCatalog(com.makao.entity.Area)
	 * 往area表里增加catalog字段内容，同时要往banner表里增加分类
	 */
	@Override
	public int newCatalog(Area area) {
		Session session = null;
		Transaction tx = null;
		int res = 0;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			String[] catalogStr = area.getCatalogs().split(",");
			for(String s : catalogStr){
				Banner b = new Banner();
				b.setCatalogName(s.split("=")[0].trim());
				b.setStatus("未配置");
				b.setCityId(area.getCityId());
				b.setAreaId(area.getId());
				session.save(b);
			}
			session.update(area);
			tx.commit();// 提交事务
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
	public int closeArea(int areaId) {
		Session session = null;
		Transaction tx = null;
		int res = 0;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			Area area = (Area) session.get(Area.class, areaId);
			area.setClosed("yes");
			session.update(area);
			tx.commit();// 提交事务
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
	public int openArea(int areaId) {
		Session session = null;
		Transaction tx = null;
		int res = 0;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			Area area = (Area) session.get(Area.class, areaId);
			area.setClosed("no");
			session.update(area);
			tx.commit();// 提交事务
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
	public List<Area> queryByCityId(int cityId) {
		Session session = null;
		Transaction tx = null;
		List<Area> res = null;
		try {
			session = sessionFactory.openSession();// 获取和数据库的回话
			tx = session.beginTransaction();// 事务开始
			res = session.createQuery("from Area a where a.cityId=?").setInteger(0, cityId).list();
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
