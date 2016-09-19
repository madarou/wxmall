package com.makao.service;

import java.util.List;

import com.makao.entity.User;

public interface IUserService {
	public User getById(int userId);
	public int insert(User user);
	public int deleteById(int userId);
	public int update(User user);
	public List<User> queryAll();
	public List<User> queryByName(String name);
	
	public void testor();
	public List<User> queryByAreaId(int areaId);
	public User checkLogin(String openid);
	
	/**
	 * @return
	 * 返回总的记录数
	 */
	public int getRecordCount();
	/**
	 * @param from
	 * @param to
	 * @return
	 * 返回id从from到to的所有记录
	 */
	public List<User> queryFromToIndex(int from, int to);
	/**
	 * @param areaId
	 * @return
	 * 返回areaid对应的记录数
	 */
	public int getRecordCountByAreaId(int areaId);
	public List<User> searchUser(int areaId, String keyword);
}
