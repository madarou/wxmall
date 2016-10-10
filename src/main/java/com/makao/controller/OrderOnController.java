package com.makao.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
import com.makao.entity.Area;
import com.makao.entity.City;
import com.makao.entity.CouponOn;
import com.makao.entity.OrderOn;
import com.makao.entity.OrderState;
import com.makao.entity.PointLog;
import com.makao.entity.Product;
import com.makao.entity.SmallOrder;
import com.makao.entity.Supervisor;
import com.makao.entity.TokenModel;
import com.makao.entity.User;
import com.makao.entity.Vendor;
import com.makao.service.IAreaService;
import com.makao.service.ICityService;
import com.makao.service.ICouponOnService;
import com.makao.service.IOrderOnService;
import com.makao.service.IPointService;
import com.makao.service.IProductService;
import com.makao.service.ISupervisorService;
import com.makao.service.IUserService;
import com.makao.service.IVendorService;
import com.makao.thread.AddInventoryThread;
import com.makao.thread.BatchSendMSGThread;
import com.makao.thread.SendMSGThread;
import com.makao.utils.MakaoConstants;
import com.makao.utils.OrderNumberUtils;
import com.makao.utils.RedisUtil;
import com.makao.weixin.po.pay.Unifiedorder;
import com.makao.weixin.utils.HttpUtil;
import com.makao.weixin.utils.JSSignatureUtil;
import com.makao.weixin.utils.SignatureUtil;
import com.makao.weixin.utils.WeixinConstants;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

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
	private IAreaService areaService;
	@Resource
	private IVendorService vendorService;
	@Resource
	private ISupervisorService supervisorService;
	@Resource
	private IProductService productService;
	@Resource
	private ICouponOnService couponOnService;
	@Resource
	private IUserService userService;
	@Resource
	private IPointService pointService;
	@Autowired
	private RedisUtil redisUtil;
	
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
	/**
	 * @param token
	 * @param OrderOn
	 * @return
	 * 最早的下单方法，由于要加入微信下单的过程和缓存校验库存，所以不用了，
	 * 但留作测试时直接往数据库中生成订单使用
	 */
