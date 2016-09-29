package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IPointDao;
import com.makao.entity.Point;
import com.makao.entity.PointLog;
import com.makao.service.IPointService;

@Service
public class PointServiceImpl implements IPointService {
	@Resource
	private IPointDao pointDao;
	public Point getById(int id) {
		return this.pointDao.getById(id);
	}
	@Override
	public int insert(Point point) {
		return this.pointDao.insert(point);
	}
	
	@Override
	public int deleteById(int id) {
		return this.pointDao.deleteById(id);
	}
	
	@Override
	public int update(Point point) {
		return this.pointDao.update(point);
	}
	
	@Override
	public List<Point> queryAll() {
		return this.pointDao.queryAll();
	}
	@Override
	public List<Point> queryByName(String name) {
		return this.pointDao.queryByName(name);
	}
	@Override
	public int insertPointLog(PointLog pl) {
		return this.pointDao.insertPointLog(pl);
		
	}
	@Override
	public List<PointLog> queryPointLog(String string, int userid) {
		return this.pointDao.queryPointLog(string, userid);
	}
	
//	@Override
//	public void testor() {
//		this.pointDao.testor();
//	}
}
