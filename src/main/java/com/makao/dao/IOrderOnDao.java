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

	public OrderOn distributeOrder(int cityId, int orderid);

	public OrderOn finishOrder(int cityId, int orderid);

	public List<OrderOn> queryByUserId(String tableName, int userid);

	public OrderOn queryByOrderId(String tableName, int orderid);

	int confirmGetOrder(int cityId, int orderid);

	//已配送订单查询，但尚未确认收货
	public List<OrderOn> queryDistributedByAreaId(String tableName, int areaId);

	/**
	 * @param areaid 
	 * @return
	 * 查询排队订单的记录数
	 */
	public int getQueueRecordCount(int cityid, int areaid);

	/**
	 * @param cityId
	 * @param areaId
	 * @return
	 * 查询待处理和处理中的记录数
	 */
	public int getProcessRecordCount(int cityId, int areaId);

	/**
	 * @param cityId
	 * @param areaId
	 * @return
	 * 查询已配送的记录数
	 */
	public int getDistributedRecordCount(int cityId, int areaId);

	/**
	 * @param cityid
	 * @return
	 * 返回Order_cityid_on的记录总数
	 */
	public int getRecordCount(int cityid);

	/**
	 * @param cityid
	 * @param orderid
	 * @return
	 * 已付款后，将订单状态从未支付改为排队中
	 */
	public OrderOn confirmMoney(String cityid, String orderid);

	/**
	 * @param cityId
	 * @param orderNum
	 * @return
	 * 查询订单是否存在
	 */
	public boolean isExist(int cityId, String orderNum);

	/**
	 * @param cityId
	 * @param orderid
	 * @return
	 * 将排队中的订单状态改为待处理
	 */
	public OrderOn processOrder(int cityId, String orderid);

	/**
	 * @param cityid 
	 * @return
	 * 从数据库中找出需要将状态从排队中改为待处理的订单，将其状态设为待处理
    	当配送时间起点-准备时间<=当前时间时的订单满足条件
	 */
	public List<OrderOn> appoachOrders(int cityid);

	/**
	 * @param cityid
	 * 订单配送完一定时间后，如果其状态还是已配送(即用户没有手动确认收货)，自动将其状态改为已收货
	 * @return
	 */
	public List<String> confirmOrders(int cityid);

	/**
	 * @param cityId
	 * @param orderid
	 * @param vcomment
	 * @return
	 * 商户添加备注
	 */
	public int vcommentOrder(int cityId, int orderid, String vcomment);
}