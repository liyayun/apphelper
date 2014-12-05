package com.lee.appmanager.ui;

import com.lee.appmanager.R;
import com.lee.appmanager.service.IService;
import com.lee.appmanager.service.WatchDogService;
import com.lee.appmanager.util.MD5Encoder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author liyayun
 * @date 2014-11-30 下午6:52:20  
 */
public class LockScreenActivity extends Activity implements OnClickListener{
	private ImageView iv_app_lock_pwd_icon;
	private TextView tv_app_lock_pwd_name;
	private EditText et_app_lock_pwd;
	private SharedPreferences sp;
	private String realpwd;
	private IService iService;
	private MyConn myconn;
	private String packname;
	private TextView tv_cancel;
	private TextView tv_confirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_lock_pwd);
		myconn = new MyConn();
		Intent intent = new Intent(this,WatchDogService.class);
		bindService(intent, myconn, BIND_AUTO_CREATE);
		sp =getSharedPreferences("config", MODE_PRIVATE);
		realpwd = sp.getString("applockPass", "");
		packname = getIntent().getStringExtra("packname");
		iv_app_lock_pwd_icon = (ImageView) this
				.findViewById(R.id.iv_app_lock_pwd_icon);
		tv_app_lock_pwd_name = (TextView) this
				.findViewById(R.id.tv_app_lock_pwd_name);
		et_app_lock_pwd = (EditText) this.findViewById(R.id.et_app_lock_pwd);
		
		tv_cancel=(TextView) findViewById(R.id.tv_cancel);
		tv_confirm=(TextView) findViewById(R.id.tv_confirm);
		tv_cancel.setOnClickListener(this);
		tv_confirm.setOnClickListener(this);
		// 完成界面的初始化 
		ApplicationInfo appinfo;
		try {
			appinfo = getPackageManager().getPackageInfo(packname, 0).applicationInfo;

			Drawable appicon = appinfo.loadIcon(getPackageManager());
			String appname = appinfo.loadLabel(getPackageManager()).toString();
			iv_app_lock_pwd_icon.setImageDrawable(appicon);
			tv_app_lock_pwd_name.setText(appname);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_confirm:
			//得到用户输入的密码
			String password = et_app_lock_pwd.getText().toString().trim();
			if(TextUtils.isEmpty(password)){
				Toast.makeText(this, "密码不能为空", 1).show();
				return ;
			}else{
				
				if(MD5Encoder.md5Encode(password).equals(realpwd)){
					
					// 通知看门狗 临时的取消对这个程序的保护
					iService.callAppProtectStop(packname);
					finish();
				}else{
					Toast.makeText(this, "密码错误", 1).show();
					return ;
				}

			}
			break;

		case R.id.tv_cancel:
			iService.killTask();
			finish();
			break;
		}
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			return true; // 阻止按键事件继续向下分发
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private class MyConn implements ServiceConnection{

		public void onServiceConnected(ComponentName name, IBinder service) {
			iService = (IService)service;
			
		}

		public void onServiceDisconnected(ComponentName name) {
			
		}
		
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(myconn);
	}

	
}
