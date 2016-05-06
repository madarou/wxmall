package com.makao.dao;

import java.util.List;

import com.makao.entity.User;

public interface IUserDao {

    public int insert(User user);

    public User getById(int id);

    public int update(User user);
    
    public List<User> queryAll();
    
    public List<User> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}