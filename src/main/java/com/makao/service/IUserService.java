package com.makao.service;

import java.util.List;

import com.makao.entity.User;

public interface IUserService {
	public User getById(int userId);
	public int insert(User user);
	public int deleteById(int userId);
	public int update(User user);
	public List<User> queryAll();
	public List<User> queryByName(String name);
	
	public void testor();
}
