package com.makao.dao;

import java.util.List;

import com.makao.entity.User;

public interface IUserDao {
    public int deleteByPrimaryKey(Integer id);

    public int insert(User user);

    public User selectByPrimaryKey(Integer id);

    public int update(User user);
    
    public List<User> queryAllUser();
    
    public List<User> queryUserByName(String name);
}