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

	/**
	 * @param tableName
	 * @param productId
	 * @return
	 * 加载tableName中所有产品id为productId的产品的评论
	 */
	List<Comment> queryProductComments(String tableName, Integer productId);

	/**
	 * @param string
	 * @param userId
	 * @param productId
	 * @return
	 * 加载用户userId对商品productId的所有评论
	 */
	List<Comment> queryUserComments(String string, Integer userId,
			Integer productId);

	Comment getByCityAreaComentId(int cityId, int areaId, int valueOf);

}
