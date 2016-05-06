package com.makao.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.makao.entity.Comment;
import com.makao.service.ICommentService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/comment")
public class CommentController {
	private static final Logger logger = Logger.getLogger(CommentController.class);
	@Resource
	private ICommentService commentService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Comment get(@PathVariable("id") Integer id)
	{
		logger.info("获取评论信息id=" + id);
		Comment Comment = (Comment)this.commentService.getById(id);
		return Comment;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.commentService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除评论信息成功id=" + id);
        	jsonObject.put("msg", "删除评论信息成功");
		}
		else{
			logger.info("删除城市信息失败id=" + id);
        	jsonObject.put("msg", "删除评论信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody Comment Comment) {
		int res = this.commentService.insert(Comment);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加评论成功id=" + Comment.getId());
        	jsonObject.put("msg", "增加评论成功");
		}
		else{
			logger.info("增加评论成功失败id=" + Comment.getId());
        	jsonObject.put("msg", "增加评论失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Comment Comment) {
		int res = this.commentService.update(Comment);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改评论信息成功id=" + Comment.getId());
        	jsonObject.put("msg", "修改评论信息成功");
		}
		else{
			logger.info("修改评论信息失败id=" + Comment.getId());
        	jsonObject.put("msg", "修改评论信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<Comment> Comments = null;
		//则根据关键字查询
		Comments = this.commentService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询评论信息完成");
        return Comments;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Comment> Comments = null;
		//则查询返回所有
		Comments = this.commentService.queryAll();
		logger.info("查询所有评论信息完成");
        return Comments;
    }
}
