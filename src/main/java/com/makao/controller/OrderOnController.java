package com.makao.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.makao.entity.City;
import com.makao.entity.OrderOn;
import com.makao.entity.Supervisor;
import com.makao.entity.User;
import com.makao.entity.Vendor;
import com.makao.service.ICityService;
import com.makao.service.IOrderOnService;
import com.makao.service.ISupervisorService;
import com.makao.service.IVendorService;
import com.makao.utils.OrderNumberUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/orderOn")
public class OrderOnController {
	private static final Logger logger = Logger.getLogger(OrderOnController.class);
	@Resource
	private IOrderOnService orderOnService;
	@Resource
	private ICityService cityService;
	@Resource
	private IVendorService vendorService;
	@Resource
	private ISupervisorService supervisorService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody OrderOn get(@PathVariable("id") Integer id)
	{
		logger.info("获取有效订单信息id=" + id);
		OrderOn OrderOn = (OrderOn)this.orderOnService.getById(id);
		return OrderOn;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.orderOnService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除有效订单信息成功id=" + id);
        	jsonObject.put("msg", "删除有效订单信息成功");
		}
		else{
			logger.info("删除有效订单信息失败id=" + id);
        	jsonObject.put("msg", "删除有效订单信息失败");
		}
        return jsonObject;
    }
	/**
	 * @param OrderOn
	 * @return
	 * curl l -H "Content-type: application/json" -X POST -d '{"productIds":"2=3.50=3,3=4.00=1","productNames":"海南小番茄=3.50=3,广东蜜桃=4.00=1","receiverName":"郭德纲","phoneNumber":"17638372821","address":"上海复旦大学","couponId":3,"couponPrice":"2.00","totalPrice":"14.5","comment":"越快越好","status":"未确认","cityarea":"常州-某某区","userId":1,"areaId":1,"cityId":1}' 'http://localhost:8080/orderOn/new'
	 * 
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody OrderOn OrderOn) {
		OrderOn.setNumber(OrderNumberUtils.generateOrderNumber());
		OrderOn.setOrderTime(new Timestamp(System.currentTimeMillis()));
		OrderOn.setPayType("微信安全支付");//现在只有这种支付方式
		OrderOn.setReceiveType("送货上门");//现在只有这种收货方式
		int res = this.orderOnService.insert(OrderOn);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加有效订单成功id=" + OrderOn.getNumber());
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("增加有效订单成功失败id=" + OrderOn.getNumber());
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody OrderOn OrderOn) {
		int res = this.orderOnService.update(OrderOn);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改有效订单信息成功id=" + OrderOn.getId());
        	jsonObject.put("msg", "修改有效订单信息成功");
		}
		else{
			logger.info("修改有效订单信息失败id=" + OrderOn.getId());
        	jsonObject.put("msg", "修改有效订单信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<OrderOn> OrderOns = null;
		//则根据关键字查询
		OrderOns = this.orderOnService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询有效订单信息完成");
        return OrderOns;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<OrderOn> OrderOns = null;
		//则查询返回所有
		OrderOns = this.orderOnService.queryAll("Order_1_on");//暂时写成这样
		logger.info("查询所有有效订单信息完成");
        return OrderOns;
    }
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 订单被取消，填写备注
	 */
	@RequestMapping(value = "/vcancel/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vcancel(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		String vcomment = paramObject.getString("vcomment");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			int res = this.orderOnService.cancelOrder(vendor.getCityId(),orderid,vcomment);
			if(res==0){
				jsonObject.put("msg", "200");
				return jsonObject;
			}
			else{
				jsonObject.put("msg", "201");
				return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
		return jsonObject;
    }
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 开始配送订单，即将其状态设为已处理
	 */
	@RequestMapping(value = "/vdistribute/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vdistribute(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			int res = this.orderOnService.distributeOrder(vendor.getCityId(),orderid);
			if(res==0){
				jsonObject.put("msg", "200");
				return jsonObject;
			}
			else{
				jsonObject.put("msg", "201");
				return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
		return jsonObject;
    }
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 完成配送订单，即将其状态设为已完成
	 */
	@RequestMapping(value = "/vfinish/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vfinish(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			int res = this.orderOnService.finishOrder(vendor.getCityId(),orderid);
			if(res==0){
				jsonObject.put("msg", "200");
				return jsonObject;
			}
			else{
				jsonObject.put("msg", "201");
				return jsonObject;
			}
		}
		jsonObject.put("msg", "201");
		return jsonObject;
    }
	
	@RequestMapping(value = "/s_queryall/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody ModelAndView query_All(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_orderOn");  
		if(token==null){
			return modelAndView;
		}
		Supervisor supervisor = this.supervisorService.getById(id);
		List<City> cites = this.cityService.queryAll();
		List<OrderOn> orderOns = new LinkedList<OrderOn>();
		if(supervisor!=null){
			for(City c : cites){
				List<OrderOn> os = this.orderOnService.queryAll("Order_"+c.getId()+"_on");
				if(os!=null)
					orderOns.addAll(os);
			}
		}
		logger.info("查询所有有效订单信息完成");
		modelAndView.addObject("id", id);  
		modelAndView.addObject("token", token); 
	    modelAndView.addObject("ordersOn", orderOns);   
	    return modelAndView;
    }
	
	
	@RequestMapping(value = "/v_query/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView areaQuery(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOn");  
		if(token==null){
			return modelAndView;
		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token);   
		return modelAndView;
    }
	
	@RequestMapping(value = "/v_query_queue/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView v_query_queue(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOn_queue");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOn> orders = null;
		if(vendor!=null)
			orders = this.orderOnService.queryQueueByAreaId("Order_"+vendor.getCityId()+"_on",vendor.getAreaId());
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders);   
		return modelAndView;
    }
	
	@RequestMapping(value = "/v_query_process/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView v_query_process(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOn_process");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOn> orders = null;
		if(vendor!=null)
			orders = this.orderOnService.queryProcessByAreaId("Order_"+vendor.getCityId()+"_on",vendor.getAreaId());
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders);   
		return modelAndView;
    }
}
