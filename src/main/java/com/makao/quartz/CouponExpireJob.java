package com.makao.quartz;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.makao.entity.City;
import com.makao.entity.CouponOn;
import com.makao.entity.OrderOn;
import com.makao.service.ICityService;
import com.makao.service.ICouponOnService;
import com.makao.service.IOrderOnService;
import com.makao.service.IVendorService;
import com.makao.thread.BatchSendMSGThread;

/**
 * @author makao
 * 每天晚上12点执行，将couponOn中to小于等于当前日期的couponOn移到couponOff中
 */
public class CouponExpireJob {
	@Resource
	private ICouponOnService couponOnService;
	@Resource
	private ICityService cityService;
	@Resource
	private IVendorService vendorService;
	public void execute(){
		//List<CouponOn> processed_coupons = new ArrayList<CouponOn>();
		//获取所有城市id
		List<City> cities = this.cityService.queryAll();
		for(City c : cities){
			List<CouponOn> temp = this.couponOnService.expireCoupons(c.getId());
			//if(temp!=null)//注意addAll方法必须先判断是否为null，否则加入null元素时会报NPE
				//processed_coupons.addAll(temp);
		}
	}

}
