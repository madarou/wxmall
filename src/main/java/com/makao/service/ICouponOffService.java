package com.makao.service;

import java.util.List;

import com.makao.entity.CouponOff;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ICouponOffService {

	int insert(CouponOff couponOff);

	int update(CouponOff couponOff);

	List<CouponOff> queryByName(String name);

	List<CouponOff> queryAll();

	CouponOff getById(int id);

	int deleteById(int id);

}
