package com.makao.service;

import java.util.List;

import com.makao.entity.City;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ICityService {

	int insert(City city);

	int update(City city);

	List<City> queryByName(String name);

	List<City> queryAll();

	City getById(int id);

	int deleteById(int id);

}
