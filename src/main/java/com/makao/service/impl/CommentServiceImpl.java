package com.makao.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.makao.dao.ICommentDao;
import com.makao.entity.Comment;
import com.makao.service.ICommentService;

@Service
public class CommentServiceImpl implements ICommentService {
	@Resource
	private ICommentDao commentDao;
	public Comment getById(int id) {
		return this.commentDao.getById(id);
	}
	@Override
	public int insert(Comment comment) {
		return this.commentDao.insert(comment);
	}
	
	@Override
	public int deleteById(int id) {
		return this.commentDao.deleteById(id);
	}
	
	@Override
	public int update(Comment comment) {
		return this.commentDao.update(comment);
	}
	
	@Override
	public List<Comment> queryAll() {
		return this.commentDao.queryAll();
	}
	@Override
	public List<Comment> queryByName(String name) {
		return this.commentDao.queryByName(name);
	}
	@Override
	public int like(int cityId, int areaId, int commentId) {
		return this.commentDao.like(cityId, areaId, commentId);
	}
	@Override
	public List<Comment> queryProductComments(String tableName,
			Integer productId) {
		return this.commentDao.queryProductComments(tableName, productId);
	}
	
//	@Override
//	public void testor() {
//		this.commentDao.testor();
//	}
}
