package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IGiftChangedDao;
import com.makao.entity.GiftChanged;
import com.makao.service.IGiftChangedService;

@Service
public class GiftChangedServiceImpl implements IGiftChangedService {
	@Resource
	private IGiftChangedDao giftChangedDao;
	public GiftChanged getById(int id) {
		return this.giftChangedDao.getById(id);
	}
	@Override
	public int insert(GiftChanged giftChanged) {
		return this.giftChangedDao.insert(giftChanged);
	}
	
	@Override
	public int deleteById(int id) {
		return this.giftChangedDao.deleteById(id);
	}
	
	@Override
	public int update(GiftChanged giftChanged) {
		return this.giftChangedDao.update(giftChanged);
	}
	
	@Override
	public List<GiftChanged> queryAll() {
		return this.giftChangedDao.queryAll();
	}
	@Override
	public List<GiftChanged> queryByName(String name) {
		return this.giftChangedDao.queryByName(name);
	}
	
//	@Override
//	public void testor() {
//		this.giftChangedDao.testor();
//	}
}
