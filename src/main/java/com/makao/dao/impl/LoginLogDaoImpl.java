package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.ILoginLogDao;
import com.makao.entity.LoginLog;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class LoginLogDaoImpl implements ILoginLogDao {

	@Override
	public int insert(LoginLog loginLog) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LoginLog getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(LoginLog loginLog) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<LoginLog> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LoginLog> queryByName(String name) {
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
