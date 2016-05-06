package com.makao.dao;

import java.util.List;

import com.makao.entity.GiftChanged;

public interface IGiftChangedDao {

    public int insert(GiftChanged giftChanged);

    public GiftChanged getById(int id);

    public int update(GiftChanged giftChanged);
    
    public List<GiftChanged> queryAll();
    
    public List<GiftChanged> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}