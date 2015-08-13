package com.example.viewpager;

import java.lang.ref.WeakReference;
import java.util.List;

import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MyTask extends AsyncTask<String, Integer, String>{

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		ListView view = null;
		ListAdapter adapter = null;
		BaseAdapter baseAdapter = null;
		List<String> str;
		WebView webView = null;
		WeakReference reference ;
		return null;
	}

}
