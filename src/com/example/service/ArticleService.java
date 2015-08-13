package com.example.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dao.ArticleDbHelper;
import com.example.domain.ArticleItem;

public class ArticleService {

	private ArticleDbHelper dbOpenHelper;

	private static ArticleService instance = null;

	private static final String TAG = ArticleService.class.getSimpleName();

	private ArticleService(Context context) {
		dbOpenHelper = new ArticleDbHelper(context);
	}

	public static ArticleService getInstance(Context context) {
		if (instance == null) {
			instance = new ArticleService(context);
		}
		return instance;
	}

	public void insertData(ArticleItem article) {
		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
		database.beginTransaction();
		try {

			ContentValues contentValues = new ContentValues();
			contentValues.put("title", article.getTitle());
			contentValues.put("host", article.getHost());
			contentValues.put("iconUrl", article.getIconUrl());

			database.insert("articleInfo", null, contentValues);

			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}

	}

	public long getCount() {
		SQLiteDatabase database = dbOpenHelper.getReadableDatabase();

		Cursor cursor = database.rawQuery("select count(*) from articleInfo",
				null);

		if (cursor.moveToNext()) {
			return cursor.getLong(0);
		}

		return 0;
	}

	public List<ArticleItem> getAllItem() {

		Log.i(TAG, "getAllItem");

		List<ArticleItem> articleItems = new ArrayList<ArticleItem>();

		SQLiteDatabase database = dbOpenHelper.getReadableDatabase();

		Cursor cursor = database.query("articleInfo", new String[] { "title",
				"host", "iconUrl" }, null, null, null, null, null);

		while (cursor.moveToNext()) {
			ArticleItem item = new ArticleItem(cursor.getString(0),
					cursor.getString(1), cursor.getString(2));

			articleItems.add(item);
		}

		return articleItems;
	}

	public void deleteItem(int itemId) {
		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();

		database.beginTransaction();

		try {
			String[] args = new String[] { String.valueOf(itemId) };
			int rowAffected = database.delete("articleInfo", "articleId=?",
					args);
			Log.i(TAG, "rowAffected : " + rowAffected);
		} finally {
			database.endTransaction();
		}
	}

}
