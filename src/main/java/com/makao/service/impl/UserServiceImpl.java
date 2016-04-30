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
	public User getUserById(int userId) {
		return this.userDao.selectByPrimaryKey(userId);
	}
	@Override
	public int insertUser(User user) {
		return this.userDao.insert(user);
	}
	
	@Override
	public int deleteUser(int userId) {
		return this.userDao.deleteByPrimaryKey(userId);
	}
	
	@Override
	public int updateUser(User user) {
		return this.userDao.update(user);
	}
	
	@Override
	public List<User> queryAllUser() {
		return this.userDao.queryAllUser();
	}
	@Override
	public List<User> queryUserByName(String name) {
		return this.userDao.queryUserByName(name);
	}

}
