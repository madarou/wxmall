package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.ICityDao;
import com.makao.entity.City;
import com.makao.service.ICityService;

@Service
public class CityServiceImpl implements ICityService {
	@Resource
	private ICityDao cityDao;
	public City getById(int id) {
		return this.cityDao.getById(id);
	}
	@Override
	public int insert(City city) {
		return this.cityDao.insert(city);
	}
	
	@Override
	public int deleteById(int id) {
		return this.cityDao.deleteById(id);
	}
	
	@Override
	public int update(City city) {
		return this.cityDao.update(city);
	}
	
	@Override
	public List<City> queryAll() {
		return this.cityDao.queryAll();
	}
	@Override
	public List<City> queryByName(String name) {
		return this.cityDao.queryByName(name);
	}
	
//	@Override
//	public void testor() {
//		this.cityDao.testor();
//	}
}
