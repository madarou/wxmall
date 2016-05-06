package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IUserPointDao;
import com.makao.entity.UserPoint;
import com.makao.service.IUserPointService;

@Service
public class UserPointServiceImpl implements IUserPointService {
	@Resource
	private IUserPointDao userPointDao;
	public UserPoint getById(int id) {
		return this.userPointDao.getById(id);
	}
	@Override
	public int insert(UserPoint userPoint) {
		return this.userPointDao.insert(userPoint);
	}
	
	@Override
	public int deleteById(int id) {
		return this.userPointDao.deleteById(id);
	}
	
	@Override
	public int update(UserPoint userPoint) {
		return this.userPointDao.update(userPoint);
	}
	
	@Override
	public List<UserPoint> queryAll() {
		return this.userPointDao.queryAll();
	}
	@Override
	public List<UserPoint> queryByName(String name) {
		return this.userPointDao.queryByName(name);
	}
	
//	@Override
//	public void testor() {
//		this.userPointDao.testor();
//	}
}
