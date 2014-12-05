package com.lee.appmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author liyayun
 * @date 2014-11-30 ����6:50:03  
 */
public class AppLockDBHelper extends SQLiteOpenHelper{

	public AppLockDBHelper(Context context) {
		super(context, "applock.db", null, 1);
	}

	/**
	 * ��һ�δ������ݿ��ʱ��ִ�� oncreate����
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE applock (_id integer primary key autoincrement, packname varchar(30))");
	}

	/**
	 * �������ݿ�Ĳ���
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	
	
	
}
