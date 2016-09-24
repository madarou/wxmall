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

	/**
	 * @param cityId
	 * @param areaId
	 * @param commentId
	 * @return
	 * 给评论点赞
	 */
	public int like(int cityId, int areaId, int commentId);

	/**
	 * @param tableName
	 * @param productId
	 * @return
	 * 返回tableName中所有productId的评论
	 */
	public List<Comment> queryProductComments(String tableName,
			Integer productId);

	/**
	 * @param string
	 * @param userId
	 * @param productId
	 * @return
	 * 加载用户userId对商品productId的所有评论
	 */
	public List<Comment> queryUserComments(String string, Integer userId,
			Integer productId);

	public Comment getByCityAreaComentId(int cityId, int areaId, int commentid);
}