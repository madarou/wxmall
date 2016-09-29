package com.makao.service;

import java.util.List;

import com.makao.entity.Point;
import com.makao.entity.PointLog;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IPointService {

	int insert(Point point);

	int update(Point point);

	List<Point> queryByName(String name);

	List<Point> queryAll();

	Point getById(int id);

	int deleteById(int id);

	/**
	 * @param pl
	 * 插入积分记录
	 */
	int insertPointLog(PointLog pl);

	/**
	 * @param string
	 * @param userid
	 * @return
	 * 查询用户积分记录
	 */
	List<PointLog> queryPointLog(String string, int userid);

}
