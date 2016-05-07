package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.IUserPointDao;
import com.makao.entity.UserPoint;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class UserPointDaoImpl implements IUserPointDao {

	@Override
	public int insert(UserPoint userPoint) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserPoint getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(UserPoint userPoint) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<UserPoint> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserPoint> queryByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void testor() {
		// TODO Auto-generated method stub

	}

	@Override
	public int deleteById(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
