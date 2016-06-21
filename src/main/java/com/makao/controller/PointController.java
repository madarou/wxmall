package com.makao.controller;

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
import com.makao.entity.Point;
import com.makao.service.IPointService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/point")
public class PointController {
	private static final Logger logger = Logger.getLogger(PointController.class);
	@Resource
	private IPointService pointService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Point get(@PathVariable("id") Integer id)
	{
		logger.info("获取积分信息id=" + id);
		Point Point = (Point)this.pointService.getById(id);
		return Point;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.pointService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除积分信息成功id=" + id);
        	jsonObject.put("msg", "删除积分信息成功");
		}
		else{
			logger.info("删除积分信息失败id=" + id);
        	jsonObject.put("msg", "删除积分信息失败");
		}
        return jsonObject;
    }
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody Point Point) {
		int res = this.pointService.insert(Point);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加积分成功id=" + Point.getId());
        	jsonObject.put("msg", "增加积分成功");
		}
		else{
			logger.info("增加积分成功失败id=" + Point.getId());
        	jsonObject.put("msg", "增加积分失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Point Point) {
		int res = this.pointService.update(Point);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改积分信息成功id=" + Point.getId());
        	jsonObject.put("msg", "修改积分信息成功");
		}
		else{
			logger.info("修改积分信息失败id=" + Point.getId());
        	jsonObject.put("msg", "修改积分信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<Point> Points = null;
		//则根据关键字查询
		Points = this.pointService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询积分信息完成");
        return Points;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Point> Points = null;
		//则查询返回所有
		Points = this.pointService.queryAll();
		logger.info("查询所有积分信息完成");
        return Points;
    }
}
