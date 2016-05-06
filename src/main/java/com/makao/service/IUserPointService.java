package com.makao.service;

import java.util.List;

import com.makao.entity.UserPoint;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface IUserPointService {

	int insert(UserPoint userPoint);

	int update(UserPoint userPoint);

	List<UserPoint> queryByName(String name);

	List<UserPoint> queryAll();

	UserPoint getById(int id);

	int deleteById(int id);

}
