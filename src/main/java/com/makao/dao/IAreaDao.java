package com.makao.dao;

import java.util.List;

import com.makao.entity.Area;

public interface IAreaDao {

    public int insert(Area area);

    public Area getById(int id);

    public int update(Area area);
    
    public List<Area> queryAll();
    
    public List<Area> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}