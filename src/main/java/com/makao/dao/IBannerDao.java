package com.makao.dao;

import java.util.List;

import com.makao.entity.Banner;

public interface IBannerDao {

    public int insert(Banner banner);

    public Banner getById(int id);

    public int update(Banner banner);
    
    public List<Banner> queryAll();
    
    public List<Banner> queryByName(String name);

	public void testor();

	public int deleteById(int id);

	public List<Banner> queryByAreaId(int areaId);
}