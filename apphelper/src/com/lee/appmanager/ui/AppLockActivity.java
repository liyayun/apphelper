package com.lee.appmanager.ui;

import com.lee.appmanager.R;
import com.lee.appmanager.service.WatchDogService;
import com.lee.appmanager.util.MD5Encoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

/**
 * @author liyayun
 * @date 2014-11-30 下午6:51:54  
 */
public class AppLockActivity extends Activity implements android.view.View.OnClickListener{
	private LinearLayout ll_lock_back;
	private LinearLayout ll_applock_set;
	private LinearLayout ll_applock_view;
	private Intent watchdogintent;
	private Switch switch_lockapp;
	private final String TAG = "AppLockActivity";
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_app_lock);
		sp=getSharedPreferences("config", Context.MODE_PRIVATE);
		ll_lock_back=(LinearLayout) findViewById(R.id.layout_lock_back);
		ll_applock_set=(LinearLayout) findViewById(R.id.ll_applock_set);
		ll_applock_view=(LinearLayout) findViewById(R.id.ll_applock_view);
		switch_lockapp=(Switch) findViewById(R.id.switch_lockapp);
		ll_lock_back.setOnClickListener(this);
		switch_lockapp.setOnCheckedChangeListener(new SwitchOnCheckedChangeListener());
		watchdogintent = new Intent(this,WatchDogService.class);
		if (islocked()) {
			switch_lockapp.setChecked(true);
			startService(watchdogintent);
		}
		ll_applock_set.setOnClickListener(this);
		ll_applock_view.setOnClickListener(this);
	}
	
	private class SwitchOnCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				Log.i(TAG, "开启程序锁");
				String applockPass=sp.getString("applockPass", null);
				if(applockPass!=null){
					startService(watchdogintent);
					Editor editor=sp.edit();
					editor.putBoolean("islocked", true);
					editor.commit();
				}else{
					Log.i(TAG, "弹出设置程序锁密码窗口");
					showSetPassDialog();
				}
				
				switch_lockapp.setText("程序锁已开启");
			} else {
				Log.i(TAG, "关闭程序锁");
				stopService(watchdogintent);
				Editor editor=sp.edit();
				editor.putBoolean("islocked", false);
				editor.commit();
				switch_lockapp.setText("程序锁未开启");
			}
		}
		
	}
	
	private void showSetPassDialog() {
		AlertDialog.Builder builder=new Builder(this);
		builder.setCancelable(false);
		builder.setTitle("设置程序锁密");
		View view_setpass=View.inflate(this, R.layout.view_setpass, null);
		final EditText et_pass=(EditText) view_setpass.findViewById(R.id.et_pass);
		final EditText et_comfirm=(EditText) view_setpass.findViewById(R.id.et_pass_comfirm);
		builder.setView(view_setpass);
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String password=et_pass.getText().toString().trim();
				String comfirm=et_comfirm.getText().toString().trim();
				if (!"".equals(password)) {
					if(password.equals(comfirm)){
						String pass_md5=MD5Encoder.md5Encode(password);
						Editor editor=sp.edit();
						editor.putString("applockPass", pass_md5);
						editor.putBoolean("islocked", true);
						editor.commit();
						startService(watchdogintent);
					}else{
						Toast.makeText(AppLockActivity.this, "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
						showSetPassDialog();
					}
				}else{
					Toast.makeText(AppLockActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
					showSetPassDialog();
				}
			}

		});
		
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		});
		builder.create().show();
	}
	
	private boolean islocked(){
		return sp.getBoolean("islocked", false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_lock_back:
			finish();
			break;
		case R.id.ll_applock_view:
			Log.i(TAG, "进入程序锁界面");
			Intent view=new Intent(this, AppLockStatusActivity.class);
			startActivity(view);
			break;
		case R.id.ll_applock_set:
			Log.i(TAG, "进入程序锁界面");
			Intent setPass=new Intent(this, SetPassActivity.class);
			startActivity(setPass);
			break;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_lock, menu);
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
