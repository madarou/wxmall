package com.makao.controller;

import java.sql.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.makao.auth.AuthPassport;
import com.makao.entity.Comment;
import com.makao.entity.Product;
import com.makao.service.ICommentService;
import com.makao.service.IOrderOffService;
import com.makao.service.IOrderOnService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/comment")
public class CommentController {
	private static final Logger logger = Logger.getLogger(CommentController.class);
	@Resource
	private ICommentService commentService;
	@Resource
	private IOrderOffService orderOffService;
	
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
	
	@AuthPassport
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody Comment Comment) {
		Comment.setDate(new Date(System.currentTimeMillis()));
		int res = this.commentService.insert(Comment);
		JSONObject jsonObject = new JSONObject();
		if(res!=0){
			//添加了评论后需要将评论的id号插入到对应订单中，方便后面读取
			//以"商品id=评论id,"的形式插入
			Comment.setId(res);
			this.orderOffService.updateComment(Comment);
			logger.info(Comment.getUserName()+" 增加评论成功,id=" + res);
        	jsonObject.put("msg", "200");
        	jsonObject.put("id", res);
		}
		else{
			logger.info( Comment.getUserName()+" 增加评论成功失败cityid,areaid,commentid=" + Comment.getCityId()
					+" "+Comment.getAreaId()+" "+Comment.getProductId());
        	jsonObject.put("msg", "增加评论失败");
		}
        return jsonObject;
    }
	
	/**
	 * @param Comment
	 * @return
	 * 用户给评论点赞
	 */
	@AuthPassport
	@RequestMapping(value = "/like", method = RequestMethod.POST)
    public @ResponseBody
    Object like(@RequestBody JSONObject paramObject) {
		int cityId = paramObject.getIntValue("cityId");
		int areaId = paramObject.getIntValue("areaId");
		int commentId = paramObject.getIntValue("commentId");
		int res = this.commentService.like(cityId, areaId, commentId);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("评论点赞成功，被赞评论" + cityId +" "+areaId+" "+commentId);
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("评论点赞失败，被赞评论" + cityId +" "+areaId+" "+commentId);
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	@RequestMapping(value="/all/{cityid:\\d+}/{areaid:\\d+}/{productid:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Object all(@PathVariable("cityid") Integer cityId,@PathVariable("areaid") Integer areaId,@PathVariable("productid") Integer productId)
	{
		List<Comment> comments = null;
		JSONObject jsonObject = new JSONObject();
		//则根据关键字查询
		comments = this.commentService.queryProductComments("Comment_"+cityId+"_"+areaId,productId);
		logger.info("获取城市 "+cityId+" 和区域 "+areaId+"下的商品 "+productId+"的所有评论完成");
		jsonObject.put("msg", "200");
		jsonObject.put("comments", comments);//不用序列化，方便前端jquery遍历
		return jsonObject;
	}
	
	/**
	 * @param cityId
	 * @param areaId
	 * @param userId
	 * @param productId
	 * @return
	 * 加载用户userId对商品productId的所有评论
	 */
	@RequestMapping(value="/{cityid:\\d+}/{areaid:\\d+}/{userid:\\d+}/{productid:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Object queryUserComment(@PathVariable("cityid") Integer cityId,@PathVariable("areaid") Integer areaId,
			@PathVariable("userid") Integer userId,@PathVariable("productid") Integer productId)
	{
		List<Comment> comments = null;
		JSONObject jsonObject = new JSONObject();
		comments = this.commentService.queryUserComments("Comment_"+cityId+"_"+areaId,userId,productId);
		logger.info("获取城市 "+cityId+" 和区域 "+areaId+"下的商品 "+productId+"的所有评论完成");
		jsonObject.put("msg", "200");
		jsonObject.put("comments", comments);//不用序列化，方便前端jquery遍历
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
