package com.makao.service;

import java.util.List;

import com.makao.entity.Area;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IAreaService {

	int insert(Area area);

	int update(Area area);

	List<Area> queryByName(String name);

	List<Area> queryAll();

	Area getById(int id);

	int deleteById(int id);

}
