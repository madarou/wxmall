package com.makao.dao;

import java.util.List;

import com.makao.entity.UserPoint;

public interface IUserPointDao {

    public int insert(UserPoint userPoint);

    public UserPoint getById(int id);

    public int update(UserPoint userPoint);
    
    public List<UserPoint> queryAll();
    
    public List<UserPoint> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}