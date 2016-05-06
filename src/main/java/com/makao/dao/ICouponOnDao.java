package com.makao.dao;

import java.util.List;

import com.makao.entity.CouponOn;

public interface ICouponOnDao {

    public int insert(CouponOn couponOn);

    public CouponOn getById(int id);

    public int update(CouponOn couponOn);
    
    public List<CouponOn> queryAll();
    
    public List<CouponOn> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}