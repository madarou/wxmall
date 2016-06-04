package com.makao.dao;

import java.util.List;

import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;

public interface IOrderOnDao {

    public int insert(OrderOn orderOn);

    public OrderOn getById(int id);

    public int update(OrderOn orderOn);
    
    public List<OrderOn> queryAll(String tableName);
    
    public List<OrderOn> queryByName(String name);

	public void testor();

	public int deleteById(int id);

	public List<OrderOn> queryQueueByAreaId(String tableName, int areaId);

	public int cancelOrder(int cityId, int orderid, String vcomment);

	public List<OrderOn> queryProcessByAreaId(String tableName, int areaId);

	public int distributeOrder(int cityId, int orderid);

	public int finishOrder(int cityId, int orderid);

	public List<OrderOn> queryByUserId(String tableName, int userid);

	public OrderOn queryByOrderId(String tableName, int orderid);

	int confirmGetOrder(int cityId, int orderid);

	//已配送订单查询，但尚未确认收货
	public List<OrderOn> queryDistributedByAreaId(String tableName, int areaId);
}