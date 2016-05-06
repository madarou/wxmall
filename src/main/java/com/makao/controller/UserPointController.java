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
import com.makao.entity.UserPoint;
import com.makao.service.IUserPointService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/userPoint")
public class UserPointController {
	private static final Logger logger = Logger.getLogger(UserPointController.class);
	@Resource
	private IUserPointService userPointService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody UserPoint get(@PathVariable("id") Integer id)
	{
		logger.info("获取用户积分信息id=" + id);
		UserPoint UserPoint = (UserPoint)this.userPointService.getById(id);
		return UserPoint;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.userPointService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除用户积分信息成功id=" + id);
        	jsonObject.put("msg", "删除用户积分信息成功");
		}
		else{
			logger.info("删除用户积分信息失败id=" + id);
        	jsonObject.put("msg", "删除用户积分信息失败");
		}
        return jsonObject;
    }
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody UserPoint UserPoint) {
		int res = this.userPointService.insert(UserPoint);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加用户积分成功id=" + UserPoint.getId());
        	jsonObject.put("msg", "增加用户积分成功");
		}
		else{
			logger.info("增加用户积分成功失败id=" + UserPoint.getId());
        	jsonObject.put("msg", "增加用户积分失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody UserPoint UserPoint) {
		int res = this.userPointService.update(UserPoint);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改用户积分信息成功id=" + UserPoint.getId());
        	jsonObject.put("msg", "修改用户积分信息成功");
		}
		else{
			logger.info("修改用户积分信息失败id=" + UserPoint.getId());
        	jsonObject.put("msg", "修改用户积分信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<UserPoint> UserPoints = null;
		//则根据关键字查询
		UserPoints = this.userPointService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询用户积分信息完成");
        return UserPoints;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<UserPoint> UserPoints = null;
		//则查询返回所有
		UserPoints = this.userPointService.queryAll();
		logger.info("查询所有用户积分信息完成");
        return UserPoints;
    }
}
