package com.makao.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.makao.dao.ICommentDao;
import com.makao.entity.Comment;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月7日
 */
@Repository
@Transactional
public class CommentDaoImpl implements ICommentDao{

	@Override
	public int insert(Comment comment) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Comment getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Comment comment) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Comment> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Comment> queryByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void testor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int deleteById(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
