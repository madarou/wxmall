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

	public List<OrderOff> queryRefundByAreaId(String tableName, int areaId);

	public int refundOrder(int cityId, int orderid);

	public int finishRefundOrder(int cityId, int orderid);

	public int cancelRefundOrder(int cityId, int orderid, String vcomment);

	public List<OrderOff> queryAllCanceledAndReturned(String tableName);

	public int finishReturnOrder(int cityId, int orderid);
}