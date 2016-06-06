package com.makao.weixin.po;

/**
 * @author makao
 * @date 2016年4月16日
 * 微信文本消息对象
 */
public class TextMessage extends BaseMessage{
	
	private String Content;
	private String MsgId;
	
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	
}
