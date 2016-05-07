package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.IGiftChangedDao;
import com.makao.entity.GiftChanged;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class GiftChangedDaoImpl implements IGiftChangedDao{

	@Override
	public int insert(GiftChanged giftChanged) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public GiftChanged getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(GiftChanged giftChanged) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<GiftChanged> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GiftChanged> queryByName(String name) {
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
