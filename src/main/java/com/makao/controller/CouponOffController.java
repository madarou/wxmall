package com.makao.controller;

import java.util.ArrayList;
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
import com.makao.entity.CouponOff;
import com.makao.entity.CouponOn;
import com.makao.service.ICouponOffService;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@CrossOrigin(origins = "*", maxAge = 3600)
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
		CouponOff CouponOff = (CouponOff)this.couponOffService.getById(id);
		return CouponOff;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.couponOffService.deleteById(id);
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
		int res = this.couponOffService.insert(CouponOff);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加失效优惠券成功id=" + CouponOff.getId());
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("增加失效优惠券成功失败id=" + CouponOff.getId());
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	@RequestMapping(value = "/all/{cityid:\\d+}/{userid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object all(@PathVariable("cityid") int cityid,@PathVariable("userid") int userid) {
        JSONObject jsonObject = new JSONObject();
		List<CouponOff> os = this.couponOffService.queryAllByUserId("Coupon_"+cityid+"_off",userid);
		System.out.print(os.size());
		List<CouponOff> offs = new ArrayList<CouponOff>();
		//只返回已过期的，不返回已使用的，已使用的券里面会有overdueDate字段记录使用时间
		for(CouponOff c : os){
			if(c.getOverdueDate()==null){
				offs.add(c);
			}
		}
		logger.info("查询城市id："+cityid+" 中的用户id为:"+userid+"的所有失效coupoff完成");
		jsonObject.put("msg", "200");
		jsonObject.put("coupons", offs);
		return jsonObject;
    }
	
	@RequestMapping(value = "/{cityid:\\d+}/{couponid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody Object get(@PathVariable("cityid") int cityid, @PathVariable("couponid") int couponid) {
		JSONObject jsonObject = new JSONObject();
		CouponOff couponoff = this.couponOffService.queryByCouponId("Coupon_"+cityid+"_off", couponid);
		logger.info("查询失效优惠券id："+couponid+" 信息完成(所属city:"+cityid+")");
		jsonObject.put("msg", "200");
		jsonObject.put("coupoff", couponoff);
		return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody CouponOff CouponOff) {
		int res = this.couponOffService.update(CouponOff);
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
		CouponOffs = this.couponOffService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询失效优惠券信息完成");
        return CouponOffs;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<CouponOff> CouponOffs = null;
		//则查询返回所有
		CouponOffs = this.couponOffService.queryAll();
		logger.info("查询所有失效优惠券信息完成");
        return CouponOffs;
    }
}
