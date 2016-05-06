package com.makao.service;

import java.util.List;

import com.makao.entity.UserPoint;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IUserPointService {

	com.makao.entity.UserPoint getUserPointById(Integer id);

	int deleteUserPoint(Integer id);

	int insertUserPoint(UserPoint userPoint);

	int updateUserPoint(UserPoint userPoint);

	List<UserPoint> queryUserPointByName(String name);

	List<UserPoint> queryAllUserPoints();

}
