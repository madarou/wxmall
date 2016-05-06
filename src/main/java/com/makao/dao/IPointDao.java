package com.makao.dao;

import java.util.List;

import com.makao.entity.Point;

public interface IPointDao {

    public int insert(Point point);

    public Point getById(int id);

    public int update(Point point);
    
    public List<Point> queryAll();
    
    public List<Point> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}