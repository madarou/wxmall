package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IOrderOffDao;
import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;
import com.makao.service.IOrderOffService;

@Service
public class OrderOffServiceImpl implements IOrderOffService {
	@Resource
	private IOrderOffDao orderOffDao;
	public OrderOff getById(int id) {
		return this.orderOffDao.getById(id);
	}
	@Override
	public int insert(OrderOff orderOff) {
		return this.orderOffDao.insert(orderOff);
	}
	
	@Override
	public int deleteById(int id) {
		return this.orderOffDao.deleteById(id);
	}
	
	@Override
	public int update(OrderOff orderOff) {
		return this.orderOffDao.update(orderOff);
	}
	
	@Override
	public List<OrderOff> queryAll() {
		return this.orderOffDao.queryAll();
	}
	@Override
	public List<OrderOff> queryByName(String name) {
		return this.orderOffDao.queryByName(name);
	}
	@Override
	public List<OrderOff> queryDoneByAreaId(String tableName, int areaId) {
		return this.orderOffDao.queryDoneByAreaId(tableName, areaId);
	}
	@Override
	public List<OrderOff> queryCancelByAreaId(String tableName, int areaId) {
		return this.orderOffDao.queryCancelByAreaId(tableName, areaId);
	}
	@Override
	public List<OrderOff> queryRefundByAreaId(String tableName, int areaId) {
		return this.orderOffDao.queryRefundByAreaId(tableName, areaId);
	}
	@Override
	public int refundOrder(int cityId, int orderid) {
		return this.orderOffDao.refundOrder(cityId,orderid);
	}
	@Override
	public int finishRefundOrder(int cityId, int orderid) {
		return this.orderOffDao.finishRefundOrder(cityId, orderid);
	}
	@Override
	public int cancelRefundOrder(int cityId, int orderid, String vcomment) {
		return this.orderOffDao.cancelRefundOrder(cityId, orderid, vcomment);
	}

	@Override
	public List<OrderOff> queryAllCanceledAndReturned(String tableName) {
		return this.orderOffDao.queryAllCanceledAndReturned(tableName);
	}
	@Override
	public int finishReturnOrder(int cityId, int orderid) {
		return this.orderOffDao.finishReturnOrder(cityId, orderid);
	}
	
//	@Override
//	public void testor() {
//		this.orderOffDao.testor();
//	}
}
