package com.lee.appmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author liyayun
 * @date 2014-11-30 下午6:50:03  
 */
public class AppLockDBHelper extends SQLiteOpenHelper{

	public AppLockDBHelper(Context context) {
		super(context, "applock.db", null, 1);
	}

	/**
	 * 第一次创建数据库的时候执行 oncreate方法
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE applock (_id integer primary key autoincrement, packname varchar(30))");
	}

	/**
	 * 更新数据库的操作
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	
	
	
}
