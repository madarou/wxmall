package com.makao.service;

import java.util.List;

import com.makao.entity.Banner;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IBannerService {

	int insert(Banner banner);

	int update(Banner banner);

	List<Banner> queryByName(String name);

	List<Banner> queryAll();

	Banner getById(int id);

	int deleteById(int id);

	List<Banner> queryByAreaId(int areaId);

}
