package com.makao.dao;

import java.util.List;

import com.makao.entity.OrderOff;

public interface IOrderOffDao {

    public int insert(OrderOff orderOff);

    public OrderOff getById(int id);

    public int update(OrderOff orderOff);
    
    public List<OrderOff> queryAll();
    
    public List<OrderOff> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}