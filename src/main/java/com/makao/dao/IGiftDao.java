package com.makao.dao;

import java.util.List;

import com.makao.entity.Gift;

public interface IGiftDao {

    public int insert(Gift gift);

    public Gift getById(int id);

    public int update(Gift gift);
    
    public List<Gift> queryAll();
    
    public List<Gift> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}