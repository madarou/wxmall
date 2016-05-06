package com.makao.service;

import java.util.List;

import com.makao.entity.Gift;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IGiftService {

	int insertGift(Gift gift);

	int updateGift(Gift gift);

	List<Gift> queryGiftByName(String name);

	List<Gift> queryAllGifts();

	com.makao.entity.Gift getGiftById(Integer id);

	int deleteGift(Integer id);

}
