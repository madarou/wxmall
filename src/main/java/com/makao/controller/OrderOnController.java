package com.makao.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.makao.auth.AuthPassport;
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
import com.makao.weixin.po.pay.Unifiedorder;
import com.makao.weixin.utils.HttpUtil;
import com.makao.weixin.utils.SignatureUtil;
import com.makao.weixin.utils.WeixinConstants;
import com.thoughtworks.xstream.XStream;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
@CrossOrigin(origins = "*", maxAge = 3600)
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
	
//	@RequestMapping(value="/{id:\\d+}",method = RequestMethod.GET)
//	public @ResponseBody OrderOn get(@PathVariable("id") Integer id)
//	{
//		logger.info("获取有效订单信息id=" + id);
//		OrderOn OrderOn = (OrderOn)this.orderOnService.getById(id);
//		return OrderOn;
//	}
	
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
	@AuthPassport
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestParam(value="token", required=false) String token, @RequestBody OrderOn OrderOn) {
		OrderOn.setNumber(OrderNumberUtils.generateOrderNumber());
		OrderOn.setOrderTime(new Timestamp(System.currentTimeMillis()));
		OrderOn.setPayType("微信安全支付");//现在只有这种支付方式
		OrderOn.setReceiveType("送货上门");//现在只有这种收货方式
		if(OrderOn.getStatus()==null||"".equals(OrderOn.getStatus())){
			OrderOn.setStatus("排队中");
		}
		//这里可以验证传来的userid在数据库对应的openid与服务端的(token,openid)对应的openid是否相同,
		//防止恶意访问api提交订单，通过userId与openid的验证至少多了一层验证。获取到的openid还会用来后面
		//订单生成后，使用微信接口向用户发送模板消息
		int res = this.orderOnService.insert(OrderOn);
		JSONObject jsonObject = new JSONObject();
		if(res==0){
			logger.info("增加有效订单成功id=" + OrderOn.getNumber());
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("增加有效订单失败id=" + OrderOn.getNumber());
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/unifiedorder", method = RequestMethod.POST)
    public @ResponseBody
    void unifiedorder(@RequestParam(value="token", required=false) String token) {
		Unifiedorder u = new Unifiedorder();
		u.setAppid(WeixinConstants.APPID);
		u.setMch_id(WeixinConstants.MCHID);
		u.setSub_mch_id(WeixinConstants.MCHID);
		u.setNonce_str(SignatureUtil.getNonceStr());
		u.setBody("测试支付(商品描述body)");
		u.setOut_trade_no(OrderNumberUtils.generateOrderNumber());
		u.setTotal_fee(1);
		u.setSpbill_create_ip("127.0.0.1");
		u.setNotify_url("http://www.baidu.com/");//接收微信支付异步通知回调地址
		u.setTrade_type("JSAPI");
		u.setOpenid("oNeZrwPHAKJ5jpLjZENmtVY5Y9u0");
		u.setDevice_info("WEB");
		
		//生成sign签名
		Map<Object, Object> parameters = new HashMap<Object, Object>();
		parameters.put("appid", u.getAppid());
		parameters.put("body", u.getBody());
		parameters.put("mch_id", u.getMch_id());
		parameters.put("nonce_str", u.getNonce_str());
		parameters.put("notify_url", u.getNotify_url());
		parameters.put("out_trade_no", u.getOut_trade_no());
		parameters.put("total_fee", u.getTotal_fee());
		parameters.put("trade_type", u.getTrade_type());
		parameters.put("spbill_create_ip", u.getSpbill_create_ip());
		parameters.put("openid", u.getOpenid());
		parameters.put("device_info", u.getDevice_info());
		u.setSign(SignatureUtil.createSign(parameters, WeixinConstants.PAY_KEY));
		
		XStream xstream = new XStream();
		xstream.alias("xml", Unifiedorder.class);
		String xml = xstream.toXML(u);
	    logger.info("统一下单xml为:\n" + xml);
	    String returnXml = HttpUtil.doPostXml(WeixinConstants.UNIFIEDORDER_URL, xml);
	    logger.info("返回结果:" + returnXml);
	}
	
	/**
	 * @param token
	 * @param OrderOn
	 * @return
	 * 确认收货
	 */
	@AuthPassport
	@RequestMapping(value = "/confirm/{cityid:\\d+}/{orderid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object confirm(@PathVariable("cityid") int cityid, @PathVariable("orderid") int orderid,
    		@RequestParam(value="token", required=false) String token) {
		JSONObject jsonObject = new JSONObject();
		int res = this.orderOnService.confirmGetOrder(cityid, orderid);
		if(res==0){
			logger.info("确认收货订单成功id=" + orderid + " 所属城市id:"+cityid);
        	jsonObject.put("msg", "200");
		}
		else{
			logger.info("确认收货订单失败id=" + orderid + " 所属城市id:"+cityid);
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	@RequestMapping(value = "/all/{cityid:\\d+}/{userid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody Object all(@PathVariable("cityid") int cityid, @PathVariable("userid") int userid) {
		JSONObject jsonObject = new JSONObject();
		List<OrderOn> os = this.orderOnService.queryByUserId("Order_"+cityid+"_on", userid);
		logger.info("查询用户id："+userid+"的所有有效订单信息完成(所属city:"+cityid+")");
		jsonObject.put("msg", "200");
		jsonObject.put("orders", os);
		return jsonObject;
    }
	
	@RequestMapping(value = "/{cityid:\\d+}/{orderid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody Object get(@PathVariable("cityid") int cityid, @PathVariable("orderid") int orderid) {
		JSONObject jsonObject = new JSONObject();
		OrderOn os = this.orderOnService.queryByOrderId("Order_"+cityid+"_on", orderid);
		logger.info("查询订单id："+orderid+" 信息完成(所属city:"+cityid+")");
		jsonObject.put("msg", "200");
		jsonObject.put("order", os);
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
	 * 开始配送订单，即将其状态设为配送中
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
	 * 完成配送订单，即将其状态设为已配送
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
	
	@RequestMapping(value = "/v_query_distributed/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView v_query_distributed(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOn_distributed");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOn> orders = null;
		if(vendor!=null)
			orders = this.orderOnService.queryDistributedByAreaId("Order_"+vendor.getCityId()+"_on",vendor.getAreaId());
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders);   
		return modelAndView;
    }
}
