package com.makao.service;

import java.util.List;

import com.makao.entity.Comment;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ICommentService {

	int insert(Comment comment);

	int update(Comment comment);

	List<Comment> queryByName(String name);

	List<Comment> queryAll();

	Comment getById(int id);

	int deleteById(int id);

}
