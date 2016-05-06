package com.makao.service;

import java.util.List;

import com.makao.entity.Point;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IPointService {

	com.makao.entity.Point getPointById(Integer id);

	int deletePoint(Integer id);

	int insertPoint(Point point);

	int updatePoint(Point point);

	List<Point> queryPointByName(String name);

	List<Point> queryAllPoints();

}
