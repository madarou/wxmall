package com.makao.weixin.po;

import java.util.List;

/**
 * @author makao
 * @date 2016年4月17日
 * 微信图文消息
 */
public class NewsMessage extends BaseMessage{
	private int ArticleCount;
	private List<News> Articles;
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<News> getArticles() {
		return Articles;
	}
	public void setArticles(List<News> articles) {
		Articles = articles;
	}
	
}
