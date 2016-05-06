package com.makao.dao;

import java.util.List;

import com.makao.entity.City;

public interface ICityDao {

    public int insert(City city);

    public City getById(int id);

    public int update(City city);
    
    public List<City> queryAll();
    
    public List<City> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}