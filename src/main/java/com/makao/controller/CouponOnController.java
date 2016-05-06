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
import com.makao.entity.CouponOn;
import com.makao.entity.CouponOn;
import com.makao.service.ICouponOnService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/couponOn")
public class CouponOnController {
	private static final Logger logger = Logger.getLogger(CouponOnController.class);
	@Resource
	private ICouponOnService couponOnService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody CouponOn get(@PathVariable("id") Integer id)
	{
		logger.info("获取有效优惠券信息id=" + id);
		CouponOn CouponOn = (CouponOn)this.couponOnService.getCouponOnById(id);
		return CouponOn;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.couponOnService.deleteCouponOn(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除有效优惠券信息成功id=" + id);
        	jsonObject.put("msg", "删除有效优惠券信息成功");
		}
		else{
			logger.info("删除有效优惠券信息失败id=" + id);
        	jsonObject.put("msg", "删除有效优惠券信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody CouponOn CouponOn) {
		int res = this.couponOnService.insertCouponOn(CouponOn);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加有效优惠券成功id=" + CouponOn.getId());
        	jsonObject.put("msg", "增加有效优惠券成功");
		}
		else{
			logger.info("增加有效优惠券成功失败id=" + CouponOn.getId());
        	jsonObject.put("msg", "增加有效优惠券失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody CouponOn CouponOn) {
		int res = this.couponOnService.updateCouponOn(CouponOn);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改有效优惠券信息成功id=" + CouponOn.getId());
        	jsonObject.put("msg", "修改有效优惠券信息成功");
		}
		else{
			logger.info("修改有效优惠券信息失败id=" + CouponOn.getId());
        	jsonObject.put("msg", "修改有效优惠券信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<CouponOn> CouponOns = null;
		//则根据关键字查询
		CouponOns = this.couponOnService.queryCouponOnByName(name);
		logger.info("根据关键字: '"+name+"' 查询有效优惠券信息完成");
        return CouponOns;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<CouponOn> CouponOns = null;
		//则查询返回所有
		CouponOns = this.couponOnService.queryAllCouponOns();
		logger.info("查询所有有效优惠券信息完成");
        return CouponOns;
    }
}
