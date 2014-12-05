package com.lee.appmanager.ui;

import java.util.List;

import com.lee.appmanager.R;
import com.lee.appmanager.adapter.AppLockAdapter;
import com.lee.appmanager.business.AppInfoProvider;
import com.lee.appmanager.db.dao.AppLockDao;
import com.lee.appmanager.domain.AppInfo;
import com.lee.appmanager.util.DiffOperation;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * @author liyayun
 * @date 2014-11-30 下午6:52:08  
 */
public class AppLockStatusActivity extends Activity implements OnClickListener{
	private static final String TAG = "AppLockStatusActivity";
	private ListView lv_lock_status;
	private TextView tv_locked;
	private TextView tv_unlock;
	private View left_underline;
	private View right_underline;
	private List<AppInfo> apps;
	private List<AppInfo> loadapps;
	private AppInfoProvider provider;
	private ProgressBar pb_lock_loading;
	private AppLockAdapter adapter;
	private AppLockDao dao;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			pb_lock_loading.setVisibility(View.INVISIBLE);
			adapter = new AppLockAdapter(loadapps,AppLockStatusActivity.this);
			lv_lock_status.setAdapter(adapter);
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lock_status_list);
		pb_lock_loading=(ProgressBar) findViewById(R.id.pb_lock_loding);
		lv_lock_status=(ListView) findViewById(R.id.lv_lock_status);
		tv_locked=(TextView) findViewById(R.id.lsl_tv_locked);
		tv_unlock=(TextView) findViewById(R.id.lsl_tv_unlock);
		left_underline=findViewById(R.id.view_left_underline);
		right_underline=findViewById(R.id.view_right_underline);
		tv_locked.setOnClickListener(this);
		tv_unlock.setOnClickListener(this);
		
		provider=new AppInfoProvider(this);
		dao = new AppLockDao(this);
		initUI(false);
		lv_lock_status.setOnItemClickListener(new ListOnItemClickListener());
	}
	
	
	private class ListOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			TranslateAnimation moveR = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			moveR.setDuration(300);
			view.startAnimation(moveR);
			
			String packname=loadapps.get(position).getPackName();
			if(dao.find(packname)){
//				dao.delete(packname);
				ContentResolver resolver=getContentResolver();
				resolver.delete(Uri.parse("content://com.lee.applock/delete"), null, new String[]{packname});
				Log.i(TAG, "从数据库删除:"+packname);
			}else{
				
//				dao.add(packname);
				ContentValues values = new ContentValues();
				values.put("packname", packname);
				getContentResolver().insert(Uri.parse("content://com.lee.applock/insert"), values);
				Log.i(TAG, "向数据库添加:"+packname);
			}
			
			loadapps.remove(position);
			adapter.notifyDataSetChanged();
			Log.i("AppLockAdapter", "点击:"+position);
		}
		
	}
	
	/**
	 * @param islock 判断要初始化的是不是已锁程序
	 */
	private void initUI(final boolean islock) {
		pb_lock_loading.setVisibility(View.VISIBLE);
		new Thread() {

			@Override
			public void run() {
				//得到手机上所安装的全部应用信息的集合
				apps = provider.getAllApps();
				
				//获得已加锁的所有的包名集合
				List<String> packages=dao.getAllApps();
				
				if(islock){
					Log.i(TAG, "初始化已加锁程序");
					loadapps=DiffOperation.fetchLock(apps, packages);
				}else{
					Log.i(TAG, "初始化未加锁程序");
					loadapps=DiffOperation.fetchUnlock(apps, packages);
				}
				handler.sendEmptyMessage(0);
			}

		}.start();
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.lsl_tv_locked:
			Log.i(TAG, "加载已加锁程序");
			left_underline.setVisibility(View.INVISIBLE);
			right_underline.setVisibility(View.VISIBLE);
			initUI(true);
			break;

		case R.id.lsl_tv_unlock:
			Log.i(TAG, "加载未加锁程序");
			left_underline.setVisibility(View.VISIBLE);
			right_underline.setVisibility(View.INVISIBLE);
			initUI(false);
			break;
		}
	}
}
