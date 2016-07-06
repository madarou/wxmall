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

	/**
	 * @param cityId
	 * @param areaId
	 * @param commentId
	 * @return
	 * 评论被点赞
	 */
	int like(int cityId, int areaId, int commentId);

}
