package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IUserDao;
import com.makao.entity.User;
import com.makao.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
	@Resource
	private IUserDao userDao;
	public User getById(int id) {
		return this.userDao.getById(id);
	}
	@Override
	public int insert(User user) {
		return this.userDao.insert(user);
	}
	
	@Override
	public int deleteById(int id) {
		return this.userDao.deleteById(id);
	}
	
	@Override
	public int update(User user) {
		return this.userDao.update(user);
	}
	
	@Override
	public List<User> queryAll() {
		return this.userDao.queryAll();
	}
	@Override
	public List<User> queryByName(String name) {
		return this.userDao.queryByName(name);
	}
	
	@Override
	public List<User> queryByAreaId(int areaId) {
		return this.userDao.queryByAreaId(areaId);
	}
	
	@Override
	public void testor() {
		this.userDao.testor();
	}
	@Override
	public User checkLogin(String openid) {
		return this.userDao.checkLogin(openid);
	}
	@Override
	public int getRecordCount() {
		return this.userDao.getRecordCount();
	}
	@Override
	public List<User> queryFromToIndex(int from, int to) {
		return this.userDao.queryFromToIndex(from, to);
	}
	@Override
	public int getRecordCountByAreaId(int areaId) {
		return this.userDao.getRecordCountByAreaId(areaId);
	}
	

}
