package com.example.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ArticleDbHelper extends SQLiteOpenHelper{

    private static final String name = "article.db";
    
    private static final int version = 1;
    
    private static final String TAG = ArticleDbHelper.class.getSimpleName();

	public ArticleDbHelper(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		Log.i(TAG, "onCreate start");
		// TODO Auto-generated method stub
		String createSql = " create table if not exists articleInfo (articleId integer primary key autoincrement,title varchar(256),host varchar(128),iconUrl varchar(256))"; 
		db.execSQL(createSql);
		Log.i(TAG,"onCreate end");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
