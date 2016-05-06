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
import com.makao.entity.OrderOn;
import com.makao.service.IOrderOnService;

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
		OrderOn OrderOn = (OrderOn)this.orderOnService.getOrderOnById(id);
		return OrderOn;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.orderOnService.deleteOrderOn(id);
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
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody OrderOn OrderOn) {
		int res = this.orderOnService.insertOrderOn(OrderOn);
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
		int res = this.orderOnService.updateOrderOn(OrderOn);
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
		OrderOns = this.orderOnService.queryOrderOnByName(name);
		logger.info("根据关键字: '"+name+"' 查询有效订单信息完成");
        return OrderOns;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<OrderOn> OrderOns = null;
		//则查询返回所有
		OrderOns = this.orderOnService.queryAllOrderOns();
		logger.info("查询所有有效订单信息完成");
        return OrderOns;
    }
}
