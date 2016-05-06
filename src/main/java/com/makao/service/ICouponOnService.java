package com.makao.service;

import java.util.List;

import com.makao.entity.CouponOn;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ICouponOnService {

	int insertCouponOn(CouponOn couponOn);

	int updateCouponOn(CouponOn couponOn);

	List<CouponOn> queryCouponOnByName(String name);

	List<CouponOn> queryAllCouponOns();

	com.makao.entity.CouponOn getCouponOnById(Integer id);

	int deleteCouponOn(Integer id);

}
