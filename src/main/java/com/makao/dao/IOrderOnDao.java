package com.makao.dao;

import java.util.List;

import com.makao.entity.OrderOn;

public interface IOrderOnDao {

    public int insert(OrderOn orderOn);

    public OrderOn getById(int id);

    public int update(OrderOn orderOn);
    
    public List<OrderOn> queryAll();
    
    public List<OrderOn> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}