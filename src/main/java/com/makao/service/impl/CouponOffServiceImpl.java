package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.ICouponOffDao;
import com.makao.entity.CouponOff;
import com.makao.service.ICouponOffService;

@Service
public class CouponOffServiceImpl implements ICouponOffService {
	@Resource
	private ICouponOffDao couponOffDao;
	public CouponOff getById(int id) {
		return this.couponOffDao.getById(id);
	}
	@Override
	public int insert(CouponOff couponOff) {
		return this.couponOffDao.insert(couponOff);
	}
	
	@Override
	public int deleteById(int id) {
		return this.couponOffDao.deleteById(id);
	}
	
	@Override
	public int update(CouponOff couponOff) {
		return this.couponOffDao.update(couponOff);
	}
	
	@Override
	public List<CouponOff> queryAll() {
		return this.couponOffDao.queryAll();
	}
	@Override
	public List<CouponOff> queryByName(String name) {
		return this.couponOffDao.queryByName(name);
	}
	@Override
	public List<CouponOff> queryAllByUserId(String tableName, int userid) {
		return this.couponOffDao.queryAllByUserId(tableName, userid);
	}
	@Override
	public CouponOff queryByCouponId(String tableName, int couponid) {
		return this.couponOffDao.queryByCouponId(tableName, couponid);
	}
	
//	@Override
//	public void testor() {
//		this.couponOffDao.testor();
//	}
}
