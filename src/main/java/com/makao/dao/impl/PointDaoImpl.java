package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.IPointDao;
import com.makao.entity.Point;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class PointDaoImpl implements IPointDao {

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
	
}
