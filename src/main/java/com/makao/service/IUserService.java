package com.makao.service;

import java.util.List;

import com.makao.entity.User;

public interface IUserService {
	public User getUserById(int userId);
	public int insertUser(User user);
	public int deleteUser(int userId);
	public int updateUser(User user);
	public List<User> queryAllUser();
	public List<User> queryUserByName(String name);
}
