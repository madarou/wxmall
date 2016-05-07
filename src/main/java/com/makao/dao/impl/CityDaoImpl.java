package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.ICityDao;
import com.makao.entity.City;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class CityDaoImpl implements ICityDao {

	@Override
	public int insert(City city) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public City getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(City city) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<City> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<City> queryByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void testor() {
		// TODO Auto-generated method stub

	}

	@Override
	public int deleteById(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
