package com.makao.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.makao.entity.OrderOn;
import com.makao.service.IOrderOnService;
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
			logger.info("增加有效订单成功id=" + OrderOn.getId());
        	jsonObject.put("msg", "增加有效订单成功");
		}
		else{
			logger.info("增加有效订单成功失败id=" + OrderOn.getId());
        	jsonObject.put("msg", "增加有效订单失败");
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
		OrderOns = this.orderOnService.queryAll();
		logger.info("查询所有有效订单信息完成");
        return OrderOns;
    }
	
	@RequestMapping(value = "/squeryall", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView query_All() {
		//List<OrderOn> orderOns = null;
		//则查询返回所有
		//orderOns = this.orderOnService.queryAll();
		//这里假设放一些东西进去
		List<OrderOn> orderOns = new ArrayList<OrderOn>();
		OrderOn oo = new OrderOn();
		oo.setAddress("ddddddddd");
		orderOns.add(oo);
		logger.info("查询所有有效订单信息完成");
		ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.addObject("ordersOn", orderOns);  
	    modelAndView.setViewName("s_orderOn");  
	    return modelAndView;
    }
	
	@RequestMapping(value = "/sareaquery", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView areaQuery() {
		//List<OrderOn> orderOns = null;
		//则查询返回所有
		//orderOns = this.orderOnService.queryAll();
		//这里假设放一些东西进去
		List<OrderOn> orderOns = new ArrayList<OrderOn>();
		OrderOn oo = new OrderOn();
		oo.setAddress("ddddddddd");
		orderOns.add(oo);
		logger.info("查询所有有效订单信息完成");
		ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.addObject("ordersOn", orderOns);  
	    modelAndView.setViewName("v_orderOn");  
	    return modelAndView;
    }
}
