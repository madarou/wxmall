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
import com.makao.entity.Gift;
import com.makao.service.IGiftService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/gift")
public class GiftController {
	private static final Logger logger = Logger.getLogger(GiftController.class);
	@Resource
	private IGiftService giftService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Gift get(@PathVariable("id") Integer id)
	{
		logger.info("获取奖品信息id=" + id);
		Gift Gift = (Gift)this.giftService.getById(id);
		return Gift;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.giftService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除奖品信息成功id=" + id);
        	jsonObject.put("msg", "删除奖品信息成功");
		}
		else{
			logger.info("删除奖品信息失败id=" + id);
        	jsonObject.put("msg", "删除奖品信息失败");
		}
        return jsonObject;
    }
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody Gift Gift) {
		int res = this.giftService.insert(Gift);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加奖品成功id=" + Gift.getId());
        	jsonObject.put("msg", "增加奖品成功");
		}
		else{
			logger.info("增加奖品成功失败id=" + Gift.getId());
        	jsonObject.put("msg", "增加奖品失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Gift Gift) {
		int res = this.giftService.update(Gift);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改奖品信息成功id=" + Gift.getId());
        	jsonObject.put("msg", "修改奖品信息成功");
		}
		else{
			logger.info("修改奖品信息失败id=" + Gift.getId());
        	jsonObject.put("msg", "修改奖品信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<Gift> Gifts = null;
		//则根据关键字查询
		Gifts = this.giftService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询奖品信息完成");
        return Gifts;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Gift> Gifts = null;
		//则查询返回所有
		Gifts = this.giftService.queryAll();
		logger.info("查询所有奖品信息完成");
        return Gifts;
    }
}
