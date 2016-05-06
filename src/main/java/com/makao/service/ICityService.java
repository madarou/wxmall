package com.makao.service;

import java.util.List;

import com.makao.entity.City;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ICityService {

	int insertCity(City city);

	int updateCity(City city);

	List<City> queryCityByName(String name);

	List<City> queryAllCities();

	City getCityById(Integer id);

	int deleteCity(Integer id);

}
