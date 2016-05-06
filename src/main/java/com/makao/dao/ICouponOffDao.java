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
}