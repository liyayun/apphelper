package com.lee.appmanager.ui;

import com.lee.appmanager.R;
import com.lee.appmanager.service.WatchDogService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * @author liyayun
 * @date 2014-11-30 下午6:52:24  
 */
public class MainActivity extends Activity implements OnClickListener{
	private static final String TAG = "MainActivity";
	private LinearLayout ll_appmanager;
	private LinearLayout ll_applock;
	private LinearLayout ll_running_app;
	private SharedPreferences sp;
	private Intent watchdogintent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		sp=getSharedPreferences("config", Context.MODE_PRIVATE);
		ll_appmanager=(LinearLayout) findViewById(R.id.main_ll_appmanager);
		ll_applock=(LinearLayout) findViewById(R.id.main_ll_applock);
		ll_running_app=(LinearLayout) findViewById(R.id.ll_running_app);
		ll_appmanager.setOnClickListener(this);
		ll_applock.setOnClickListener(this);
		ll_running_app.setOnClickListener(this);
		
		watchdogintent = new Intent(this,WatchDogService.class);
		if (islocked()) {
			startService(watchdogintent);
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_ll_appmanager:
			Log.i(TAG, "进入程序管理");
			Intent managerActivity=new Intent(this, AppManagerActivity.class);
			startActivity(managerActivity);
			break;

		case R.id.main_ll_applock:
			Log.i(TAG, "进入程序锁");
			Intent lockActivity=new Intent(this, AppLockActivity.class);
			startActivity(lockActivity);
			break;
		case R.id.ll_running_app:
			Log.i(TAG, "正在运行的程序..");
			Intent runningApp=new Intent(this, TaskManagerActivity.class);
			startActivity(runningApp);
			break;
		}
		
	}
	
	//判断程序锁是否开启
	private boolean islocked(){
		return sp.getBoolean("islocked", false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId(); 
		if (id == R.id.item_exit) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
