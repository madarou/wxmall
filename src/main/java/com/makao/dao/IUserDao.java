package com.makao.dao;

import java.util.List;

import com.makao.entity.User;

public interface IUserDao {

    public int insert(User user);

    public User getById(int id);

    public int update(User user);
    
    public List<User> queryAll();
    
    public List<User> queryByName(String name);

	public void testor();

	public int deleteById(int id);

	public List<User> queryByAreaId(int areaId);

	public User checkLogin(String openid);

	/**
	 * @return
	 * 返回总记录数
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
	 * 返回areaId对应的记录总数
	 */
	public int getRecordCountByAreaId(int areaId);

	public List<User> searchUser(int areaId, String keyword);
}