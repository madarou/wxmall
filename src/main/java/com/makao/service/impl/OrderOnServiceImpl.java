package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IOrderOnDao;
import com.makao.entity.OrderOn;
import com.makao.service.IOrderOnService;

@Service
public class OrderOnServiceImpl implements IOrderOnService {
	@Resource
	private IOrderOnDao orderOnDao;
	public OrderOn getById(int id) {
		return this.orderOnDao.getById(id);
	}
	@Override
	public int insert(OrderOn orderOn) {
		return this.orderOnDao.insert(orderOn);
	}
	
	@Override
	public int deleteById(int id) {
		return this.orderOnDao.deleteById(id);
	}
	
	@Override
	public int update(OrderOn orderOn) {
		return this.orderOnDao.update(orderOn);
	}
	
	@Override
	public List<OrderOn> queryAll(String tableName) {
		return this.orderOnDao.queryAll(tableName);
	}
	@Override
	public List<OrderOn> queryByName(String name) {
		return this.orderOnDao.queryByName(name);
	}
	@Override
	public List<OrderOn> queryQueueByAreaId(String tableName, int areaId) {
		return this.orderOnDao.queryQueueByAreaId(tableName, areaId);
	}
	@Override
	public int cancelOrder(int cityId, int orderid, String vcomment) {
		return this.orderOnDao.cancelOrder(cityId, orderid, vcomment);
	}
	@Override
	public List<OrderOn> queryProcessByAreaId(String tableName, int areaId) {
		return this.orderOnDao.queryProcessByAreaId(tableName,areaId);
	}
	
//	@Override
//	public void testor() {
//		this.orderOnDao.testor();
//	}
}
