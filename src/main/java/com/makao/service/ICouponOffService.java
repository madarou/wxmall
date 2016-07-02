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

	/**
	 * @param string
	 * @param userid
	 * @return
	 * 查询用户userid下的所有失效优惠券
	 */
	List<CouponOff> queryAllByUserId(String tableName, int userid);

	/**
	 * @param tableName
	 * @param couponid
	 * @return
	 * 查询couponOff详情
	 */
	CouponOff queryByCouponId(String tableName, int couponid);

}
