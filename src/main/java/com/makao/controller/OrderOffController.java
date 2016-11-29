package com.makao.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.makao.entity.Comment;
import com.makao.entity.OrderOff;
import com.makao.entity.OrderOn;
import com.makao.entity.Product;
import com.makao.entity.Supervisor;
import com.makao.entity.Vendor;
import com.makao.service.ICityService;
import com.makao.service.ICommentService;
import com.makao.service.IOrderOffService;
import com.makao.service.IProductService;
import com.makao.service.ISupervisorService;
import com.makao.service.IVendorService;
import com.makao.thread.SendMSGThread;
import com.makao.utils.MakaoConstants;
import com.makao.utils.OrderNumberUtils;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月6日
 */
//@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/orderOff")
public class OrderOffController {
	private static final Logger logger = Logger.getLogger(OrderOffController.class);
	private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private final DecimalFormat fnum = new  DecimalFormat("##0.00"); //保留两位小数  
	@Resource
	private IOrderOffService orderOffService;
	@Resource
	private IVendorService vendorService;
	@Resource
	private ISupervisorService supervisorService;
	@Resource
	private ICityService cityService;
	@Resource
	private IProductService productService;
	@Resource
	private ICommentService commentService;
	
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
	
	@RequestMapping(value = "/all/{cityid:\\d+}/{userid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody Object all(@PathVariable("cityid") int cityid, @PathVariable("userid") int userid) {
		JSONObject jsonObject = new JSONObject();
		List<OrderOff> os = this.orderOffService.queryByUserId("Order_"+cityid+"_off", userid);
		logger.info("查询用户id："+userid+"的所有失效订单信息完成(所属city:"+cityid+")");
		jsonObject.put("msg", "200");
		jsonObject.put("orders", os);
		return jsonObject;
    }
	
