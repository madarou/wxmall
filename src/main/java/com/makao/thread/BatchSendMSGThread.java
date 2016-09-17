package com.makao.thread;

import java.util.List;

import org.apache.log4j.Logger;

import com.makao.entity.OrderOn;
import com.makao.entity.Vendor;
import com.makao.service.IVendorService;
import com.makao.weixin.utils.WeixinConstants;

/**
 * @author makao
 * 自动检查到有多个要待处理的订单时，根据这些订单的areaid找到所有对应的vendor，
 * 向每一个绑定了微信的vendor推送准备订单的模板消息
 *
 */
public class BatchSendMSGThread implements Runnable{
	private static final Logger logger = Logger.getLogger(BatchSendMSGThread.class);
	private String fromUserName;
	private List<OrderOn> orders;
	private IVendorService vendorService;

	public BatchSendMSGThread(String fromUserName, List<OrderOn> orders, IVendorService vendorService){
		this.fromUserName = fromUserName;
		this.orders = orders;
		this.vendorService = vendorService;
	}
	
	public BatchSendMSGThread(List<OrderOn> orders, IVendorService vendorService){
		this.fromUserName = WeixinConstants.MSG_FROM_USERNAME;
		this.orders = orders;
		this.vendorService = vendorService;
	}
	
	private void batchSendMSG(){
		//遍历orders
		for(OrderOn order : this.orders){
			int areaId = order.getAreaId();
			List<Vendor> vendors = vendorService.getByAreaId(areaId);
			for(Vendor v : vendors){
				String vendor_openid = v.getOpenid();
				if(vendor_openid!=null&&!"".equals(vendor_openid)){
					SendMSGThread snt = new SendMSGThread(vendor_openid,order,4);
					new Thread(snt, "send prepare order mb msg thread").start();
				}
			}
		}
	}
	@Override
	public void run() {
		batchSendMSG();
	}

}
