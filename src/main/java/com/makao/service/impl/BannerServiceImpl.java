package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IBannerDao;
import com.makao.entity.Banner;
import com.makao.service.IBannerService;

@Service
public class BannerServiceImpl implements IBannerService {
	@Resource
	private IBannerDao bannerDao;
	public Banner getById(int id) {
		return this.bannerDao.getById(id);
	}
	@Override
	public int insert(Banner banner) {
		return this.bannerDao.insert(banner);
	}
	
	@Override
	public int deleteById(int id) {
		return this.bannerDao.deleteById(id);
	}
	
	@Override
	public int update(Banner banner) {
		return this.bannerDao.update(banner);
	}
	
	@Override
	public List<Banner> queryAll() {
		return this.bannerDao.queryAll();
	}
	@Override
	public List<Banner> queryByName(String name) {
		return this.bannerDao.queryByName(name);
	}
	@Override
	public List<Banner> queryByAreaId(int areaId) {
		return this.bannerDao.queryByAreaId(areaId);
	}
	
//	@Override
//	public void testor() {
//		this.bannerDao.testor();
//	}
}
