package com.makao.quartz;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.makao.entity.City;
import com.makao.entity.OrderOn;
import com.makao.service.ICityService;
import com.makao.service.IOrderOnService;
import com.makao.service.IProductService;
import com.makao.thread.AddInventoryThread;
import com.makao.utils.RedisUtil;

/**
 * @author makao
 *由于用户下单成功后可能在15分钟内未支付或支付失败，需要定期：
 *1.将超过15分钟的未支付的订单里的商品的库存依次加回缓存中
 *2.将缓存中的所有商品的库存定期写入数据库中
 *3.将数据库中超过下单时间15分钟的未支付的订单删除
 */
public class InventoryOptJob {
	@Resource
	private IOrderOnService orderOnService;
	@Resource
	private IProductService productService;
	@Resource
	private ICityService cityService;
	@Autowired
	private RedisUtil redisUtil;
	public void execute(){
		List<City> cities = this.cityService.queryAll();
		for(City c:cities){
			//数据库中查到所有满足条件的订单，同时删除它们，并且返回订单列表
			List<OrderOn> orders = this.orderOnService.unPaidOrders(c.getId());
			//遍历返回的订单列表，取出其中的(城市区域商品)-(数量)
			//加入到对应pi_cityId_areaId_id缓存中
			if(orders==null)
				continue;
			for(OrderOn o:orders){
				int areaId = o.getAreaId();
				String[] ids = o.getProductIds().split(",");
				String[] names = o.getProductNames().split(",");
				for(int i=0; i<ids.length; i++){
					String productKey = "pi_"+c.getId()+"_"+areaId+"_"+ids[i];
					String productNum = names[i].split("=")[2];
					AddInventoryThread ait = new AddInventoryThread(productKey, Integer.valueOf(productNum), redisUtil);
					new Thread(ait, "add inventory thread").start();
				}
			}
			//遍历当前city的pi_cityid_*为key的所有值，将这些新的库存值写入商品数据库
			List<String> keys = redisUtil.getKeys("pi_"+c.getId()+"_*");
			for(String key : keys){
				String tableName = "Product_"+c.getId()+"_"+key.split("_")[2];
				String productid = key.split("_")[3];
				String inventN = redisUtil.redisQueryObject(key);
				int res = this.productService.updateInventory(tableName, productid, inventN);
			}
		}
	}
}
