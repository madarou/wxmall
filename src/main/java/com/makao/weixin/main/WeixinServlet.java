package com.makao.weixin.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.makao.entity.Vendor;
import com.makao.service.IVendorService;
import com.makao.service.impl.VendorServiceImpl;
import com.makao.weixin.po.Image;
import com.makao.weixin.po.Music;
import com.makao.weixin.po.News;
import com.makao.weixin.po.TextMessage;
import com.makao.weixin.utils.CheckUtil;
import com.makao.weixin.utils.MessageUtil;
import com.makao.weixin.utils.QRCodeUtil;

/**
 * @description: TODO
 * @author makao
 * @date 2016年6月6日
 */
@WebServlet(name = "WeixinServlet", urlPatterns = { "/WeixinServlet2" }, loadOnStartup = 1)
public class WeixinServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(WeixinServlet.class);
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * 微信平台接入时验证的，之后就没用了
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		PrintWriter out = response.getWriter();
		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * 处理微信平台传来的用户请求的主入口
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		try {
			Map<String, String> map = MessageUtil.xmlToMap(request);
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String createTime = map.get("CreateTime");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			System.out.println(content);
			String msgId = map.get("MsgId");
			logger.info("message fromUserName: "+ fromUserName);//其实就是发送者的微信openid
			logger.info("message toUserName: "+ toUserName);//其实就是公众号的微信号
			
			String message = null;
			//如果用户发送过来的是文本消息，处理文本消息
			if (MessageUtil.MESSAGE_TEXT.equals(msgType)) {
				//创建关键字回复
				if("你好".equals(content) || "hello".equals(content)){
					message = MessageUtil.textMessageToXml(toUserName, fromUserName, "你好");
				}else if("图文".equals(content)){//创建图文消息回复
					News news = new News();
					news.setTitle("鱼圈圈水族");
					news.setDescription("成都鱼圈圈水族馆");
					news.setPicUrl("http://madarou1.ngrok.cc/weixin/img/yqq.jpeg");
					news.setUrl("www.yuqq.cc");
					List<News> news_items = new ArrayList<News>();//可以创建多个图文消息，都放到list中就行
					news_items.add(news);
					
					message = MessageUtil.newsMessageToXml(toUserName, fromUserName, news_items);
				}else if("图片".equals(content)){//创建图片消息回复
					Image image = new Image();
					//这里的media_id是从com.makao.weixin.test.WeixinTest中请求上传url后返回获取到的临时图片media_id，
					//只能保留三天有效，这里是测试用GxK1O6cUp2udZmIg8nY9Kt0AUFof3D99-KHAPf1qp8tNcyFKRUHViRBwjlsUvzSc
					image.setMediaId("qtflXFXBnmImScE5EoHs5SZgBU_SomZ3WgZtXYMO5mUdW4vU8BgTL-lt8jBL6dn9");
					message = MessageUtil.imageMessageToXml(toUserName, fromUserName, image);
				}else if("音乐".equals(content)){//创建音乐消息回复
					Music music = new Music();
					music.setTitle("JAY");
					music.setDescription("JAY钢琴串烧");
					music.setMusicUrl("http://madarou1.ngrok.cc/weixin/resource/JAY.mp3");
					music.setHQMusicUrl("http://madarou1.ngrok.cc/weixin/resource/JAY.mp3");
					music.setThumbMediaId("Km8RvxqCPK2vHHQ69lb8aOeAMZ4G67ZcgR1QbJi3TenS_xR9ZPNhWizHbpU_UcmT");
					message = MessageUtil.musicMessageToXml(toUserName, fromUserName, music);
				}else{//创建默认回复
					TextMessage text = new TextMessage();
					text.setFromUserName(toUserName);
					text.setToUserName(fromUserName);
					text.setMsgType(MessageUtil.MESSAGE_TEXT);
					text.setContent(content);
					text.setCreateTime(new Date().toLocaleString());
	
					message = MessageUtil.textMessageToXml(text);
				}
			}else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){//如果用户发送过来的是地理位置消息，而不是文本消息
				//获取用户的地理位置，并返回给他
				String location_label = map.get("Label");
				//这里客户端会收到自己地理位置信息的回复
				message = MessageUtil.textMessageToXml(toUserName, fromUserName, location_label+" "+map.get("EventKey"));
			}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){//如果用户触发了某个事件，处理事件
				String event = map.get("Event");
				logger.info("event: "+event);
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(event)){//如果是关注事件
					String eventKey = map.get("EventKey");
					logger.info("eventKey: "+eventKey);
					if(eventKey!=null&&!"".equals(eventKey)){//管理员关注
						if(eventKey.indexOf(MessageUtil.MESSAGE_VENDOR_SUBSCRIBE)>-1){
							logger.info("subcribe add vendor openid: "+eventKey);
							int vendorid = Integer.valueOf(eventKey.split("_")[1]);
							//根据fromUserName openid去Vender表里查
							IVendorService vendorService = new VendorServiceImpl();
							Vendor v = vendorService.getById(vendorid);
							v.setOpenid(fromUserName);
							vendorService.update(v);
							message = MessageUtil.textMessageToXml(toUserName, fromUserName, MessageUtil.onVendorSubscriptionAutoReply());
						}
					}
					else{
						message = MessageUtil.textMessageToXml(toUserName, fromUserName, MessageUtil.onSubscriptionAutoReply());
					}
				}else if(MessageUtil.MESSAGE_SCAN.equals(event)){//如果事件是扫码事件，则判断如果是管理员扫码，可能是管理员绑定账号
					String eventKey = map.get("EventKey");
					logger.info("eventKey: "+eventKey);
					if(eventKey!=null&&!"".equals(eventKey)){//管理员关注
						if(isNumeric(eventKey)){
							logger.info("scan add vendor openid: "+eventKey);
							int vendorid = Integer.valueOf(eventKey);
							//根据fromUserName openid去Vender表里查
							logger.info("vendorid : "+vendorid);
							IVendorService vendorService = new VendorServiceImpl();
							Vendor v = vendorService.getById(vendorid);
							logger.info("vendor: "+v.getUserName());
							v.setOpenid(fromUserName);
							logger.info("set fromUserName: "+fromUserName);
							vendorService.update(v);
							logger.info("updated:  "+v.getOpenid());
							message = MessageUtil.textMessageToXml(toUserName, fromUserName, MessageUtil.onVendorSubscriptionAutoReply());
							logger.info("message created:  "+ message);
						}else{
							message = MessageUtil.textMessageToXml(toUserName, fromUserName, MessageUtil.onSubscriptionAutoReply());
						}
					}
					else{
						message = MessageUtil.textMessageToXml(toUserName, fromUserName, MessageUtil.onSubscriptionAutoReply());
					}
				}
			}
			System.out.println(message);
			out.print(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}

	}
	
	public static boolean isNumeric(String str){
		  for (int i = 0; i < str.length(); i++){
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		  }
		  return true;
		 }
}
