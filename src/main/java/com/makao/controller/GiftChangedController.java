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
import com.makao.entity.GiftChanged;
import com.makao.service.IGiftChangedService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/giftChanged")
public class GiftChangedController {
	private static final Logger logger = Logger.getLogger(GiftChangedController.class);
	@Resource
	private IGiftChangedService giftChangedService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody GiftChanged get(@PathVariable("id") Integer id)
	{
		logger.info("获取已兑换奖品信息id=" + id);
		GiftChanged GiftChanged = (GiftChanged)this.giftChangedService.getGiftChangedById(id);
		return GiftChanged;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.giftChangedService.deleteGiftChanged(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除已兑换奖品信息成功id=" + id);
        	jsonObject.put("msg", "删除已兑换奖品信息成功");
		}
		else{
			logger.info("删除已兑换奖品信息失败id=" + id);
        	jsonObject.put("msg", "删除已兑换奖品信息失败");
		}
        return jsonObject;
    }
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody GiftChanged GiftChanged) {
		int res = this.giftChangedService.insertGiftChanged(GiftChanged);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加已兑换奖品成功id=" + GiftChanged.getId());
        	jsonObject.put("msg", "增加已兑换奖品成功");
		}
		else{
			logger.info("增加已兑换奖品成功失败id=" + GiftChanged.getId());
        	jsonObject.put("msg", "增加已兑换奖品失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody GiftChanged GiftChanged) {
		int res = this.giftChangedService.updateGiftChanged(GiftChanged);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改已兑换奖品信息成功id=" + GiftChanged.getId());
        	jsonObject.put("msg", "修改已兑换奖品信息成功");
		}
		else{
			logger.info("修改已兑换奖品信息失败id=" + GiftChanged.getId());
        	jsonObject.put("msg", "修改已兑换奖品信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<GiftChanged> GiftChangeds = null;
		//则根据关键字查询
		GiftChangeds = this.giftChangedService.queryGiftChangedByName(name);
		logger.info("根据关键字: '"+name+"' 查询已兑换奖品信息完成");
        return GiftChangeds;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<GiftChanged> GiftChangeds = null;
		//则查询返回所有
		GiftChangeds = this.giftChangedService.queryAllGiftChangeds();
		logger.info("查询所有已兑换奖品信息完成");
        return GiftChangeds;
    }
}
