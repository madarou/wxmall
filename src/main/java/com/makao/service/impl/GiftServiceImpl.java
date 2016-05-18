package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IGiftDao;
import com.makao.entity.Gift;
import com.makao.service.IGiftService;

@Service
public class GiftServiceImpl implements IGiftService {
	@Resource
	private IGiftDao giftDao;
	public Gift getById(int id) {
		return this.giftDao.getById(id);
	}
	@Override
	public int insert(Gift gift) {
		return this.giftDao.insert(gift);
	}
	
	@Override
	public int deleteById(int id) {
		return this.giftDao.deleteById(id);
	}
	
	@Override
	public int update(Gift gift) {
		return this.giftDao.update(gift);
	}
	
	@Override
	public List<Gift> queryAll() {
		return this.giftDao.queryAll();
	}
	@Override
	public List<Gift> queryByName(String name) {
		return this.giftDao.queryByName(name);
	}
	@Override
	public List<Gift> queryByCityAreaId(int cityId, int areaId) {
		return this.giftDao.queryByCityAreaId(cityId, areaId);
	}
	
//	@Override
//	public void testor() {
//		this.giftDao.testor();
//	}
}
