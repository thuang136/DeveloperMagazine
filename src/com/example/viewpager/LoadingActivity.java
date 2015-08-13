package com.example.viewpager;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.domain.ArticleItem;
import com.example.service.ArticleService;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class LoadingActivity extends Activity {

	private SlideShowView slideShowView;

	private ListView articleListView;

	ArticleService articleService;

	private static final String TAG = "LoadingActivity";

	private static final String URL = "http://192.168.1.101:8080/CrawlerServer/yeah";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_loading);

		articleListView = (ListView) this.findViewById(R.id.articleList);

		articleService = ArticleService.getInstance(this);

		new ArticleJsonAsyncTask().execute(URL);

	}

	private class ArticleJsonAsyncTask extends
			AsyncTask<String, Integer, List<ArticleItem>> {

		@Override
		protected List<ArticleItem> doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			String result = null;
			List<ArticleItem> articleItems = new ArrayList<ArticleItem>();
			HttpGet getMethod = new HttpGet(url);
			DefaultHttpClient client = new DefaultHttpClient();

			try {
				HttpResponse httpResponse = client.execute(getMethod);

				int length = 4 * 1024;

				if (length < 0) {
					throw new Exception("invalid http response length");
				}

				StringBuilder stringBuilder = new StringBuilder(length);
				InputStreamReader isr = new InputStreamReader(httpResponse
						.getEntity().getContent(), HTTP.UTF_8);

				char buffer[] = new char[length];

				int count = 0;

				while ((count = isr.read(buffer, 0, length)) != -1) {

					stringBuilder.append(buffer, 0, count);

				}
				result = stringBuilder.toString();

				JSONObject jsonObject = new JSONObject(result);
				JSONArray jsonArray = jsonObject.getJSONArray("articles");

				for (int i = 0; i < jsonArray.length(); i++) {

					ArticleItem item = new ArticleItem();

					JSONObject member = (JSONObject) jsonArray.get(i);

					item.setTitle(member.getString("title"));
					item.setHost(member.getString("domain"));
					item.setIconUrl(member.getString("imgUrl"));
					item.setHtml(member.getString("html"));

					articleItems.add(item);
				}

			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			return articleItems;

		}

		protected void onPreExecute() {
		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(List<ArticleItem> result) {

			result.add(0, new ArticleItem());

			final ArticleItem[] items = result.toArray(new ArticleItem[0]);

			articleListView.setAdapter(new ArticleListAdapter(
					LoadingActivity.this, result));

			articleListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					ArticleItem item = items[position];
					intent.putExtra("html", item.getHtml());
					intent.setClass(LoadingActivity.this, WebViewActivity.class);
					startActivity(intent);
				}

			});
		}

	}
}
