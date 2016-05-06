package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.ISupervisorDao;
import com.makao.entity.Supervisor;
import com.makao.service.ISupervisorService;

@Service
public class SupervisorServiceImpl implements ISupervisorService {
	@Resource
	private ISupervisorDao supervisorDao;
	public Supervisor getById(int id) {
		return this.supervisorDao.getById(id);
	}
	@Override
	public int insert(Supervisor supervisor) {
		return this.supervisorDao.insert(supervisor);
	}
	
	@Override
	public int deleteById(int id) {
		return this.supervisorDao.deleteById(id);
	}
	
	@Override
	public int update(Supervisor supervisor) {
		return this.supervisorDao.update(supervisor);
	}
	
	@Override
	public List<Supervisor> queryAll() {
		return this.supervisorDao.queryAll();
	}
	@Override
	public List<Supervisor> queryByName(String name) {
		return this.supervisorDao.queryByName(name);
	}
	
//	@Override
//	public void testor() {
//		this.supervisorDao.testor();
//	}
}
