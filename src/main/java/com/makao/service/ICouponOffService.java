package com.makao.service;

import java.util.List;

import com.makao.entity.CouponOff;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ICouponOffService {

	int insertCouponOff(CouponOff couponOff);

	int updateCouponOff(CouponOff couponOff);

	List<CouponOff> queryCouponOffByName(String name);

	List<CouponOff> queryAllCouponOffs();

	com.makao.entity.CouponOff getCouponOffById(Integer id);

	int deleteCouponOff(Integer id);

}
