package com.makao.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
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
import com.makao.entity.TokenModel;
import com.makao.entity.User;
import com.makao.entity.Vendor;
import com.makao.service.ICityService;
import com.makao.service.IOrderOnService;
import com.makao.service.ISupervisorService;
import com.makao.service.IVendorService;
import com.makao.utils.MakaoConstants;
import com.makao.utils.OrderNumberUtils;
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
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
    public @ResponseBody
    void payOrder(@RequestParam(value="token", required=false) String token,@RequestBody OrderOn orderOn, 
    		HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String page = "";
		
		TokenModel tm = (TokenModel) request.getAttribute("tokenmodel");
		String openid = tm.getOpenid();
		if(openid==null || "".equals(openid.trim())){
			page = "订单提交失败，没有openid："+openid;
			logger.warn(page);
			out.write(page);
			return;
		}
		//为了鲁棒性，检查价格是否为00.00格式
		Pattern pattern = Pattern.compile("^(\\d+){2}.(\\d+){2}$");
		Matcher m = pattern.matcher(orderOn.getTotalPrice());
		if(!m.matches()){
			page = "订单提交失败，价格格式错误";
			logger.warn(page);
			out.write(page);
			return;
		}
		orderOn.setNumber(OrderNumberUtils.generateOrderNumber());
		orderOn.setOrderTime(new Timestamp(System.currentTimeMillis()));
		orderOn.setPayType("微信安全支付");//现在只有这种支付方式
		orderOn.setReceiveType("送货上门");//现在只有这种收货方式
		orderOn.setStatus("未支付");
		// 生成微信订单
		Unifiedorder u = new Unifiedorder();
		u.setAppid(WeixinConstants.APPID);
		u.setMch_id(WeixinConstants.MCHID);
		u.setNonce_str(SignatureUtil.getNonceStr());
		u.setBody("超级社区商品购买订单");
		u.setOut_trade_no(orderOn.getNumber());
		String price = orderOn.getTotalPrice();
		u.setTotal_fee(Integer.parseInt(price.split(".")[0]+price.split(".")[1]));//单位为分
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
			out.write(page);
			return;
		}
		String prepay_id = returnXml.get("prepay_id");
		if (prepay_id == null || "".equals(prepay_id)) {
			page = "订单号: "+orderOn.getNumber()+"没有获取到prepay_id，参数错误下单失败!";
			logger.warn(page);
			out.write(page);
			return;
		}
		this.orderOnService.insert(orderOn);
		
		// 为前端页面能够使用JSSDK设置签名
		Map<String, String> wxConfig = JSSignatureUtil
				.getSignature(MakaoConstants.SERVER_DOMAIN+"/orderOn/pay");

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

				page = "<!DOCTYPE html>"
						+ "<html>"
						+ "<head>"
							+ "<meta charset=\"utf-8\">"
							+ "<title>订单支付</title>"
							+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=0\">"
							+ "<link rel=\"stylesheet\" href=\"static/css/weixin.css\">"
						+ "</head>"
						+"<body>"
							+ "<div class=\"wxapi_container\">"
								+"<div class=\"lbox_close wxapi_form\">"
									+ "<h3 id=\"menu-pay\">超级社区</h3>"
									+ "<span class=\"desc\">订单总价：￥"+price+"</span>"
									+ "<button class=\"btn btn_primary\" id=\"chooseWXPay\">支付订单</button>"
								+"</div>"
							+ "</div>"
						+ "</body>"
						+"<script src=\"http://res.wx.qq.com/open/js/jweixin-1.0.0.js\"></script>"
						+"<script>"
							+ "wx.config({"
								+ "debug: false,"
								+ "appId: '"+WeixinConstants.APPID+"',"
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
										+ "timestamp:"+timeStamp+","
										+ "nonceStr:'"+nonceStr+"',"
										+ "package:'"+packages+"',"
										+ "signType:'MD5',"
										+ "paySign:'"+paySign+"',"
										+ "success: function (res) {"
											+ "if(res.errMsg=='chooseWXPay:ok'){"
												+ "alert('支付成功');"
											+ "}"
											+ "else{"
												+ "alert('支付失败');"
											+ "}"
										+ "}"
									+ "});"
								+ "}"
							+ "});"
							+ "wx.error(function (res) {"
								+ "alert(res.errMsg);"
							+ "});"
						+ "</script>"
					+ "</html>";
		out.write(page);
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
		    String orderid = resultXML.get("out_trade_no");
		    String cityid = resultXML.get("attach");
		    if(orderid!=null && !"".equals(orderid)){
		    	int res = this.orderOnService.confirmMoney(cityid,orderid);//将订单的状态从未支付改为排队中
		    	if(res==0){
		    		page = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		    		out.write(page);
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
			int price = Integer.parseInt(nameDetail[1].split(".")[0]+nameDetail[1].split(".")[1]);
			String po = "{\"goods_id\":\""+ids[i]+"\",\"goods_name\":\""+nameDetail[0]+"\",\"quantity\":"+nameDetail[2]+",\"price\":"+price+"}";
			sb.append(po);
		}
		sb.append("]}");
		return sb.toString();
	}
}
