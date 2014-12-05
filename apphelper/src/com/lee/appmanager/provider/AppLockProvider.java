package com.lee.appmanager.provider;

import com.lee.appmanager.db.dao.AppLockDao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * @author liyayun
 * @date 2014-11-30 下午6:50:57  
 */
public class AppLockProvider extends ContentProvider {

	private static final int INSERT = 10;
	private static final int DELETE = 11;
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static Uri changeuri = Uri
			.parse("content://com.lee.applock");
	private AppLockDao dao;
	static {
		matcher.addURI("com.lee.applock", "insert", INSERT);
		matcher.addURI("com.lee.applock", "delete", DELETE);
	}

	@Override
	public boolean onCreate() {
		dao = new AppLockDao(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int result = matcher.match(uri);
		if (result == INSERT) {
			String packname = (String) values.get("packname");

			dao.add(packname);
			getContext().getContentResolver().notifyChange(changeuri, null);
		} else {
			throw new IllegalArgumentException("uri地址不正确");
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int result = matcher.match(uri);
		if (result == DELETE) {
			String packname = selectionArgs[0];
			dao.delete(packname);
			getContext().getContentResolver().notifyChange(changeuri, null);
		} else {
			throw new IllegalArgumentException("uri地址不正确");
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