//	@AuthPassport
//	@RequestMapping(value = "/new", method = RequestMethod.POST)
//    public @ResponseBody
//    Object add(@RequestParam(value="token", required=false) String token, @RequestBody OrderOn OrderOn) {
//		OrderOn.setNumber(OrderNumberUtils.generateOrderNumber());
//		OrderOn.setOrderTime(new Timestamp(System.currentTimeMillis()));
//		OrderOn.setPayType("微信安全支付");//现在只有这种支付方式
//		OrderOn.setReceiveType("送货上门");//现在只有这种收货方式
//		if(OrderOn.getStatus()==null||"".equals(OrderOn.getStatus())){
//			OrderOn.setStatus(OrderState.QUEUE.getCode()+"");
//		}
//		//这里可以验证传来的userid在数据库对应的openid与服务端的(token,openid)对应的openid是否相同,
//		//防止恶意访问api提交订单，通过userId与openid的验证至少多了一层验证。获取到的openid还会用来后面
//		//订单生成后，使用微信接口向用户发送模板消息
//		int res = this.orderOnService.insert(OrderOn);
//		JSONObject jsonObject = new JSONObject();
//		if(res!=0){
//			logger.info("增加有效订单成功id=" + OrderOn.getNumber());
//        	jsonObject.put("msg", "200");
//        	jsonObject.put("number", OrderOn.getNumber());
//		}
//		else{
//			logger.info("增加有效订单失败id=" + OrderOn.getNumber());
//        	jsonObject.put("msg", "201");
//		}
//        return jsonObject;
//    }
	
	@AuthPassport
	@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    Object add(@RequestParam(value="token", required=false) String token, @RequestBody SmallOrder smallOrder) {
		OrderOn OrderOn = new OrderOn();
		OrderOn.setNumber(OrderNumberUtils.generateOrderNumber());
		OrderOn.setOrderTime(new Timestamp(System.currentTimeMillis()));
		OrderOn.setPayType("微信安全支付");//现在只有这种支付方式
		OrderOn.setReceiveType("送货上门");//现在只有这种收货方式
		if(smallOrder.getStatus()==null||"".equals(smallOrder.getStatus())){
			OrderOn.setStatus(OrderState.NOT_PAID.getCode()+"");
		}else{
			OrderOn.setStatus(smallOrder.getStatus());
		}
		int cityId = smallOrder.getCityId();
		int areaId = smallOrder.getAreaId();
		
//		String idStr = smallOrder.getProductIds();
//		String numStr = smallOrder.getNums();
//		String[] ids = idStr.substring(1,idStr.length()-1).split(",");
//		String[] nums = numStr.substring(1, numStr.length()-1).split(",");
		String[] ids = smallOrder.getProductIds();
		String[] nums = smallOrder.getNums();
		
		StringBuilder sb = new StringBuilder();
		float totalPrice = 0.00f;
		for(int i=0; i<ids.length ; i++){
			Product p = this.productService.getById(Integer.valueOf(ids[i]), cityId, areaId);
			sb.append(p.getProductName()+"="+p.getPrice()+"="+nums[i]+",");
			totalPrice = totalPrice + Float.valueOf(p.getPrice())*Float.valueOf(nums[i]);
		}
		String productNames = sb.substring(0, sb.length()-1);//去掉最后一个逗号
		OrderOn.setProductNames(productNames);
		//OrderOn.setProductIds(idStr.substring(1,idStr.length()-1));
		StringBuilder sb2 = new StringBuilder();
		for(String s: ids){
			sb2.append(s+",");
		}
		OrderOn.setProductIds(sb2.substring(0, sb2.length()-1));
		
		String couponPrice = "0.00";
		if(smallOrder.getCouponId()>0){
			//如果用了优惠券，查找优惠券
			CouponOn couponOn = this.couponOnService.queryByCouponId("Coupon_"+smallOrder.getCityId()+"_on", smallOrder.getCouponId());
			if(couponOn.getUserId()==smallOrder.getUserId()){//确保该优惠券是该用户所有
				couponPrice = couponOn.getAmount();
			}
		}
		OrderOn.setReceiverName(smallOrder.getReceiverName());
		OrderOn.setPhoneNumber(smallOrder.getPhoneNumber());
		OrderOn.setAddress(smallOrder.getAddress());
		OrderOn.setReceiveTime(smallOrder.getReceiveTime());
		OrderOn.setCouponId(smallOrder.getCouponId());
		OrderOn.setCouponPrice(couponPrice);
		DecimalFormat fnum = new  DecimalFormat("##0.00"); //保留两位小数   
		totalPrice = totalPrice-Float.valueOf(couponPrice);
		OrderOn.setTotalPrice(fnum.format(totalPrice));
		OrderOn.setCityarea(smallOrder.getCityarea());
		OrderOn.setUserId(smallOrder.getUserId());
		OrderOn.setAreaId(smallOrder.getAreaId());
		OrderOn.setCityId(smallOrder.getCityId());
		Area area = this.areaService.getById(areaId);
		if(area!=null){
			OrderOn.setSender("由社享网-"+area.getCityName()+area.getAreaName()+"随机分配");
			OrderOn.setSenderPhone(area.getPhoneNumber());
			OrderOn.setCityarea(area.getCityName()+area.getAreaName());
		}
		//这里可以验证传来的userid在数据库对应的openid与服务端的(token,openid)对应的openid是否相同,
		//防止恶意访问api提交订单，通过userId与openid的验证至少多了一层验证。获取到的openid还会用来后面
		//订单生成后，使用微信接口向用户发送模板消息
		int res = this.orderOnService.insert(OrderOn);
		JSONObject jsonObject = new JSONObject();
		if(res!=0){
			logger.info("增加有效订单成功id=" + OrderOn.getNumber());
        	jsonObject.put("msg", "200");
        	jsonObject.put("number", OrderOn.getNumber());
        	jsonObject.put("id", res);
		}
		else{
			logger.info("增加有效订单失败id=" + OrderOn.getNumber());
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	/**
	 * @param token
	 * @param OrderOn
	 * @return
	 * 该方法在用户提交订单时调用，主要完成：
	 * 1.根据订单中的商品id到缓存中查找对应商品的库存
	 * 2.如果某个商品没有库存了，则返回该商品对应商品的名称，说明库存不足，下单失败
	 * 3.所有商品都有库存，则轮流为所有商品在缓存中减少对应数量的库存，如果中途有exec为null，循环执行，
	 *   直到所有商品库存减少的操作都完成，如果减少某个商品库存的过程中，减后库存为负，则对应商品的名称，说明库存不足，下单失败
	 * 4.生成订单OrderOn，存入缓存中，给定失效时间(即一定时间未支付则失效)
	 * 5.返回下单成功消息
	 */
	@AuthPassport
	@RequestMapping(value = "/neworder", method = RequestMethod.POST)
    public @ResponseBody
    Object addorder(@RequestParam(value="token", required=false) String token, @RequestBody SmallOrder smallOrder,
    		HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		TokenModel tm = (TokenModel) request.getAttribute("tokenmodel");
		String openid = tm.getOpenid();
		if(openid==null || "".equals(openid.trim())){
			logger.warn("订单提交失败，没有openid："+openid);
			jsonObject.put("msg", "201");
			return jsonObject;
		}
		
		int cityId = smallOrder.getCityId();
		int areaId = smallOrder.getAreaId();
		String[] ids = smallOrder.getProductIds();
		String[] nums = smallOrder.getNums();
		//依次检查当前的商品库存是否足够，有一个不够则返回失败
		for(String id: ids){
			Object object = redisUtil.redisQueryObject("pi_"+cityId+"_"+areaId+"_"+id.trim());
			if(object==null){//缓存里面如果没有，从数据库里读
				int inv = this.productService.getInventory(cityId, areaId, id);
				redisUtil.redisSaveInventory("pi_"+cityId+"_"+areaId+"_"+id.trim(), String.valueOf(inv));
				//每次更新销量后的缓存
				redisUtil.redisSaveInventory("lastpi_"+cityId+"_"+areaId+"_"+id.trim(), String.valueOf(inv));
			}
			int inventory = Integer.valueOf(redisUtil.redisQueryObject("pi_"+cityId+"_"+areaId+"_"+id.trim()));
			if(inventory<=0){
				logger.warn("订单提交失败，商品(cityId_areaId_Id): "+cityId+"_"+areaId+"_"+id.trim()+" 售罄");
				jsonObject.put("msg", "203");
				return jsonObject;
			}
		}
		//所有商品都有库存，则轮流为所有商品在缓存中减少对应数量的库存，如果中途有exec为null，循环执行
		for (int i = 0; i < nums.length; i++) {
			List<Object> rt = redisUtil.cutInventoryTx(
					"pi_" + cityId + "_" + areaId
							+ "_" + ids[i].trim(), Integer.valueOf(nums[i]));
			// 如果某商品扣除购买数量后，库存为负，下单失败，但是要将减掉的库存加回去
			if (((Long) rt.get(0)).intValue() <= 0) {
				// 另外起一个线程来回加减掉的库存
				AddInventoryThread ait = new AddInventoryThread("pi_"
						+ cityId + "_" + areaId + "_"
						+ ids[i].trim(), Integer.valueOf(nums[i]), redisUtil);
				new Thread(ait, "add inventory thread").start();

				logger.warn("订单提交失败，商品(cityId_areaId_Id): "
						+ cityId+ "_" + areaId + "_"
						+ ids[i] + " 库存不够");
				jsonObject.put("msg", "204");
				return jsonObject;
			}
		}
		
//		String[] ids = orderOn.getProductIds().split(",");
//		for(String id: ids){
//			Object object = redisUtil.redisQueryObject("pi_"+orderOn.getCityId()+"_"+orderOn.getAreaId()+"_"+id.trim());
//			if(object==null){//缓存里面如果没有，从数据库里读
//				int inv = this.productService.getInventory(orderOn.getCityId(), orderOn.getAreaId(), id);
//				redisUtil.redisSaveObject("pi_"+orderOn.getCityId()+"_"+orderOn.getAreaId()+"_"+id.trim(), inv);
//			}
//			int inventory = Integer.valueOf(redisUtil.redisQueryObject("pi_"+orderOn.getCityId()+"_"+orderOn.getAreaId()+"_"+id.trim()));
//			if(inventory<=0){
//				logger.warn("订单提交失败，商品(cityId_areaId_Id): "+orderOn.getCityId()+"_"+orderOn.getAreaId()+"_"+id.trim()+" 售罄");
//				jsonObject.put("msg", "203");
//				return jsonObject;
//			}
//		}
//		//所有商品都有库存，则轮流为所有商品在缓存中减少对应数量的库存，如果中途有exec为null，循环执行
//		String[] names = orderOn.getProductNames().split(",");
//		for(int i=0; i<ids.length; i++){
//			int productNum = Integer.parseInt(names[i].split("=")[2]);
//			List<Object> rt = redisUtil.cutInventoryTx("pi_"+orderOn.getCityId()+"_"+orderOn.getAreaId()+"_"+ids[i].trim(), productNum);
//			//如果某商品扣除购买数量后，库存为负，下单失败，但是要将减掉的库存加回去
//			if(((Long)rt.get(0)).intValue()<=0){
//				//另外起一个线程来回加减掉的库存
//				AddInventoryThread ait = new AddInventoryThread("pi_"+orderOn.getCityId()+"_"+orderOn.getAreaId()+"_"+ids[i].trim(),productNum,redisUtil);
//				new Thread(ait,"add inventory thread").start();
//				
//				logger.warn("订单提交失败，商品(cityId_areaId_Id): "+orderOn.getCityId()+"_"+orderOn.getAreaId()+"_"+ids[i]+" 库存不够");
//				jsonObject.put("msg", "204");
//				return jsonObject;
//			}
//		}
		OrderOn order = new OrderOn();
		order.setNumber(OrderNumberUtils.generateOrderNumber());
		order.setOrderTime(new Timestamp(System.currentTimeMillis()));
		order.setPayType("微信安全支付");//现在只有这种支付方式
		order.setReceiveType("送货上门");//现在只有这种收货方式
		order.setStatus(OrderState.NOT_PAID.getCode()+"");
		
		StringBuilder sb = new StringBuilder();
		float totalPrice = 0.00f;
		for(int i=0; i<ids.length ; i++){
			Product p = this.productService.getById(Integer.valueOf(ids[i]), cityId, areaId);
			sb.append(p.getProductName()+"="+p.getPrice()+"="+nums[i]+",");
			totalPrice = totalPrice + Float.valueOf(p.getPrice())*Float.valueOf(nums[i]);
		}
		String productNames = sb.substring(0, sb.length()-1);//去掉最后一个逗号
		order.setProductNames(productNames);
		StringBuilder sb2 = new StringBuilder();
		for(String s: ids){
			sb2.append(s+",");
		}
		order.setProductIds(sb2.substring(0, sb2.length()-1));
		
		String couponPrice = "0.00";
		if(smallOrder.getCouponId()>0){
			//如果用了优惠券，查找优惠券
			CouponOn couponOn = this.couponOnService.queryByCouponId("Coupon_"+cityId+"_on", smallOrder.getCouponId());
			if(couponOn.getUserId()==smallOrder.getUserId()){//确保该优惠券是该用户所有
				couponPrice = couponOn.getAmount();
			}
		}
		order.setReceiverName(smallOrder.getReceiverName());
		order.setPhoneNumber(smallOrder.getPhoneNumber());
		order.setAddress(smallOrder.getAddress());
		order.setReceiveTime(smallOrder.getReceiveTime());
		order.setCouponId(smallOrder.getCouponId());
		order.setCouponPrice(couponPrice);
		DecimalFormat fnum = new  DecimalFormat("##0.00"); //保留两位小数   
		totalPrice = totalPrice-Float.valueOf(couponPrice);
		order.setTotalPrice(fnum.format(totalPrice));
		order.setCityarea(smallOrder.getCityarea());
		order.setUserId(smallOrder.getUserId());
		order.setAreaId(areaId);
		order.setCityId(cityId);
		Area area = this.areaService.getById(areaId);
		if(area!=null){
			order.setSender("由社享网-"+area.getCityName()+area.getAreaName()+"随机分配");
			order.setSenderPhone(area.getPhoneNumber());
			order.setCityarea(area.getCityName()+area.getAreaName());
		}
		
		//int res = this.orderOnService.insert(orderOn);//这里不再实际往数据库里生成订单，只放在缓存中，提交支付请求时才生成
		//为了前端方便，还是需要在数据库里插入，因为前端需要id，插入后才有id
		int res = this.orderOnService.insert(order);
		order.setId(res);
		redisUtil.redisSaveObject(order.getNumber(), order, 15);
		//按用户-》未支付订单列表的形式存储每个用户未支付的订单列表，用户在未收货订单查询时一起返回
		redisUtil.redisSaveList("uo_"+smallOrder.getUserId(), order, 0);
		if(redisUtil.redisQueryObject(order.getNumber())!=null){
			logger.info("增加有效订单成功id=" + order.getNumber());
			jsonObject.put("number", order.getNumber());
        	jsonObject.put("msg", "200");
        	jsonObject.put("id", res);
		}
		else{
			logger.info("增加有效订单失败id=" + order.getNumber());
        	jsonObject.put("msg", "201");
		}
        return jsonObject;
    }
	
	
	/**
	 * @param token
	 * @param paramObject
	 * @param request
	 * @param response
	 * @throws IOException
	 * 模拟支付，不通过微信
	 */
	@AuthPassport
	@RequestMapping(value = "/pay2", method = RequestMethod.GET)
    public @ResponseBody
    void pay(@RequestParam(value="token", required=false) String token,@RequestParam(value="number", required=false) String number, 
    		HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String page = "";
		//String orderNumber = paramObject.getString("number");
		
		//模拟的数据无法从缓存区到cityid的值，所以一次从两个模拟的成熟中找
		OrderOn oo = this.orderOnService.confirmMoney("1",number);
		if(oo!=null){
			oo = this.orderOnService.confirmMoney("2",number);
		}

//				page = "<link rel=\"stylesheet\" href=\"https://res.wx.qq.com/open/libs/weui/0.4.3/weui.min.css\">"
//						+ "<div class=\"wxapi_container\">"
//								+"<div class=\"lbox_close wxapi_form\">"
//									+ "<button class=\"btn btn_primary\" id=\"chooseWXPay\">支付订单</button>"
//								+"</div>"
//						+ "</div>"
//						+"<script src=\"http://res.wx.qq.com/open/js/jweixin-1.0.0.js\"></script>"
//						+"<script>"
//							+ "var btn2 = document.getElementById(\"chooseWXPay\");"
//							+ "btn2.onclick=function(){"
//								+ "alert('支付成功');"
//							+ "}"
//						+ "</script>";
				page = "<!DOCTYPE html>"
						+ "<html>"
						+ "<head>"
							+ "<meta charset=\"utf-8\">"
							+ "<title>订单支付</title>"
							+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=0\">"
							+ "<link rel=\"stylesheet\" href=\"http://115.159.109.12:8080/css/weixin.css\">"
						+ "</head>"
						+"<body>"
							+ "<div class=\"wxapi_container\">"
								+"<div class=\"lbox_close wxapi_form\">"
									+ "<h3 id=\"menu-pay\">超级社区</h3>"
									+ "<span class=\"desc\">订单总价：￥xx.xx</span>"
									+ "<button class=\"btn btn_primary\" id=\"chooseWXPay\">支付订单</button>"
								+"</div>"
							+ "</div>"
						+ "</body>"
						+"<script src=\"http://res.wx.qq.com/open/js/jweixin-1.0.0.js\"></script>"
						+"<script>"
							+ "var btn2 = document.getElementById(\"chooseWXPay\");"
							+ "btn2.onclick=function(){"
								+ "alert('支付成功');"
							+ "}"
						+ "</script>"
					+ "</html>";
		out.write(page);
	}
	
	@AuthPassport
	@RequestMapping(value = "/pay3", method = RequestMethod.GET)
    public @ResponseBody
    Object pay3(@RequestParam(value="token", required=false) String token,@RequestParam(value="number", required=false) String number, 
    		HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		//模拟的数据无法从缓存区到cityid的值，所以一次从两个模拟的成熟中找
		OrderOn oo = this.orderOnService.confirmMoney("1",number);
		if(oo!=null){
			oo = this.orderOnService.confirmMoney("2",number);
		}
		JSONObject jsonObject = new JSONObject();
        	jsonObject.put("msg", "200");
        	jsonObject.put("appId", "appId");
        	jsonObject.put("timestamp1", "timestamp1");
        	jsonObject.put("nonceStr1", "nonceStr1");
        	jsonObject.put("signature", "signature");
        	jsonObject.put("timestamp2", "timestamp2");
        	jsonObject.put("nonceStr2", "nonceStr2");
        	jsonObject.put("package", "package");
        	jsonObject.put("signType", "MD5");
        	jsonObject.put("paySign", "paySign");
        return jsonObject;

	}
	
	/**
	 * @param token
	 * @param request
	 * @param response
	 * @throws IOException
	 * 支付订单，过程包括:
	 * 		1.通过token获取到下单用户的openid
	 * 		2.根据request中的内容和openid，调用微信unified接口下单
	 * 		3.下单成功后，立刻获取下单后的prepay_id以及其他信息生成订单支付需要的参数
	 * 		4.下单成功后，在本地生成orderOn记录，状态为"未支付"
	 * 		5.将订单支付的参数填充到返回的支付页面一并返回
	 * 		6.在返回的支付页面的success: function (res)中根据res的结果决定前面页面的跳转
	 * 		7.真正成功的支付处理在postPay中完成，在那里根据orderid到本地数据库中更新对应订单的状态为"排队中"
	 */
	@AuthPassport
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
    public @ResponseBody
    Object payOrder(@RequestParam(value="token", required=false) String token,@RequestParam(value="number", required=false) String orderNumber, 
    		HttpServletRequest request,HttpServletResponse response) throws IOException {
		String page = "";
		JSONObject jsonObject = new JSONObject();
		
		TokenModel tm = (TokenModel) request.getAttribute("tokenmodel");
		String openid = tm.getOpenid();
		if(openid==null || "".equals(openid.trim())){
			page = "订单"+orderNumber+"支付失败，没有openid："+openid;
			logger.warn(page);
			jsonObject.put("msg", "201");
			return jsonObject;
		}
		//String orderNumber = paramObject.getString("number");
		OrderOn orderOn = (OrderOn)redisUtil.redisQueryObject(orderNumber);
		if(orderOn==null){
			page = "订单支付失败，订单已过期，order number=："+orderNumber;
			logger.info(page);
			//从数据库里删除，由于没有cityid，只有number不好删除，可集中删除
			//this.orderOnService.deleteByNumber(orderNumber);
			jsonObject.put("msg", "202");
			return jsonObject;
		}
		
		// 生成微信订单
		Unifiedorder u = new Unifiedorder();
		u.setAppid(WeixinConstants.APPID);
		u.setMch_id(WeixinConstants.MCHID);
		u.setNonce_str(SignatureUtil.getNonceStr());
		u.setBody("超级社区商品购买订单");
		u.setOut_trade_no(orderOn.getNumber());
		String price = orderOn.getTotalPrice();
		//u.setTotal_fee(Integer.parseInt(price.split("\\.")[0]+price.split("\\.")[1]));//单位为分
		u.setTotal_fee(1);//单位为分
		u.setSpbill_create_ip(request.getRemoteAddr());//用户下单的ip
		u.setNotify_url(MakaoConstants.SERVER_DOMAIN+"/orderOn/postPay");// 接收微信支付异步通知回调地址
		u.setTrade_type("JSAPI");
		u.setOpenid(openid);
		u.setDevice_info("WEB");
		u.setAttach(String.valueOf(orderOn.getCityId()));//将cityId作为附加数据传入，因为在postPay中需要通过它确定orderOn是具体哪张表
		u.setDetail(orderToJson(orderOn));
		// 还有签名没有，下面生成sign签名
		// 生成sign签名，这里必须用SortedMap，因为签名算法里key值是要排序的
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
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
		parameters.put("attach", u.getAttach());
		parameters.put("detail", u.getDetail());
		u.setSign(SignatureUtil.createSign(parameters, WeixinConstants.PAY_KEY));
		
		// 提交到微信统一订单接口，用xml格式提交和接收
		// 这里XStream是转化-，防止mch_id被转化为了mdc__id
		XStream xstream = new XStream(new DomDriver("UTF-8",
				new XmlFriendlyNameCoder("-_", "_")));
		xstream.alias("xml", Unifiedorder.class);
		String xml = xstream.toXML(u);
		Map<String, String> returnXml = HttpUtil.doPostXmlAndParse(
				WeixinConstants.UNIFIEDORDER_URL, xml);
		if (returnXml == null) {
			page = "订单号: "+orderOn.getNumber()+"提交到微信时下单失败!";
			logger.warn(page);
			jsonObject.put("msg", "203");
			return jsonObject;
		}
		for (Map.Entry<String, String> entry : returnXml.entrySet()) {  
		    logger.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
		}  
		String prepay_id = returnXml.get("prepay_id");
		if (prepay_id == null || "".equals(prepay_id)) {
			page = "订单号: "+orderOn.getNumber()+"没有获取到prepay_id，参数错误下单失败!";
			logger.warn(page);
			jsonObject.put("msg", "201");
			return jsonObject;
		}
		// 为前端页面能够使用JSSDK设置签名
		//Map<String, String> wxConfig = JSSignatureUtil.getSignature(MakaoConstants.SERVER_DOMAIN+"/orderOn/pay");
		Map<String, String> wxConfig = JSSignatureUtil.getSignature(MakaoConstants.SERVER_DOMAIN+"/user/snsapi_userinfo");
		// 生成支付订单需要的参数和签名
		String timeStamp = SignatureUtil.getTimeStamp();
		String nonceStr = SignatureUtil.getNonceStr();
		String packages = "prepay_id=" + prepay_id;
		String signType = "MD5";
		SortedMap<Object, Object> signMap = new TreeMap<Object, Object>();
		signMap.put("appId", WeixinConstants.APPID);
		signMap.put("timeStamp", timeStamp);
		signMap.put("nonceStr", nonceStr);
		signMap.put("package", packages);
		signMap.put("signType", signType);
		// 生成统一订单时的签名算法与支付时使用的签名算法一样，只是用到的key=value不一样
		String paySign = SignatureUtil.createSign(signMap,
				WeixinConstants.PAY_KEY);
		jsonObject.put("msg", "200");
		jsonObject.put("appId", WeixinConstants.APPID);
		jsonObject.put("timestamp1", wxConfig.get("timestamp"));
		jsonObject.put("nonceStr1", wxConfig.get("nonceStr"));
		jsonObject.put("signature", wxConfig.get("signature"));
		jsonObject.put("timestamp2", timeStamp);
		jsonObject.put("nonceStr2", nonceStr);
		jsonObject.put("package", packages);
		jsonObject.put("signType", "MD5");
		jsonObject.put("paySign", paySign);
		return jsonObject;
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * 下单时设置的notify_url
	 * 支付完成后微信服务器访问的地址
	 * 这里通过微信request里的内容真正判断支付是否成功
	 * 支付成功后，根据request的订单id在本地数据库中更新对应订单状态为"排队中"
	 * 成功后要返回SUCCESS，否则微信服务器会多次轮询访问
	 */
	@RequestMapping(value = "/postPay", method = RequestMethod.POST)
    public @ResponseBody
    void postPay(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setHeader("content-type", "text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String page = "";
		Map<String, String> resultXML = null;//解析微信服务器发来的request的结果
		try {
			resultXML = HttpUtil.parseXmlRequest(request);
		} catch (DocumentException e) {
			logger.warn("解析微信支付后的响应出错", e);
			page = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[参数格式校验错误]]></return_msg></xml>";
			out.write(page);
			return;
		}
		if("SUCCESS".equals(resultXML.get("result_code"))){
		    String openid = resultXML.get("openid");
		    String orderNumber = resultXML.get("out_trade_no");
		    String cityid = resultXML.get("attach");
		    if(orderNumber!=null && !"".equals(orderNumber)){
		    	OrderOn oo = this.orderOnService.confirmMoney(cityid,orderNumber);//将订单的状态从未支付改为排队中，如果订单的配送时间是立即配送，则直接将状态改为待处理
		    	if(oo!=null){
		    		//将缓存中如果还存在的该订单删除
		    		OrderOn orderOn = (OrderOn)redisUtil.redisQueryObject(orderNumber);
		    		if(orderOn!=null){
		    			redisUtil.redisDeleteKey(orderNumber);
		    		}
		    		//通知微信端，已经收到支付结果了，不要再发了
		    		page = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		    		out.write(page);
		    		
		    		//推送下单成功的模板消息
		    		SendMSGThread snt = new SendMSGThread(openid,oo,1);
					new Thread(snt, "send order created mb msg thread").start();
					//如果是立即配送，需要推送配送的消息给管理员
					if("立即配送".equals(oo.getReceiveTime())){
						List<Vendor> vs = this.vendorService.getByAreaId(oo.getAreaId());
						for(Vendor v : vs){
			        		String open_id = v.getOpenid();
			        		if(open_id!=null&&!"".equals(open_id)){
			        			SendMSGThread snt2 = new SendMSGThread(open_id,oo,4);
			        			new Thread(snt2, "send order need distribute immediatelly mb msg thread").start();
			        		}
			        	}
					}
					//下单马上送积分至少要在付钱成功之后
		    		User u = this.userService.getById(oo.getUserId());
		    		u.setPoint(u.getPoint()+oo.getPoint());
		    		this.userService.update(u);
		    		//写入积分记录
		    		PointLog pl = new PointLog();
		    		pl.setName("商城消费");
		    		pl.setPoint(oo.getPoint());
		    		pl.setGetDate(new Date(System.currentTimeMillis()));
		    		pl.setComment("订单号:"+oo.getNumber());
		    		pl.setCityId(oo.getCityId());
		    		pl.setUserId(oo.getUserId());
		    		this.pointService.insertPointLog(pl);
		    		if(out!=null)
		    			out.close();
		    	}
		    }
		}
	}
	
	@RequestMapping(value = "/unifiedorder", method = RequestMethod.POST)
    public @ResponseBody
    void unifiedorder(@RequestParam(value="token", required=false) String token,HttpServletRequest request,HttpServletResponse response) throws IOException {
		Unifiedorder u = new Unifiedorder();
		u.setAppid(WeixinConstants.APPID);
		u.setMch_id(WeixinConstants.MCHID);
		//u.setSub_mch_id(WeixinConstants.MCHID);
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
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
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
		
		XStream xstream = new XStream(new DomDriver("UTF-8",new XmlFriendlyNameCoder("-_", "_")));
		xstream.alias("xml", Unifiedorder.class);
		String xml = xstream.toXML(u);
	    logger.info("统一下单xml为:\n" + xml);
	    String returnXml = HttpUtil.doPostXml(WeixinConstants.UNIFIEDORDER_URL, xml);
	    logger.info("返回结果:" + returnXml);
	    
	    //为前端页面能够使用JSSDK设置签名
	    String appId = WeixinConstants.APPID;
	    Map<String, String> wxConfig = JSSignatureUtil.getSignature("http://madarou1.ngrok.cc/orderOn/unifiedorder");
	    //将生成的订单需要在提交时使用的信息返回到前端页面
	    response.setHeader("content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String page = "<!DOCTYPE html>"
						+ "<html>"
						+ "<head>"
							+ "<meta charset=\"utf-8\">"
							+ "<title>订单支付</title>"
							+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=0\">"
							+ "<link rel=\"stylesheet\" href=\"/css/weixin.css\">"
						+ "</head>"
						+"<body>"
							+ "<div class=\"wxapi_container\">"
								+"<div class=\"lbox_close wxapi_form\">"
									+ "<h3 id=\"menu-pay\">微信支付接口</h3>"
									+ "<span class=\"desc\">发起一个微信支付请求</span>"
									+ "<button class=\"btn btn_primary\" id=\"chooseWXPay\">支付订单</button>"
								+"</div>"
							+ "</div>"
						+ "</body>"
						+"<script src=\"http://res.wx.qq.com/open/js/jweixin-1.0.0.js\"></script>"
						+"<script>"
							+ "wx.config({"
								+ "debug: true,"
								+ "appId: '"+appId+"',"
								+ "timestamp: "+wxConfig.get("timestamp")+","
								+ "nonceStr: '"+wxConfig.get("nonceStr")+"',"
								+ "signature: '"+wxConfig.get("signature")+"',"
								+ "jsApiList: ["
									+ "'chooseWXPay'"
								+ "]"
							+ "});"
							+ "wx.ready(function () {"
								+ "var btn = document.getElementById(\"chooseWXPay\");"
								+ "btn.onclick=function(){"
									+ "alert('click success');"
									+ "wx.chooseWXPay({"
										+ "timestamp: 1414723227,"
										+ "nonceStr: 'noncestr',"
										+ "package: 'addition=action_id%3dgaby1234%26limit_pay%3d&bank_type=WX&body=innertest&fee_type=1&input_charset=GBK&notify_url=http%3A%2F%2F120.204.206.246%2Fcgi-bin%2Fmmsupport-bin%2Fnotifypay&out_trade_no=1414723227818375338&partner=1900000109&spbill_create_ip=127.0.0.1&total_fee=1&sign=432B647FE95C7BF73BCD177CEECBEF8D',"
										+ "signType: 'MD5',"
										+ "paySign: 'bd5b1933cda6e9548862944836a9b52e8c9a2b69'"
									+ "});"
								+ "}"
							+ "});"
							+ "wx.error(function (res) {"
								+ "alert(res.errMsg);"
							+ "});"
						+ "</script>"
					+ "</html>";
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
	
	/**
	 * @param cityid
	 * @param orderid
	 * @param token
	 * @return
	 * 取消订单，从缓存里取消，也是从数据库里取消
	 */
	@AuthPassport
	@RequestMapping(value = "/cancel/{cityid:\\d+}/{orderid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object cancel(@PathVariable("cityid") int cityid, @PathVariable("orderid") int orderid,
    		@RequestParam(value="token", required=false) String token) {
		JSONObject jsonObject = new JSONObject();
		OrderOn res = this.orderOnService.userCancelOrder(cityid, orderid);
		if(res!=null){
			OrderOn order = this.redisUtil.redisQueryObject(res.getNumber());
			if(order!=null)
				this.redisUtil.redisDeleteKey(res.getNumber());
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
		//试图从缓存中找用户未支付的订单
		List<OrderOn> redis_orders = redisUtil.redisQueryList("uo_"+userid, OrderOn.class);
		if(redis_orders!=null)
			logger.info("redis_orders size: "+redis_orders.size());
		else
			logger.info("redis_orders size: "+0);
		//因为缓存中的order没有实际插入到数据库中，所以id=0，前端好像是根据id加载列表的，所以
		//缓存中如果有多个订单的话，只会被加载一个，所以counter作为缓存中临时订单的id，可以使前端
		//正常加载所有，如果订单实际插入数据库，会生成新的id，不会受到影响
		//int counter = -1;
		if(redis_orders!=null&&redis_orders.size()>0){
			for(OrderOn oo : redis_orders){
				logger.info("current redis order: "+oo.getNumber());
				String order_num = oo.getNumber();
				//检查该订单号的订单还在缓存中吗？不在就从"uo_"+userid对应的order list中删除掉
				OrderOn orderOn = (OrderOn)redisUtil.redisQueryObject(order_num);
				if(orderOn==null){
					redisUtil.redisDelListValue("uo_"+userid, oo);
				}
				//如果还存在，则将orderOn加入到os的顶部一起返回
				else{
					logger.info("adding order to os: "+orderOn.getNumber());
					//orderOn.setId(counter--);
					os.add(0, orderOn);
				}
			}
		}
		logger.info("查询用户id："+userid+"的所有有效订单信息完成(所属city:"+cityid+")");
		jsonObject.put("msg", "200");
		jsonObject.put("orders", os);
		logger.info("最后 os的大小: "+os.size());
		return jsonObject;
    }
	
	@RequestMapping(value = "/{cityid:\\d+}/{orderid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody Object get(@PathVariable("cityid") int cityid, @PathVariable("orderid") int orderid) {
		JSONObject jsonObject = new JSONObject();
		OrderOn os = this.orderOnService.queryByOrderId("Order_"+cityid+"_on", orderid);
		List<SmallProduct> sps = new ArrayList<SmallProduct>();
		if(os!=null){
			//重新组装商品列表，方便前端直接显示
			String[] pro_ids = os.getProductIds().split(",");
			String[] pro_names = os.getProductNames().split(",");
			for(int i=0; i<pro_ids.length; i++){
				String pid = pro_ids[i];
				String[] names = pro_names[i].split("=");
				Product p = (Product)this.productService.getById(Integer.valueOf(pid),os.getCityId(),os.getAreaId());
				SmallProduct sp = new SmallProduct();
				sp.setId(pid);
				sp.setName(names[0]);//这里要显示当时下单时的名称，防止后面修改了商品名后，引起误会
				//sp.setImage(p.getCoverSUrl());
				sp.setCityId(os.getCityId());
				sp.setAreaId(os.getAreaId());
				sp.setImage(p.getCoverSUrl());
				sp.setPrice(names[1]);//这里要显示当时用户下单时的价格，而不是现在商品实际的价格
				sp.setNumber(names[2]);
				sps.add(sp);
			}
			
		}
		logger.info("查询订单id："+orderid+" 信息完成(所属city:"+cityid+")");
		jsonObject.put("msg", "200");
		jsonObject.put("order", os);
		jsonObject.put("products", sps);
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
	@AuthPassport
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
	 * 商户为订单添加备注
	 */
	@AuthPassport
	@RequestMapping(value = "/vcomment/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vcomment(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		String vcomment = paramObject.getString("vcomment");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			int res = this.orderOnService.vcommentOrder(vendor.getCityId(),orderid,vcomment);
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
	 * @param orderid
	 * @return
	 * 排队中的订单状态被设置为待处理
	 */
	@AuthPassport
	@RequestMapping(value = "/vprocess/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vprocess(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		String orderid = paramObject.getString("orderid");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			OrderOn res = this.orderOnService.processOrder(vendor.getCityId(),orderid);
			if(res!=null){
				//如果绑定了微信，推送消息到配送员微信
				String vendor_openid = vendor.getOpenid();
				if(vendor_openid!=null&&!"".equals(vendor_openid)){
					SendMSGThread snt = new SendMSGThread(vendor_openid,res,4);
					new Thread(snt, "send prepare order mb msg thread").start();
				}
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
	@AuthPassport
	@RequestMapping(value = "/vdistribute/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vdistribute(@PathVariable("id") int id, @RequestBody JSONObject paramObject,@RequestParam(value="token", required=false) String token) {
		int orderid = paramObject.getIntValue("orderid");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			OrderOn res = this.orderOnService.distributeOrder(vendor.getCityId(),orderid);
			if(res!=null){
				//由于之前设计的order里只存了userid，没有openid，
				//为了能给用户发送模板消息，这里要多一步获取其openid操作
				User u = this.userService.getById(res.getUserId());
				if(u!=null){
					//推送马上配送的的模板消息
		    		SendMSGThread snt = new SendMSGThread(u.getOpenid(),res,2);
					new Thread(snt, "send sending order mb msg thread").start();
				}
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
	@AuthPassport
	@RequestMapping(value = "/vfinish/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vfinish(@PathVariable("id") int id, @RequestBody JSONObject paramObject,@RequestParam(value="token", required=false) String token) {
		int orderid = paramObject.getIntValue("orderid");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			OrderOn res = this.orderOnService.finishOrder(vendor.getCityId(),orderid);
			if(res!=null){
				//由于之前设计的order里只存了userid，没有openid，
				//为了能给用户发送模板消息，这里要多一步获取其openid操作
				User u = this.userService.getById(res.getUserId());
				//推送配送完成的模板消息
	    		SendMSGThread snt = new SendMSGThread(u.getOpenid(),res,3);
				new Thread(snt, "send order finished mb msg thread").start();
				jsonObject.put("msg", "200");
				logger.info("完成订单配送成功orderid: "+orderid);
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
	 * @return
	 * 从数据库中找出需要将状态从排队中改为待处理的订单，将其状态设为待处理
    	当配送时间起点-准备时间<=当前时间时的订单满足条件，
    	请求路径中的参数id是机器人id的验证,加了AuthInterceptor的话可以去掉
	 */
//	@AuthPassport
	@RequestMapping(value = "/vapproach/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object vapproach(@PathVariable("id") int id) {
		JSONObject jsonObject = new JSONObject();
		List<OrderOn> processed_orders = new ArrayList<OrderOn>();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			//获取所有城市id
			List<City> cities = this.cityService.queryAll();
			for(City c : cities){
				//获取所有满足条件的订单并设置对应的状态
				List<OrderOn> temp = this.orderOnService.approachOrders(c.getId());
				if(temp!=null)//注意addAll方法必须先判断是否为null，否则加入null元素时会报NPE
					processed_orders.addAll(temp);
			}
			if(processed_orders!=null&&processed_orders.size()>0){
				BatchSendMSGThread snt = new BatchSendMSGThread(processed_orders, vendorService);
				new Thread(snt, "batch send order preprare mb msg thread").start();
			}
			jsonObject.put("msg", "200");
			jsonObject.put("orders", processed_orders);
			return jsonObject;
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
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 总后台查询所有有效订单，分页
	 */
	@RequestMapping(value = "/s_queryall/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody ModelAndView query_All_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_orderOn");  
		if(token==null){
			return modelAndView;
		}
		Supervisor supervisor = this.supervisorService.getById(id);
		List<City> cites = this.cityService.queryAll();
		List<OrderOn> orderOns = new LinkedList<OrderOn>();
		int pageCount = 0;
		int recordCount = 0;
		if(supervisor!=null){
			for(City c : cites){
				List<OrderOn> os = this.orderOnService.queryAll("Order_"+c.getId()+"_on");
				if(os!=null){
					orderOns.addAll(os);
					int rc = this.orderOnService.getRecordCount(c.getId());
					recordCount += rc;
				}
			}
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			int from=(showPage-1)*MakaoConstants.PAGE_SIZE;
			int to=(orderOns.size()-from>=MakaoConstants.PAGE_SIZE)?(from+MakaoConstants.PAGE_SIZE-1):(orderOns.size()-1);
			orderOns = orderOns.subList(from, to+1);
		}
		logger.info("查询所有有效订单信息完成");
		modelAndView.addObject("id", id);  
		modelAndView.addObject("token", token); 
	    modelAndView.addObject("ordersOn", orderOns);   
	    modelAndView.addObject("pageCount", pageCount);
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
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 增加分页
	 */
	@RequestMapping(value = "/v_query_queue/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView v_query_queue_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOn_queue");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOn> orders = null;
		int pageCount = 0;
		if(vendor!=null){
			orders = this.orderOnService.queryQueueByAreaId("Order_"+vendor.getCityId()+"_on",vendor.getAreaId());
			int recordCount = this.orderOnService.getQueueRecordCount(vendor.getCityId(),vendor.getAreaId());
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			int from=(showPage-1)*MakaoConstants.PAGE_SIZE;
			int to=(orders.size()-from>=MakaoConstants.PAGE_SIZE)?(from+MakaoConstants.PAGE_SIZE-1):(orders.size()-1);
			orders = orders.subList(from, to+1);
		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders);   
	    modelAndView.addObject("pageCount", pageCount);   
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
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 获取待处理和待退货的订单的数量
	 */
	@RequestMapping(value = "/hasNew/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView hasNew(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_header");  
		if(token==null){
			return modelAndView;
		}
		Vendor vendor = this.vendorService.getById(id);
		int n = 0;
		if(vendor!=null)
			n = this.orderOnService.queryProcessAndReturnByAreaId(vendor.getCityId(),vendor.getAreaId());
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("onumber", n);   
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 增加了分页
	 */
	@RequestMapping(value = "/v_query_process/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView v_query_process_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOn_process");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOn> orders = null;
		int pageCount = 0;
		if(vendor!=null){
			orders = this.orderOnService.queryProcessByAreaId("Order_"+vendor.getCityId()+"_on",vendor.getAreaId());
			int recordCount = this.orderOnService.getProcessRecordCount(vendor.getCityId(),vendor.getAreaId());
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			int from=(showPage-1)*MakaoConstants.PAGE_SIZE;
			int to=(orders.size()-from>=MakaoConstants.PAGE_SIZE)?(from+MakaoConstants.PAGE_SIZE-1):(orders.size()-1);
			orders = orders.subList(from, to+1);
		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders);   
	    modelAndView.addObject("pageCount", pageCount);   
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
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 增加分页
	 */
	@RequestMapping(value = "/v_query_distributed/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView v_query_distributed_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOn_distributed");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOn> orders = null;
		int pageCount = 0;
		if(vendor!=null){
			orders = this.orderOnService.queryDistributedByAreaId("Order_"+vendor.getCityId()+"_on",vendor.getAreaId());
			int recordCount = this.orderOnService.getDistributedRecordCount(vendor.getCityId(),vendor.getAreaId());
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			int from=(showPage-1)*MakaoConstants.PAGE_SIZE;
			int to=(orders.size()-from>=MakaoConstants.PAGE_SIZE)?(from+MakaoConstants.PAGE_SIZE-1):(orders.size()-1);
			orders = orders.subList(from, to+1);
		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orders", orders);   
	    modelAndView.addObject("pageCount", pageCount);   
		return modelAndView;
    }
	
	/**
	 * @param orderOn
	 * @return
	 * 将前端传来的orderOn转换成微信detail需要的json格式
	 */
	private String orderToJson(OrderOn orderOn){
		String productIds = orderOn.getProductIds();
		String productNames = orderOn.getProductNames();
		String[] ids = productIds.split(",");
		String[] names = productNames.split(",");
		StringBuilder sb = new StringBuilder();
		sb.append("{\"goods_detail\":[");
		for(int i=0; i<ids.length; i++){
			String[] nameDetail = names[i].split("=");
			int price = Integer.parseInt(nameDetail[1].split("\\.")[0]+nameDetail[1].split("\\.")[1]);
			String po = "{\"goods_id\":\""+ids[i]+"\",\"goods_name\":\""+nameDetail[0]+"\",\"quantity\":"+nameDetail[2]+",\"price\":"+price+"}";
			sb.append(po);
		}
		sb.append("]}");
		return sb.toString();
	}
	
	private class SmallProduct{
		private String id;
		private String name;
		private String image;
		private String price;
		private String number;
		private int cityId;
		private int areaId;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public int getCityId() {
			return cityId;
		}
		public void setCityId(int cityId) {
			this.cityId = cityId;
		}
		public int getAreaId() {
			return areaId;
		}
		public void setAreaId(int areaId) {
			this.areaId = areaId;
		}
	}

}
