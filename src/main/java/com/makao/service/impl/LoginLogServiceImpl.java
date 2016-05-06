package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.ILoginLogDao;
import com.makao.entity.LoginLog;
import com.makao.service.ILoginLogService;

@Service
public class LoginLogServiceImpl implements ILoginLogService {
	@Resource
	private ILoginLogDao loginLogDao;
	public LoginLog getById(int id) {
		return this.loginLogDao.getById(id);
	}
	@Override
	public int insert(LoginLog loginLog) {
		return this.loginLogDao.insert(loginLog);
	}
	
	@Override
	public int deleteById(int id) {
		return this.loginLogDao.deleteById(id);
	}
	
	@Override
	public int update(LoginLog loginLog) {
		return this.loginLogDao.update(loginLog);
	}
	
	@Override
	public List<LoginLog> queryAll() {
		return this.loginLogDao.queryAll();
	}
	@Override
	public List<LoginLog> queryByName(String name) {
		return this.loginLogDao.queryByName(name);
	}
	
//	@Override
//	public void testor() {
//		this.loginLogDao.testor();
//	}
}
