package com.makao.service;

import java.util.List;

import com.makao.entity.GiftChanged;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IGiftChangedService {

	int insert(GiftChanged giftChanged);

	int update(GiftChanged giftChanged);

	List<GiftChanged> queryByName(String name);

	List<GiftChanged> queryAll();

	GiftChanged getById(int id);

	int deleteById(int id);

}
