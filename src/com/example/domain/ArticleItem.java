package com.example.domain;

public class ArticleItem {

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public ArticleItem(String title, String host, String iconUrl) {
		super();
		this.title = title;
		this.host = host;
		this.iconUrl = iconUrl;
	}

	public ArticleItem() {

	}

	private String title;

	private String host;

	private String iconUrl;

	private String html;

}
