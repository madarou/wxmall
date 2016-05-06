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
import com.makao.entity.CouponOff;
import com.makao.service.ICouponOffService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/couponOff")
public class CouponOffController {
	private static final Logger logger = Logger.getLogger(CouponOffController.class);
	@Resource
	private ICouponOffService couponOffService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody CouponOff get(@PathVariable("id") Integer id)
	{
		logger.info("获取失效优惠券信息id=" + id);
		CouponOff CouponOff = (CouponOff)this.couponOffService.getCouponOffById(id);
		return CouponOff;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.couponOffService.deleteCouponOff(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除失效优惠券信息成功id=" + id);
        	jsonObject.put("msg", "删除失效优惠券信息成功");
		}
		else{
			logger.info("删除失效优惠券信息失败id=" + id);
        	jsonObject.put("msg", "删除失效优惠券信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody CouponOff CouponOff) {
		int res = this.couponOffService.insertCouponOff(CouponOff);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加失效优惠券成功id=" + CouponOff.getId());
        	jsonObject.put("msg", "增加失效优惠券成功");
		}
		else{
			logger.info("增加失效优惠券成功失败id=" + CouponOff.getId());
        	jsonObject.put("msg", "增加失效优惠券失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody CouponOff CouponOff) {
		int res = this.couponOffService.updateCouponOff(CouponOff);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改失效优惠券信息成功id=" + CouponOff.getId());
        	jsonObject.put("msg", "修改失效优惠券信息成功");
		}
		else{
			logger.info("修改失效优惠券信息失败id=" + CouponOff.getId());
        	jsonObject.put("msg", "修改失效优惠券信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<CouponOff> CouponOffs = null;
		//则根据关键字查询
		CouponOffs = this.couponOffService.queryCouponOffByName(name);
		logger.info("根据关键字: '"+name+"' 查询失效优惠券信息完成");
        return CouponOffs;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<CouponOff> CouponOffs = null;
		//则查询返回所有
		CouponOffs = this.couponOffService.queryAllCouponOffs();
		logger.info("查询所有失效优惠券信息完成");
        return CouponOffs;
    }
}
