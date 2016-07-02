package com.makao.dao;

import java.util.List;

import com.makao.entity.CouponOff;

public interface ICouponOffDao {

    public int insert(CouponOff couponOff);

    public CouponOff getById(int id);

    public int update(CouponOff couponOff);
    
    public List<CouponOff> queryAll();
    
    public List<CouponOff> queryByName(String name);

	public void testor();

	public int deleteById(int id);

	/**
	 * @param tableName
	 * @param userid
	 * @return
	 * 查询用户所有失效优惠券
	 */
	public List<CouponOff> queryAllByUserId(String tableName, int userid);

	/**
	 * @param tableName
	 * @param couponid
	 * @return
	 * 查询失效优惠券详情
	 */
	public CouponOff queryByCouponId(String tableName, int couponid);
}