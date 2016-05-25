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
import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;
import com.makao.entity.Supervisor;
import com.makao.entity.Vendor;
import com.makao.service.ICityService;
import com.makao.service.IOrderOffService;
import com.makao.service.ISupervisorService;
import com.makao.service.IVendorService;
import com.makao.utils.OrderNumberUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@Controller
@RequestMapping("/orderOff")
public class OrderOffController {
	private static final Logger logger = Logger.getLogger(OrderOffController.class);
	@Resource
	private IOrderOffService orderOffService;
	@Resource
	private IVendorService vendorService;
	@Resource
	private ISupervisorService supervisorService;
	@Resource
	private ICityService cityService;
	
	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
	public @ResponseBody OrderOff get(@PathVariable("id") Integer id)
	{
		logger.info("获取失效订单信息id=" + id);
		OrderOff OrderOff = (OrderOff)this.orderOffService.getById(id);
		return OrderOff;
	}
	
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object delete(@PathVariable("id") Integer id) {
        int res = this.orderOffService.deleteById(id);
        JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("删除失效订单信息成功id=" + id);
        	jsonObject.put("msg", "删除失效订单信息成功");
		}
		else{
			logger.info("删除失效订单信息失败id=" + id);
        	jsonObject.put("msg", "删除失效订单信息失败");
		}
        return jsonObject;
    }
	/**
	 * @param OrderOff
	 * @return
	 * 该方法只是测试时生成数据用，因为OrderOff里的东西都是在OrderOn里直接移过去的
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestBody OrderOff OrderOff) {
        OrderOff.setNumber(OrderNumberUtils.generateOrderNumber());
		OrderOff.setOrderTime(new Timestamp(System.currentTimeMillis()));
		OrderOff.setFinalTime(new Timestamp(System.currentTimeMillis()));
		OrderOff.setPayType("微信安全支付");//现在只有这种支付方式
		OrderOff.setReceiveType("送货上门");//现在只有这种收货方式
		int res = this.orderOffService.insert(OrderOff);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加失效订单成功id=" + OrderOff.getNumber());
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("增加失效订单成功失败id=" + OrderOff.getNumber());
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Object update(@RequestBody OrderOff OrderOff) {
		int res = this.orderOffService.update(OrderOff);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("修改失效订单信息成功id=" + OrderOff.getId());
        	jsonObject.put("msg", "修改失效订单信息成功");
		}
		else{
			logger.info("修改失效订单信息失败id=" + OrderOff.getId());
        	jsonObject.put("msg", "修改失效订单信息失败");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/query/{name:\\S+}", method = RequestMethod.GET)
    public @ResponseBody
    Object queryByName(@PathVariable("name")String name) {
		List<OrderOff> OrderOffs = null;
		//则根据关键字查询
		OrderOffs = this.orderOffService.queryByName(name);
		logger.info("根据关键字: '"+name+"' 查询失效订单信息完成");
        return OrderOffs;
    }
	
	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public @ResponseBody
    Object queryAll() {
		List<OrderOff> OrderOffs = null;
		//则查询返回所有
		OrderOffs = this.orderOffService.queryAll();
		logger.info("查询所有失效订单信息完成");
        return OrderOffs;
    }
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 处理退货订单，将其状态设置为退货中
	 */
	@RequestMapping(value = "/vrefund/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vrefund(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			int res = this.orderOffService.refundOrder(vendor.getCityId(),orderid);
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
	 * 处理需要退款的订单，将其状态设置为已退款
	 */
	@RequestMapping(value = "/srefund/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object srefund(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		int cityid = paramObject.getIntValue("cityid");
		JSONObject jsonObject = new JSONObject();
		Supervisor supervisor = this.supervisorService.getById(id);
		if(supervisor!=null){
			int res = this.orderOffService.finishRefundOrder(cityid,orderid);
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
	 * 设置退货订单的状态从退货中变成已退货
	 */
	@RequestMapping(value = "/vfinish/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vfinish(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			int res = this.orderOffService.finishReturnOrder(vendor.getCityId(),orderid);
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
	 * 将退货中的订单从退货申请中或者已退货变成已取消退货
	 */
	@RequestMapping(value = "/vcancel/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vcancel(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		String vcomment = paramObject.getString("vcomment");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			int res = this.orderOffService.cancelRefundOrder(vendor.getCityId(),orderid,vcomment);
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
	
	@RequestMapping(value = "/s_queryall", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView query_All() {
		//List<OrderOn> orderOns = null;
		//则查询返回所有
		//orderOns = this.orderOnService.queryAll();
		//这里假设放一些东西进去
		List<OrderOff> orderOffs = new ArrayList<OrderOff>();
		OrderOff oo = new OrderOff();
		oo.setAddress("ddddddddd");
		orderOffs.add(oo);
		logger.info("查询所有失效订单信息完成");
		ModelAndView modelAndView = new ModelAndView();  
	    modelAndView.addObject("ordersOff", orderOffs);  
	    modelAndView.setViewName("s_orderOff");  
	    return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询所有已完成的订单
	 */
	@RequestMapping(value = "/v_query_done/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView areaQuery(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_done");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		if(vendor!=null)
			orders = this.orderOffService.queryDoneByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders);     
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询所有已取消的、已退货和已取消退货的订单
	 */
	@RequestMapping(value = "/v_query_cancel/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView vquerycancel(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_cancel");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		if(vendor!=null)
			orders = this.orderOffService.queryCancelByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders);     
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询退货中、已退货的订单
	 */
	@RequestMapping(value = "/v_query_refund/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView vqueryrefund(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_refund");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		if(vendor!=null)
			orders = this.orderOffService.queryRefundByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders);     
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询所有已取消的和已退货的订单，在退款订单里显示，因为这些是需要退款的
	 */
	@RequestMapping(value = "/s_query_refund/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView squeryrefund(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_orderOff_refund");  
		if(token==null){
			return modelAndView;
		}
		Supervisor supervisor = this.supervisorService.getById(id);
		List<City> cites = this.cityService.queryAll();
		List<OrderOff> orderOffs = new LinkedList<OrderOff>();
		if(supervisor!=null){
			for(City c : cites){
				List<OrderOff> os = this.orderOffService.queryAllCanceledAndReturned("Order_"+c.getId()+"_off");
				if(os!=null)
					orderOffs.addAll(os);
			}
		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orderOffs", orderOffs);     
		return modelAndView;
    }
}
