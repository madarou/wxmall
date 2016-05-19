package com.makao.dao;

import java.util.List;

import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;

public interface IOrderOffDao {

    public int insert(OrderOff orderOff);

    public OrderOff getById(int id);

    public int update(OrderOff orderOff);
    
    public List<OrderOff> queryAll();
    
    public List<OrderOff> queryByName(String name);

	public void testor();

	public int deleteById(int id);

	public List<OrderOff> queryDoneByAreaId(String tableName, int areaId);

	public List<OrderOff> queryCancelByAreaId(String tableName, int areaId);
}