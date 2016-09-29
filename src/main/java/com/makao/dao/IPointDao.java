package com.makao.dao;

import java.util.List;

import com.makao.entity.Point;
import com.makao.entity.PointLog;

public interface IPointDao {

    public int insert(Point point);

    public Point getById(int id);

    public int update(Point point);
    
    public List<Point> queryAll();
    
    public List<Point> queryByName(String name);

	public void testor();

	public int deleteById(int id);

	/**
	 * @param pl
	 * @return
	 * 插入积分记录
	 */
	public int insertPointLog(PointLog pl);

	/**
	 * @param string
	 * @param userid
	 * @return
	 * 查询用户的积分记录
	 */
	public List<PointLog> queryPointLog(String string, int userid);
}