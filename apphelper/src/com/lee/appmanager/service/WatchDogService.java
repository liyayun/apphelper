package com.lee.appmanager.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.lee.appmanager.db.dao.AppLockDao;
import com.lee.appmanager.ui.LockScreenActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * @author liyayun
 * @date 2014-11-30 下午6:51:28  
 */
public class WatchDogService extends Service {
	protected static final String TAG = "WatchDogService";
	private AppLockDao dao;
	private List<String> lockapps;
	private ActivityManager am;
	private Intent lockappintent;
	private boolean flag;
	private MyBinder myBinder;
	private KeyguardManager keyguardManager;
	private List<String> tempstopapps;
	private String packname;
	private String packageName;

	@Override
	public IBinder onBind(Intent intent) {

		return myBinder;
	}

	public class MyBinder extends Binder implements IService {

		public void callAppProtectStart(String packname) {

			appProtectStart(packname);
		}

		public void callAppProtectStop(String packname) {
			appProtectStop(packname);
		}

		public void killTask() {
			
			Method method;
			try {
				method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
				method.invoke(am, packageName);
				Log.i(TAG, "杀死:" + packageName+"成功");
			} catch (Exception e) {
				Log.i(TAG, "杀死:" + packageName+"失败");
				am.killBackgroundProcesses(packageName);
				Intent home = new Intent(Intent.ACTION_MAIN);
				home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				home.addCategory(Intent.CATEGORY_HOME);
				startActivity(home);
				e.printStackTrace();
			} 
		}
	}

	/**
	 * 重新开启对应用的保护
	 * 
	 * @param packname
	 */
	public void appProtectStart(String packname) {
		if (tempstopapps.contains(packname)) {
			tempstopapps.remove(packname);
		}
	}

	/**
	 * 临时停止对某个app的保护
	 * 
	 * @param packname
	 */
	public void appProtectStop(String packname) {
		tempstopapps.add(packname);
	}

	/**
	 * 服务第一次创建的时候 调用的方法
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "看门狗服务被创建");
		getContentResolver().registerContentObserver(
				Uri.parse("content://com.lee.applock"), true,
				new MyObserver(new Handler()));
		keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

		myBinder = new MyBinder();
		dao = new AppLockDao(this);
		tempstopapps = new ArrayList<String>();
		flag = true;
		// 得到所有的要锁定的应用程序
		lockapps = dao.getAllApps();
		lockappintent = new Intent(this, LockScreenActivity.class);
		// 服务是不存在任务栈的 要在服务里面开启activity的话 必须添加这样一个flag
		lockappintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		new Thread() {

			@Override
			public void run() {
				// 开启看门狗
				while (flag) {
					try {
						// 判断屏幕是否是锁屏状态
						if (keyguardManager.inKeyguardRestrictedInputMode()) {
							// 清空临时的集合
							tempstopapps.clear();

						}

						// 返回系统里面的任务栈的信息 , taskinfos的集合里面只有一个元素
						// 得到当前正在运行程序的包名
						// 内容就是当前正在运行的进程对应的任务栈
						List<RunningTaskInfo> taskinfos = am.getRunningTasks(1);
						RunningTaskInfo currenttask = taskinfos.get(0);
						// 获取当前用户可见的activity 所在的程序的包名
						packname = currenttask.topActivity.getPackageName();
						// Log.i(TAG, "当前运行" + packname);
						if (lockapps.contains(packname)) {

							// todo : 如果当前的应用程序 需要临时的被终止保护
							if (tempstopapps.contains(packname)) {

								// return;
								sleep(2000);
								continue;
							}
							Log.i(TAG, "需要锁定" + packname);
							packageName = packname;
							// todo 弹出来一个锁定的界面 让用户输入密码
							lockappintent.putExtra("packname", packname);
							startActivity(lockappintent);

						} else {
							// todo 放行执行
						}
						sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		flag = false;
	}

	private class MyObserver extends ContentObserver {

		public MyObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {

			super.onChange(selfChange);
			// 重新更新lockapps集合里面的内容
			Log.i("change", "--------------数据库内容变化了");
			lockapps = dao.getAllApps();
		}

	}

}
