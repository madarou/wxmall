package com.makao.controller;

import java.util.LinkedList;
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
import com.makao.entity.Area;
import com.makao.entity.City;
import com.makao.entity.Coupon;
import com.makao.entity.OrderOn;
import com.makao.entity.Supervisor;
import com.makao.service.ICityService;
import com.makao.service.ICouponService;
import com.makao.service.ISupervisorService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/coupon")
public class CouponController {
	private static final Logger logger = Logger.getLogger(CouponController.class);
	@Resource
	private ICouponService couponService;
	@Resource
	private ISupervisorService supervisorService;
	@Resource
	private ICityService cityService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody Coupon get(@PathVariable("id") Integer id)
	{
		logger.info("获取优惠券信息id=" + id);
		Coupon Coupon = (Coupon)this.couponService.getById(id);
		return Coupon;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.couponService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除优惠券信息成功id=" + id);
        	jsonObject.put("msg", "删除优惠券信息成功");
		}
		else{
			logger.info("删除优惠券信息失败id=" + id);
        	jsonObject.put("msg", "删除优惠券信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/new/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@PathVariable("id") int id, @RequestBody Coupon Coupon) {
		Supervisor supervisor = this.supervisorService.getById(id);
		JSONObject jsonObject = new JSONObject();
		if(supervisor!=null){
			int res = this.couponService.insert(Coupon);
			if(res==0){
				logger.info("增加优惠券成功id=" + Coupon.getId());
	        	jsonObject.put("msg", "200");
	        	return jsonObject;
			}
			else{
				logger.info("增加优惠券成功失败id=" + Coupon.getId());
	        	jsonObject.put("msg", "201");
	        	return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody Coupon Coupon) {
		int res = this.couponService.update(Coupon);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改优惠券信息成功id=" + Coupon.getId());
        	jsonObject.put("msg", "修改优惠券信息成功");
		}
		else{
			logger.info("修改优惠券信息失败id=" + Coupon.getId());
        	jsonObject.put("msg", "修改优惠券信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<Coupon> Coupons = null;
		//则根据关键字查询
		Coupons = this.couponService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询优惠券信息完成");
        return Coupons;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<Coupon> Coupons = null;
		//则查询返回所有
		Coupons = this.couponService.queryAll();
		logger.info("查询所有优惠券信息完成");
        return Coupons;
    }
	
	@RequestMapping(value = "/s_queryall/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll(@PathVariable("id") int id,
			@RequestParam(value = "token", required = false) String token) {
		 ModelAndView modelAndView = new ModelAndView();  
			modelAndView.setViewName("s_coupon");  
			if(token==null){
				return modelAndView;
			}
			Supervisor supervisor = this.supervisorService.getById(id);
			List<City> cites = this.cityService.queryAll();
			List<Coupon> coupons = new LinkedList<Coupon>();
			if(supervisor!=null){
				for(City c : cites){
					List<Coupon> os = this.couponService.queryAll("Coupon_"+c.getId());
					if(os!=null)
						coupons.addAll(os);
				}
				logger.info("查询所有coupon信息完成");
			}
		    modelAndView.addObject("id", id);  
		    modelAndView.addObject("token", token);   
			modelAndView.addObject("coupons", coupons);
			return modelAndView;
    }
}
