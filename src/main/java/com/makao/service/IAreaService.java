package com.makao.service;

import java.util.List;

import com.makao.entity.Area;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IAreaService {

	int insertArea(Area area);

	int updateArea(Area area);

	List<Area> queryAreaByName(String name);

	List<Area> queryAllAreas();

	com.makao.entity.Area getAreaById(Integer id);

	int deleteArea(Integer id);

}
