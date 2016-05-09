package com.makao.dao;

import java.util.List;

import com.makao.entity.Comment;

public interface ICommentDao {

    public int insert(Comment comment);

    public Comment getById(int id);

    public int update(Comment comment);
    
    public List<Comment> queryAll();
    
    public List<Comment> queryByName(String name);

	public void testor();

	public int deleteById(int id);
}