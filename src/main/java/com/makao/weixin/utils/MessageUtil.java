package com.makao.weixin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.makao.weixin.po.Image;
import com.makao.weixin.po.ImageMessage;
import com.makao.weixin.po.Music;
import com.makao.weixin.po.MusicMessage;
import com.makao.weixin.po.News;
import com.makao.weixin.po.NewsMessage;
import com.makao.weixin.po.TextMessage;
import com.thoughtworks.xstream.XStream;

/**
 * @author makao
 * @date 2016年4月16日
 */
/**
 * @author makao
 * @date 2016年4月17日
 */
public class MessageUtil {
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_NEWS = "news";//图文消息
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_MUSIC = "music";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_SCANCODE = "scancode_push";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	public static final String MESSAGE_SCAN = "SCAN";
	
	/**
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * 微信传来的xml格式转换成map
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		Element root = doc.getRootElement();
		
		List<Element> list = root.elements();
		for(Element e : list){
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}
	
	/**
	 * @return
	 * 用户关注时的自动回复
	 */
	public static String onSubscriptionAutoReply(){
		return "才来？！等你很久了";
	}
	
	/**
	 * @return
	 * 菜单中的click菜单的click事件被触发时
	 */
	public static String onClickAutoReply(){
		return "你点了click菜单";
	}
	
	/**
	 * @return
	 * 菜单中的view菜单的view事件被触发时
	 */
	public static String onViewAutoReply(){
		return "你点了view菜单";
	}
	
	/**
	 * @return
	 * 菜单中的弹出二维码工具的事件被触发时
	 */
	public static String onScanCodeAutoReply(){
		return "你打开了二维码扫码器";
	}
	
	/**
	 * @param textMessage
	 * @return
	 * 将文本消息对象转换为xml
	 */
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		//原始组装的根节点是com.makao.po.TextMessage，将它转换成'xml'，与微信文档中定义的一致
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	
	/**
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 * 重载的文本消息转换为xml
	 */
	public static String textMessageToXml(String toUserName, String fromUserName, String content){
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MessageUtil.MESSAGE_TEXT);
		text.setContent(content);
		text.setCreateTime(new Date().toLocaleString());

		return textMessageToXml(text);
	}
	
	/**
	 * <xml>
		<ToUserName><![CDATA[toUser]]></ToUserName>
		<FromUserName><![CDATA[fromUser]]></FromUserName>
		<CreateTime>12345678</CreateTime>
		<MsgType><![CDATA[news]]></MsgType>
		<ArticleCount>2</ArticleCount>
		<Articles>
		<item>
		<Title><![CDATA[title1]]></Title> 
		<Description><![CDATA[description1]]></Description>
		<PicUrl><![CDATA[picurl]]></PicUrl>
		<Url><![CDATA[url]]></Url>
		</item>
		<item>
		<Title><![CDATA[title]]></Title>
		<Description><![CDATA[description]]></Description>
		<PicUrl><![CDATA[picurl]]></PicUrl>
		<Url><![CDATA[url]]></Url>
		</item>
		</Articles>
		</xml>
	 * @param newsMessage
	 * @return
	 * 将图文消息转换为xml
	 */
	public static String newsMessageToXml(NewsMessage newsMessage){
		XStream xstream = new XStream();
		//原始组装的根节点是com.makao.po.TextMessage，将它转换成'xml'，与微信文档中定义的一致
		xstream.alias("xml", newsMessage.getClass());
		//里面的item也要手动修改
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}
	

	/**
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 * 重载的将图文消息转换为xml
	 */
	public static String newsMessageToXml(String toUserName, String fromUserName, List<News> items){
		NewsMessage newsMessage = new NewsMessage();
		
		newsMessage.setFromUserName(toUserName);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setCreateTime(new Date().toGMTString());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticleCount(items.size());
		newsMessage.setArticles(items);

		return newsMessageToXml(newsMessage);
	}
	
	/**
	 * <xml>
		<ToUserName><![CDATA[toUser]]></ToUserName>
		<FromUserName><![CDATA[fromUser]]></FromUserName>
		<CreateTime>12345678</CreateTime>
		<MsgType><![CDATA[image]]></MsgType>
		<Image>
		<MediaId><![CDATA[media_id]]></MediaId>
		</Image>
		</xml>
	 * @param imageMessage
	 * @return
	 * 将图片消息转换成xml
	 */
	public static String imageMessageToXml(ImageMessage imageMessage){
		XStream xstream = new XStream();
		//原始组装的根节点是com.makao.po.ImageMessage，将它转换成'xml'，与微信文档中定义的一致
		xstream.alias("xml", imageMessage.getClass());
		//里面的com.makao.po.Image也要手动修改为Image
		xstream.alias("Image", new Image().getClass());
		return xstream.toXML(imageMessage);
	}
	
	/**
	 * @param toUserName
	 * @param fromUserName
	 * @param image
	 * @return
	 * 重载的将图片消息转换为xml
	 */
	public static String imageMessageToXml(String toUserName, String fromUserName, Image image){
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setCreateTime(new Date().toGMTString());
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setImage(image);
		
		return imageMessageToXml(imageMessage);
	}
	
	/**
	 * <xml>
		<ToUserName><![CDATA[toUser]]></ToUserName>
		<FromUserName><![CDATA[fromUser]]></FromUserName>
		<CreateTime>12345678</CreateTime>
		<MsgType><![CDATA[music]]></MsgType>
		<Music>
		<Title><![CDATA[TITLE]]></Title>
		<Description><![CDATA[DESCRIPTION]]></Description>
		<MusicUrl><![CDATA[MUSIC_Url]]></MusicUrl>
		<HQMusicUrl><![CDATA[HQ_MUSIC_Url]]></HQMusicUrl>
		<ThumbMediaId><![CDATA[media_id]]></ThumbMediaId>
		</Music>
		</xml>
	 * @param musicMessage
	 * @return
	 * 将音乐消息转换成xml
	 */
	public static String musicMessageToXml(MusicMessage musicMessage){
		XStream xstream = new XStream();
		//原始组装的根节点是com.makao.po.MusicMessage，将它转换成'xml'，与微信文档中定义的一致
		xstream.alias("xml", musicMessage.getClass());
		//里面的com.makao.po.Music也要手动修改为Music
		xstream.alias("Music", new Music().getClass());
		return xstream.toXML(musicMessage);
	}
	
	/**
	 * @param toUserName
	 * @param fromUserName
	 * @param music
	 * @return
	 * 重载的将音乐消息转换成xml
	 */
	public static String musicMessageToXml(String toUserName, String fromUserName, Music music){
		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setFromUserName(toUserName);
		musicMessage.setToUserName(fromUserName);
		musicMessage.setCreateTime(new Date().toGMTString());
		musicMessage.setMsgType(MESSAGE_MUSIC);
		musicMessage.setMusic(music);
		
		return musicMessageToXml(musicMessage);
	}
	
}
