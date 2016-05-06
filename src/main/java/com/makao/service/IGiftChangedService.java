package com.makao.service;

import java.util.List;

import com.makao.entity.GiftChanged;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IGiftChangedService {

	com.makao.entity.GiftChanged getGiftChangedById(Integer id);

	int deleteGiftChanged(Integer id);

	int insertGiftChanged(GiftChanged giftChanged);

	int updateGiftChanged(GiftChanged giftChanged);

	List<GiftChanged> queryGiftChangedByName(String name);

	List<GiftChanged> queryAllGiftChangeds();

}
