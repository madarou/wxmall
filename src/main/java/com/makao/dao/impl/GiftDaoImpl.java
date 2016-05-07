package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.IGiftDao;
import com.makao.entity.Gift;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class GiftDaoImpl implements IGiftDao {

	@Override
	public int insert(Gift gift) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Gift getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Gift gift) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Gift> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Gift> queryByName(String name) {
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
