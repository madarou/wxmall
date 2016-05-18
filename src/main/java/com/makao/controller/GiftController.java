package com.makao.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.makao.entity.Gift;
import com.makao.entity.Vendor;
import com.makao.service.IGiftService;
import com.makao.service.IVendorService;

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
	@Resource
	private IVendorService vendorService;
	
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
	@RequestMapping(value = "/vnew/{vendorid:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@PathVariable("vendorid") int vendorid,@RequestBody Gift Gift) {
		Vendor vendor = this.vendorService.getById(vendorid);
		Gift.setCityId(vendor.getCityId());
		Gift.setAreaId(vendor.getAreaId());
		Gift.setAreaName(vendor.getAreaName());
		Gift.setType("礼品兑换");
		int res = this.giftService.insert(Gift);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加奖品成功id=" + Gift.getName());
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("增加奖品成功失败id=" + Gift.getId());
        	jsonObject.put("msg", "200");
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
	
	@RequestMapping(value = "/v_giftmanage/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView giftManage(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("v_giftManage");
		if (token == null) {
			return modelAndView;
		}
		Vendor vendor = this.vendorService.getById(id);
		List<Gift> gifts = null;
		if(vendor!=null){
			//根据vendorId找到areaId和vendorId，到Gift_cityId中查找所有该areaId下的gift
			int cityId = vendor.getCityId();
			int areaId = vendor.getAreaId();
			gifts = this.giftService.queryByCityAreaId(cityId,areaId);
		}
		
		modelAndView.addObject("id", id);
		modelAndView.addObject("token", token);
		modelAndView.addObject("gifts", gifts);
		return modelAndView;
    }
}
