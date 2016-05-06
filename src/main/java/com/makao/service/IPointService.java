package com.makao.service;

import java.util.List;

import com.makao.entity.Point;

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

}
