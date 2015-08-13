package com.example.viewpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {

	private WebView webView;

	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		this.setContentView(R.layout.layout_webview);
		Intent intent = this.getIntent();
		String html = intent.getStringExtra("html");
		webView = (WebView) findViewById(R.id.webview);

		// webView.loadUrl(url);

		WebSettings settings = webView.getSettings();

		settings.setUseWideViewPort(true);

		settings.setLoadWithOverviewMode(true);

		webView.loadData(html, "text/html; charset=UTF-8", null);

		/*
		 * 
		 * webView.setWebViewClient(new WebViewClient() { public boolean
		 * shouldOverrideUrlLoading(WebView view, String url) { String data =
		 * "<html>     <head>         <title>         </title>     </head>     <body> Hello World    </body></html>"
		 * ; view.loadData(data, "text/html", "utf-8"); return true; } });
		 */
	}
}
