package com.makao.service;

import java.util.List;

import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IOrderOffService {

	int insert(OrderOff orderOff);

	int update(OrderOff orderOff);

	List<OrderOff> queryByName(String name);

	List<OrderOff> queryAll();

	OrderOff getById(int id);

	int deleteById(int id);

	List<OrderOff> queryDoneByAreaId(String string, int areaId);

	List<OrderOff> queryCancelByAreaId(String tableName, int areaId);

}
