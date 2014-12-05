package com.lee.appmanager.ui;

import java.util.List;


import com.lee.appmanager.R;
import com.lee.appmanager.adapter.AppManagerAdapter;
import com.lee.appmanager.business.AppInfoProvider;
import com.lee.appmanager.domain.AppInfo;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * @author liyayun
 * @date 2014-11-30 下午6:52:16  
 */
public class AppManagerActivity extends Activity implements OnClickListener{
	protected static final int GET_APPS_FINISH = 1;
	private static final String TAG = "AppManagerActivity";
	private static final String USER_APPS = "user_apps";
	private static final String SYSTEM_APPS = "system_apps";
	private ListView lv;
	private ProgressBar pb;
	private TextView manager_tv_user;
	private TextView manager_tv_system;
	protected AppInfoProvider provider;
	protected List<AppInfo> appinfos;
	protected AppManagerAdapter adapter;
	private PopupWindow localPopupWindow;
	@SuppressLint("HandlerLeak")
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_APPS_FINISH:
				pb.setVisibility(View.INVISIBLE);
				adapter=new AppManagerAdapter(appinfos, AppManagerActivity.this);
				lv.setAdapter(adapter);
				break;
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_manager);
		lv=(ListView)findViewById(R.id.ll_lv_apps);
		pb=(ProgressBar) findViewById(R.id.pb_app_loding);
		manager_tv_user=(TextView) findViewById(R.id.manager_tv_user);
		manager_tv_system=(TextView) findViewById(R.id.manager_tv_system);
		manager_tv_system.setOnClickListener(this);
		manager_tv_user.setOnClickListener(this);
		
		initUI(USER_APPS);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				dismissPopUpwindow();

				// 获取当前view对象在窗体中的位置
				int[] arrayOfInt = new int[2];
				view.getLocationInWindow(arrayOfInt);

				int i = arrayOfInt[0] + 150;
				int j = arrayOfInt[1];

				// String packname = info.getPackname();
				/*
				 * TextView tv = new TextView (AppManagerActivity.this);
				 * tv.setTextSize(20); tv.setTextColor(Color.RED);
				 * tv.setText(packname);
				 */
				View popupview = View.inflate(AppManagerActivity.this,
						R.layout.popup_item, null);
				LinearLayout ll_start = (LinearLayout) popupview
						.findViewById(R.id.ll_start);
				LinearLayout ll_uninstall = (LinearLayout) popupview
						.findViewById(R.id.ll_uninstall);
				LinearLayout ll_share = (LinearLayout) popupview
						.findViewById(R.id.ll_share);

				// 把当前条目在listview中的位置设置给view对象
				ll_share.setTag(position);
				ll_uninstall.setTag(position);
				ll_start.setTag(position);

				ll_start.setOnClickListener(AppManagerActivity.this);
				ll_uninstall.setOnClickListener(AppManagerActivity.this);
				ll_share.setOnClickListener(AppManagerActivity.this);

				LinearLayout ll = (LinearLayout) popupview
						.findViewById(R.id.ll_popup);
				ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
				sa.setDuration(200);
				localPopupWindow = new PopupWindow(popupview, 700, 180);
				// 一定要记得给popupwindow设置背景颜色
				// Drawable background = new ColorDrawable(Color.TRANSPARENT);
				Drawable background = getResources().getDrawable(
						R.drawable.local_popup_bg);
				localPopupWindow.setBackgroundDrawable(background);
				localPopupWindow.showAtLocation(view, Gravity.LEFT
						| Gravity.TOP, i, j);
				ll.startAnimation(sa);

			}
		});

		lv.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				dismissPopUpwindow();

			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPopUpwindow();

			}
		});
		
	}

	private void initUI(final String apps) {
		pb.setVisibility(View.VISIBLE); 
		new Thread(){
			@Override
			public void run() {
				provider=new AppInfoProvider(AppManagerActivity.this);
				if("system_apps".equals(apps)){
					appinfos=provider.getSystemApps();
				}else{
					appinfos=provider.getUserApps();
				}
				Message msg=new Message();
				msg.what=GET_APPS_FINISH;
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	private void dismissPopUpwindow() {
		/*
		 * 保证只有一个popupwindow的实例存在
		 */
		if (localPopupWindow != null) {
			localPopupWindow.dismiss();
			localPopupWindow = null;
		}
	}

	public void onClick(View v) {
		int positon=0;
		if( v.getTag()!=null){
			positon = (Integer) v.getTag();
		}
		AppInfo item = appinfos.get(positon);
		String packname = item.getPackName();

		dismissPopUpwindow();
		switch (v.getId()) {
			case R.id.manager_tv_user:
				manager_tv_user.setBackgroundColor(getResources().getColor(R.color.manager_title_bg_clicked));
				manager_tv_system.setBackgroundColor(getResources().getColor(R.color.manager_title_bg));
				initUI(USER_APPS);
				break;
			case R.id.manager_tv_system:
				manager_tv_user.setBackgroundColor(getResources().getColor(R.color.manager_title_bg));
				manager_tv_system.setBackgroundColor(getResources().getColor(R.color.manager_title_bg_clicked));
				initUI(SYSTEM_APPS);
				break;
			case R.id.ll_share:
				Log.i(TAG, "分享" + packname);
				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				// shareIntent.putExtra("android.intent.extra.SUBJECT", "分享");
				shareIntent.setType("text/plain");
				// 需要指定意图的数据类型
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
				shareIntent.putExtra(Intent.EXTRA_TEXT,
						"推荐你一个应用程序：" + item.getAppName());
				shareIntent = Intent.createChooser(shareIntent, "分享"); 
				startActivity(shareIntent);
				break;
			case R.id.ll_uninstall:
	
				// 需求不能卸载系统的应用程序
				if (item.isSystemApp()) {
					Toast.makeText(this, "系统应用不能被删除", 0).show();
				} else {
					Log.i(TAG, "卸载" + packname);
					Uri uri=Uri.parse("package:"+packname);
					Intent deleteIntent=new Intent();
					deleteIntent.setAction(Intent.ACTION_DELETE);
					deleteIntent.setData(uri);
					startActivityForResult(deleteIntent, 0);
				}
				break;
			case R.id.ll_start:
				Log.i(TAG, "运行" + packname);
				// getPackageManager().queryIntentActivities(intent, flags);
				try {
					PackageInfo info = getPackageManager().getPackageInfo(
							packname,
							PackageManager.GET_UNINSTALLED_PACKAGES
									| PackageManager.GET_ACTIVITIES);
					ActivityInfo[] activityinfos = info.activities;
					if (activityinfos.length > 0) {
						ActivityInfo startActivity = activityinfos[0];
						Intent intent = new Intent();
						intent.setClassName(packname, startActivity.name);
						startActivity(intent);
					} else {
						Toast.makeText(this, "当前应用程序无法启动", 0).show();
					}
				} catch (Exception e) {
					Toast.makeText(this, "应用程序无法启动", 0).show();
					e.printStackTrace();
				}
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		initUI(USER_APPS);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "销毁activity");
		dismissPopUpwindow();
	}

}
