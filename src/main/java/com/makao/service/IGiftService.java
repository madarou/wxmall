package com.makao.service;

import java.util.List;

import com.makao.entity.Gift;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IGiftService {

	int insert(Gift gift);

	int update(Gift gift);

	List<Gift> queryByName(String name);

	List<Gift> queryAll();

	Gift getById(int id);

	int deleteById(int id);

	List<Gift> queryByCityAreaId(int cityId, int areaId);

}
