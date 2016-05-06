package com.makao.service;

import java.util.List;

import com.makao.entity.Comment;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
public interface ICommentService {

	int insertComment(Comment comment);

	int updateComment(Comment comment);

	List<Comment> queryCommentByName(String name);

	List<Comment> queryAllComments();

	com.makao.entity.Comment getCommentById(Integer id);

	int deleteComment(Integer id);

}
