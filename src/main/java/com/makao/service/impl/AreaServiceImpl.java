package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.IAreaDao;
import com.makao.entity.Area;
import com.makao.service.IAreaService;

@Service
public class AreaServiceImpl implements IAreaService {
	@Resource
	private IAreaDao areaDao;
	public Area getById(int id) {
		return this.areaDao.getById(id);
	}
	@Override
	public int insert(Area area) {
		return this.areaDao.insert(area);
	}
	
	@Override
	public int deleteById(int id) {
		return this.areaDao.deleteById(id);
	}
	
	@Override
	public int update(Area area) {
		return this.areaDao.update(area);
	}
	
	@Override
	public List<Area> queryAll() {
		return this.areaDao.queryAll();
	}
	@Override
	public List<Area> queryByName(String name) {
		return this.areaDao.queryByName(name);
	}
	@Override
	public int editCatalog(Area area, String oldName, String newName,
			String sequenceNew, String productTable) {
		return this.areaDao.editCatalog(area, oldName, newName,
				sequenceNew, productTable);
	}
	@Override
	public int deleteCatalog(Area area, String catalogName, String productTable) {
		return this.areaDao.deleteCatalog(area, catalogName, productTable);
	}
	@Override
	public int newCatalog(Area area) {
		return this.areaDao.newCatalog(area);
	}
	@Override
	public int closeArea(int areaId) {
		return this.areaDao.closeArea(areaId);
	}
	@Override
	public int openArea(int areaId) {
		return this.areaDao.openArea(areaId);
	}
	@Override
	public List<Area> queryByCityId(int cityId) {
		return this.areaDao.queryByCityId(cityId);
	}
	
//	@Override
//	public void testor() {
//		this.areaDao.testor();
//	}
}