	@RequestMapping(value = "/{cityid:\\d+}/{orderid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody Object get(@PathVariable("cityid") int cityid, @PathVariable("orderid") int orderid) {
		JSONObject jsonObject = new JSONObject();
		OrderOff os = this.orderOffService.queryByOrderId("Order_"+cityid+"_off", orderid);
		List<SmallProduct> sps = new ArrayList<SmallProduct>();
		if(os!=null){
			//重新组装商品列表，方便前端直接显示
			String[] pro_ids = os.getProductIds().split(",");
			String[] pro_names = os.getProductNames().split(",");
			String pcomments = os.getPcomments();
			String[] coms = null;
			if(pcomments!=null&&!"".equals(pcomments))
			{	
				coms = pcomments.split(",");
			}
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
				if(coms!=null&&coms.length>0)
				{
					for(String c : coms){
						if(c!=null&&!"".equals(c)){
							String proid = c.split("=")[0];
							if(pid.equals(proid)){
								String comid = c.split("=")[1];
								Comment com = this.commentService.getByCityAreaComentId(os.getCityId(),os.getAreaId(),Integer.valueOf(comid));
								sp.setComment(com.getContent());
							}
						}
					}
				}
				sps.add(sp);
			}
			
		}
		logger.info("查询失效订单id："+orderid+" 信息完成(所属city:"+cityid+")");
		jsonObject.put("msg", "200");
		jsonObject.put("order", os);
		jsonObject.put("products", sps);
		return jsonObject;
    }
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 用户发起退货请求，将订单状态设置为退货申请中
	 */
	@AuthPassport
	@RequestMapping(value = "/return/{cityid:\\d+}/{orderid:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    Object returnOrder(@PathVariable("cityid") int cityid, @PathVariable("orderid") int orderid,
    		@RequestParam(value="token", required=false) String token) {
		JSONObject jsonObject = new JSONObject();
		OrderOff res = this.orderOffService.returnOrder(cityid, orderid);
		if(res!=null){
			logger.info("发起退货申请成功，订单id=" + orderid + " 所属城市id:"+cityid);
        	jsonObject.put("msg", "200");
        	// 通知vendor
        	List<Vendor> vs = this.vendorService.getByAreaId(res.getAreaId());
        	for(Vendor v : vs){
        		String openid = v.getOpenid();
        		if(openid!=null&&!"".equals(openid)){
        			SendMSGThread snt = new SendMSGThread(openid,res,6);
        			new Thread(snt, "send order created mb msg thread").start();
        		}
        	}
		}
		else{
			logger.info("发起退货申请失败，订单id=" + orderid + " 所属城市id:"+cityid);
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
	
//	@RequestMapping(value = "/queryall", method = RequestMethod.GET)
//    public @ResponseBody
//    Object queryAll() {
//		List<OrderOff> OrderOffs = null;
//		//则查询返回所有
//		OrderOffs = this.orderOffService.queryAll();
//		logger.info("查询所有失效订单信息完成");
//        return OrderOffs;
//    }
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 处理退货订单，将其状态设置为退货中
	 */
	@AuthPassport
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
	@AuthPassport
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
	@AuthPassport
	@RequestMapping(value = "/vfinish/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object vfinish(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		int orderid = paramObject.getIntValue("orderid");
		JSONObject jsonObject = new JSONObject();
		Vendor vendor = this.vendorService.getById(id);
		if(vendor!=null){
			OrderOff res = this.orderOffService.finishReturnOrder(vendor.getCityId(),orderid);
			if(res!=null){
				//给超级管理员推送完成退货的消息
				List<Supervisor> ss = this.supervisorService.queryAll();
				for(Supervisor s : ss){
					String openid = s.getOpenid();
					if(openid!=null&&!"".equals(openid)){
						SendMSGThread snt = new SendMSGThread(openid,res,5);
						new Thread(snt, "send order need refund mb msg thread").start();
					}
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
	
	@RequestMapping(value = "/s_querydealed/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody ModelAndView query_Dealed(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_statistic_dealed");  
		List<City> cities = this.cityService.queryAll();
		modelAndView.addObject("id", id);  
		modelAndView.addObject("token", token); 
	    modelAndView.addObject("cities", cities);   
	    return modelAndView;
    }
	
	@RequestMapping(value = "/s_queryreturned/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody ModelAndView query_Returned(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_statistic_returned");  
		List<City> cities = this.cityService.queryAll();
		modelAndView.addObject("id", id);  
		modelAndView.addObject("token", token); 
	    modelAndView.addObject("cities", cities);   
	    return modelAndView;
    }
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 查找对应区域下在特定时间范围内的已完成、和已取消退货的所有订单
	 */
	@RequestMapping(value = "/querydealed/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody Object queryDealed(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		JSONObject jsonObject = new JSONObject();
		int cityid = paramObject.getIntValue("cityid");
		int areaid = paramObject.getIntValue("areaid");
		String fromdate = paramObject.getString("fromdate")+" 00:00:00";
		String todate = paramObject.getString("todate")+" 23:59:59";
		List<OrderOff> ods = this.orderOffService.queryDealed("Order_"+cityid+"_off", areaid, fromdate, todate);
		
		float totalT = 0.00f, totalB = 0.00f;
		int i = 0;
		for (; i < ods.size(); i++) {
			OrderOff o = ods.get(i);
			float totalPr = Float.valueOf(o.getTotalPrice());
			float bid = 0.00f;
			String[] ids = o.getProductIds().split(",");
			String[] nums = o.getProductNames().split(",");
			for(int ii=0; ii<ids.length ; ii++){
				Product p = this.productService.getById(Integer.valueOf(ids[ii]), cityid, areaid);
				bid = bid + Float.valueOf(p.getBid())*Float.valueOf(nums[ii].split("=")[2]);
			}
			totalT = totalT+totalPr;
			totalB = totalB+(totalPr-bid);
		}
		
		logger.info("查询"+cityid+"_"+areaid+"下，时间 "+fromdate+"_"+todate+" 的订单信息完成");
		jsonObject.put("msg", "200");
		jsonObject.put("orders", ods);
		jsonObject.put("totalT", fnum.format(totalT));
		jsonObject.put("totalB", fnum.format(totalB));
		return jsonObject;
    }
	
	/**
	 * @param id
	 * @param paramObject
	 * @return
	 * 查找对应区域下在特定时间范围内的已退款的所有订单,注意是已退款不是已退货
	 */
	@RequestMapping(value = "/queryreturned/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody Object queryReturn(@PathVariable("id") int id, @RequestBody JSONObject paramObject) {
		JSONObject jsonObject = new JSONObject();
		int cityid = paramObject.getIntValue("cityid");
		int areaid = paramObject.getIntValue("areaid");
		String fromdate = paramObject.getString("fromdate")+" 00:00:00";
		String todate = paramObject.getString("todate")+" 23:59:59";
		List<OrderOff> ods = this.orderOffService.queryReturned("Order_"+cityid+"_off", areaid, fromdate, todate);
		
		float totalT = 0.00f;
		int i = 0;
		for (; i < ods.size(); i++) {
			OrderOff o = ods.get(i);
			float totalPr = Float.valueOf(o.getTotalPrice());
			totalT = totalT+totalPr;
		}
		
		logger.info("查询"+cityid+"_"+areaid+"下，时间 "+fromdate+"_"+todate+" 的订单信息完成");
		jsonObject.put("msg", "200");
		jsonObject.put("orders", ods);
		jsonObject.put("totalT", fnum.format(totalT));
		return jsonObject;
    }
	
	@RequestMapping(value = "/export/{id:\\d+}", method = RequestMethod.POST)
    public void export(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) {
		int cityid = Integer.parseInt(request.getParameter("cid"));
		int areaid = Integer.parseInt(request.getParameter("aid"));
		String fromdate =request.getParameter("fromd")+" 00:00:00";
		String todate = request.getParameter("tod")+" 23:59:59";
		List<OrderOff> ods = this.orderOffService.queryDealed("Order_"+cityid+"_off", areaid, fromdate, todate);
		if(ods!=null&&ods.size()>0){
			String exportsFolder = request.getServletContext().getRealPath("/")+"WEB-INF/static/exports/";
			//String filePath = "/Users/makao/Desktop/exports_"+ System.currentTimeMillis() + ".xlsx";
			String filePath = exportsFolder+"orders_"+System.currentTimeMillis()+".xlsx";
			try {
				// 输出流
				OutputStream os = new FileOutputStream(filePath);
				// 工作区
				XSSFWorkbook wb = new XSSFWorkbook();
				XSSFSheet sheet = wb.createSheet("test");
				XSSFRow row = sheet.createRow(0);
				row.createCell(0).setCellValue("订单号");
				row.createCell(1).setCellValue("总金额");
				row.createCell(2).setCellValue("优惠券抵扣");
				row.createCell(3).setCellValue("收货人");
				row.createCell(4).setCellValue("联系电话");
				row.createCell(5).setCellValue("下单时间");
				row.createCell(6).setCellValue("完成时间");
				row.createCell(7).setCellValue("订单状态");
				row.createCell(8).setCellValue("去成本金额");
				float totalT = 0.00f, totalB = 0.00f;
				int i = 0;
				for (; i < ods.size(); i++) {
					OrderOff o = ods.get(i);
					XSSFRow r = sheet.createRow(i+1);
					float totalPr = Float.valueOf(o.getTotalPrice());
					r.createCell(0).setCellValue(o.getNumber());
					r.createCell(1).setCellValue(o.getTotalPrice());
					r.createCell(2).setCellValue("-"+o.getCouponPrice());
					r.createCell(3).setCellValue(o.getReceiverName());
					r.createCell(4).setCellValue(o.getPhoneNumber());
					r.createCell(5).setCellValue(df.format(o.getOrderTime()));
					r.createCell(6).setCellValue(df.format(o.getFinalTime()));
					r.createCell(7).setCellValue("13".equals(o.getFinalStatus())?"已完成":"已取消退货");
					float bid = 0.00f;
					String[] ids = o.getProductIds().split(",");
					String[] nums = o.getProductNames().split(",");
					for(int ii=0; ii<ids.length ; ii++){
						Product p = this.productService.getById(Integer.valueOf(ids[ii]), cityid, areaid);
						bid = bid + Float.valueOf(p.getBid())*Float.valueOf(nums[ii].split("=")[2]);
					}
					r.createCell(8).setCellValue(fnum.format(totalPr-bid));
					totalT = totalT+totalPr;
					totalB = totalB+(totalPr-bid);
				}
				XSSFRow blank = sheet.createRow(i+1);
				XSSFRow r = sheet.createRow(i+2);
				r.createCell(1).setCellValue("全部累积金额:");
				r.createCell(2).setCellValue(fnum.format(totalT));
				r.createCell(3).setCellValue("去成本累积金额:");
				r.createCell(4).setCellValue(fnum.format(totalB));
				// 写文件
				wb.write(os);
				os.flush();
				// 关闭输出流
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			download(filePath, response);
		}
    }
	
	@RequestMapping(value = "/export2/{id:\\d+}", method = RequestMethod.POST)
    public void export2(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) {
		int cityid = Integer.parseInt(request.getParameter("cid"));
		int areaid = Integer.parseInt(request.getParameter("aid"));
		String fromdate =request.getParameter("fromd")+" 00:00:00";
		String todate = request.getParameter("tod")+" 23:59:59";
		List<OrderOff> ods = this.orderOffService.queryReturned("Order_"+cityid+"_off", areaid, fromdate, todate);
		if(ods!=null&&ods.size()>0){
			String exportsFolder = request.getServletContext().getRealPath("/")+"WEB-INF/static/exports/";
			//String filePath = "/Users/makao/Desktop/exports_"+ System.currentTimeMillis() + ".xlsx";
			String filePath = exportsFolder+"orders_"+System.currentTimeMillis()+".xlsx";
			try {
				// 输出流
				OutputStream os = new FileOutputStream(filePath);
				// 工作区
				XSSFWorkbook wb = new XSSFWorkbook();
				XSSFSheet sheet = wb.createSheet("test");
				XSSFRow row = sheet.createRow(0);
				row.createCell(0).setCellValue("订单号");
				row.createCell(1).setCellValue("总金额");
				row.createCell(2).setCellValue("优惠券抵扣");
				row.createCell(3).setCellValue("收货人");
				row.createCell(4).setCellValue("联系电话");
				row.createCell(5).setCellValue("下单时间");
				row.createCell(6).setCellValue("完成时间");
				row.createCell(7).setCellValue("订单状态");
				float totalT = 0.00f;
				int i = 0;
				for (; i < ods.size(); i++) {
					OrderOff o = ods.get(i);
					XSSFRow r = sheet.createRow(i+1);
					float totalPr = Float.valueOf(o.getTotalPrice());
					r.createCell(0).setCellValue(o.getNumber());
					r.createCell(1).setCellValue(o.getTotalPrice());
					r.createCell(2).setCellValue("-"+o.getCouponPrice());
					r.createCell(3).setCellValue(o.getReceiverName());
					r.createCell(4).setCellValue(o.getPhoneNumber());
					r.createCell(5).setCellValue(df.format(o.getOrderTime()));
					r.createCell(6).setCellValue(df.format(o.getFinalTime()));
					r.createCell(7).setCellValue("已退款");
					totalT = totalT+totalPr;
				}
				XSSFRow blank = sheet.createRow(i+1);
				XSSFRow r = sheet.createRow(i+2);
				r.createCell(1).setCellValue("全部退货金额累计:");
				r.createCell(2).setCellValue(fnum.format(totalT));
				// 写文件
				wb.write(os);
				os.flush();
				// 关闭输出流
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			download(filePath, response);
		}
    }
	
	private void download(String path, HttpServletResponse response) {
		try {
			// path是指欲下载的文件的路径。
			File file = new File(path);
			// 取得文件名。
			String filename = file.getName();
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(
					response.getOutputStream());
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 总后台返回所有已完成的订单，分页
	 */
	@RequestMapping(value = "/s_queryall/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody ModelAndView query_All_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
		ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_orderOff");  
		if(token==null){
			return modelAndView;
		}
		Supervisor supervisor = this.supervisorService.getById(id);
		List<City> cites = this.cityService.queryAll();
		List<OrderOff> orderOffs = new LinkedList<OrderOff>();
		int pageCount = 0;
		int recordCount = 0;
		if(supervisor!=null){
			for(City c : cites){
				List<OrderOff> os = this.orderOffService.queryAll("Order_"+c.getId()+"_off");
				if(os!=null){
					orderOffs.addAll(os);
					int rc = this.orderOffService.getRecordCount(c.getId());
					recordCount += rc;
				}
			}
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			int from=(showPage-1)*MakaoConstants.PAGE_SIZE;
			int to=(orderOffs.size()-from>=MakaoConstants.PAGE_SIZE)?(from+MakaoConstants.PAGE_SIZE-1):(orderOffs.size()-1);
			orderOffs = orderOffs.subList(from, to+1);
		}
		logger.info("查询所有已完成的订单信息完成");
		modelAndView.addObject("id", id);  
		modelAndView.addObject("token", token); 
	    modelAndView.addObject("ordersOff", orderOffs);   
	    modelAndView.addObject("pageCount", pageCount);
	    return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询所有已收货的订单
	 */
	@RequestMapping(value = "/v_query_confirm/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView areaQuery(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_confirm");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		if(vendor!=null)
			orders = this.orderOffService.queryConfirmGetByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
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
	@RequestMapping(value = "/v_query_confirm/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView areaQuery_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_confirm");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		int pageCount = 0;
		if(vendor!=null){
			orders = this.orderOffService.queryConfirmGetByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
			int recordCount = this.orderOffService.getConfirmRecordCount(vendor.getCityId(),vendor.getAreaId());
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
	 * @param id
	 * @param token
	 * @param showPage
	 * @return
	 * 已完成的订单，即状态为13，已收货且已过了退货期的订单
	 */
	@RequestMapping(value = "/v_query_teminaled/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView terminalQuery_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_terminaled");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		int pageCount = 0;
		if(vendor!=null){
			orders = this.orderOffService.queryTerminaledByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
			int recordCount = this.orderOffService.getTermialedRecordCount(vendor.getCityId(),vendor.getAreaId());
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
	 * 查询所有已取消，已退货和已取消退货的订单，增加了分页
	 */
	@RequestMapping(value = "/v_query_cancel/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView vquerycancel_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_cancel");  
		if(token==null){
			return modelAndView;
		}
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		int pageCount = 0;
		if(vendor!=null){
			orders = this.orderOffService.queryCancelByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
			int recordCount = this.orderOffService.getCancelRecordCount(vendor.getCityId(),vendor.getAreaId());
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
	 * @param id
	 * @param token
	 * @return
	 * 查询退货中、退货申请中的订单
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
	 * 增加分页
	 */
	@RequestMapping(value = "/v_query_refund/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView vqueryrefund_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("v_orderOff_refund");  
		if(token==null){
			return modelAndView;
		}
		
		Vendor vendor = this.vendorService.getById(id);
		List<OrderOff> orders = null;
		int pageCount = 0;
		if(vendor!=null){
			orders = this.orderOffService.queryRefundByAreaId("Order_"+vendor.getCityId()+"_off",vendor.getAreaId());
			int recordCount = this.orderOffService.getReturnRecordCount(vendor.getCityId(),vendor.getAreaId());
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
	 * @param id
	 * @param token
	 * @return
	 * 查询所有已取消(卖家取消)的和已退货的订单，在退款订单里显示，因为这些是需要退款的
	 * 另外订单的退款状态也不能是'已退款'和'无需退款'
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
	    modelAndView.addObject("orderOff", orderOffs);     
		return modelAndView;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询待退款的订单的number
	 */
	@RequestMapping(value = "/hasNew/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView hasNew(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_header");  
		if(token==null){
			return modelAndView;
		}
		Supervisor supervisor = this.supervisorService.getById(id);
		List<City> cites = this.cityService.queryAll();
		int n = 0;
		if(supervisor!=null){
			for(City c : cites){
				int m = this.orderOffService.queryNeedRefundNumber("Order_"+c.getId()+"_off");
				n=n+m;
			}
		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("onumber", n);   
		return modelAndView;
    }
	
	@RequestMapping(value = "/hasNew/{id:\\d+}", method = RequestMethod.POST)
    public @ResponseBody
    Object hasNew2(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token) {
	    JSONObject json = new JSONObject();
		if(token==null){
			json.put("onumber", 0);
			return json;
		}
		Supervisor supervisor = this.supervisorService.getById(id);
		List<City> cites = this.cityService.queryAll();
		int n = 0;
		if(supervisor!=null){
			for(City c : cites){
				int m = this.orderOffService.queryNeedRefundNumber("Order_"+c.getId()+"_off");
				n=n+m;
			}
		}
		json.put("onumber", n);
		return json;
    }
	
	/**
	 * @param id
	 * @param token
	 * @return
	 * 查询所有已取消(卖家取消)的和已退货的订单，在退款订单里显示，因为这些是需要退款的，增加了分页
	 */
	@RequestMapping(value = "/s_query_refund/{id:\\d+}/{showPage:\\d+}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView squeryrefund_paging(@PathVariable("id") int id, @RequestParam(value="token", required=false) String token
    		,@PathVariable("showPage") int showPage) {
	    ModelAndView modelAndView = new ModelAndView();  
		modelAndView.setViewName("s_orderOff_refund");  
		if(token==null){
			return modelAndView;
		}
		Supervisor supervisor = this.supervisorService.getById(id);
		List<City> cites = this.cityService.queryAll();
		List<OrderOff> orderOffs = new LinkedList<OrderOff>();
		int pageCount = 0;
		int recordCount = 0;
		if(supervisor!=null){
			for(City c : cites){
				List<OrderOff> os = this.orderOffService.queryAllCanceledAndReturned("Order_"+c.getId()+"_off");
				if(os!=null){
					orderOffs.addAll(os);
					int rc = this.orderOffService.getCanceledAndReturnedRecordCount(c.getId());
					recordCount += rc;
				}
			}
			pageCount = (recordCount%MakaoConstants.PAGE_SIZE==0)?(recordCount/MakaoConstants.PAGE_SIZE):(recordCount/MakaoConstants.PAGE_SIZE+1);
			//如果要显示第showPage页，那么游标应该移动到的position的值是：
			int from=(showPage-1)*MakaoConstants.PAGE_SIZE;
			int to=(orderOffs.size()-from>=MakaoConstants.PAGE_SIZE)?(from+MakaoConstants.PAGE_SIZE-1):(orderOffs.size()-1);
			orderOffs = orderOffs.subList(from, to+1);
		}
	    modelAndView.addObject("id", id);  
	    modelAndView.addObject("token", token); 
	    modelAndView.addObject("orderOff", orderOffs);  
	    modelAndView.addObject("pageCount", pageCount);
		return modelAndView;
    }
	
	private class SmallProduct{
		private String id;
		private String name;
		private String image;
		private String price;
		private String number;
		private int cityId;
		private int areaId;
		private String comment;
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
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
	}
	
}
