package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.IOrderOffDao;
import com.makao.entity.OrderOff;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class OrderOffDaoImpl implements IOrderOffDao {

	@Override
	public int insert(OrderOff orderOff) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OrderOff getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(OrderOff orderOff) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<OrderOff> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderOff> queryByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void testor() {
		// TODO Auto-generated method stub

	}

	@Override
	public int deleteById(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
