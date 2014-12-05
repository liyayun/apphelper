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
 * @date 2014-11-30 ����6:51:28  
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
				Log.i(TAG, "ɱ��:" + packageName+"�ɹ�");
			} catch (Exception e) {
				Log.i(TAG, "ɱ��:" + packageName+"ʧ��");
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
	 * ���¿�����Ӧ�õı���
	 * 
	 * @param packname
	 */
	public void appProtectStart(String packname) {
		if (tempstopapps.contains(packname)) {
			tempstopapps.remove(packname);
		}
	}

	/**
	 * ��ʱֹͣ��ĳ��app�ı���
	 * 
	 * @param packname
	 */
	public void appProtectStop(String packname) {
		tempstopapps.add(packname);
	}

	/**
	 * �����һ�δ�����ʱ�� ���õķ���
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "���Ź����񱻴���");
		getContentResolver().registerContentObserver(
				Uri.parse("content://com.lee.applock"), true,
				new MyObserver(new Handler()));
		keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

		myBinder = new MyBinder();
		dao = new AppLockDao(this);
		tempstopapps = new ArrayList<String>();
		flag = true;
		// �õ����е�Ҫ������Ӧ�ó���
		lockapps = dao.getAllApps();
		lockappintent = new Intent(this, LockScreenActivity.class);
		// �����ǲ���������ջ�� Ҫ�ڷ������濪��activity�Ļ� �����������һ��flag
		lockappintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		new Thread() {

			@Override
			public void run() {
				// �������Ź�
				while (flag) {
					try {
						// �ж���Ļ�Ƿ�������״̬
						if (keyguardManager.inKeyguardRestrictedInputMode()) {
							// �����ʱ�ļ���
							tempstopapps.clear();

						}

						// ����ϵͳ���������ջ����Ϣ , taskinfos�ļ�������ֻ��һ��Ԫ��
						// �õ���ǰ�������г���İ���
						// ���ݾ��ǵ�ǰ�������еĽ��̶�Ӧ������ջ
						List<RunningTaskInfo> taskinfos = am.getRunningTasks(1);
						RunningTaskInfo currenttask = taskinfos.get(0);
						// ��ȡ��ǰ�û��ɼ���activity ���ڵĳ���İ���
						packname = currenttask.topActivity.getPackageName();
						// Log.i(TAG, "��ǰ����" + packname);
						if (lockapps.contains(packname)) {

							// todo : �����ǰ��Ӧ�ó��� ��Ҫ��ʱ�ı���ֹ����
							if (tempstopapps.contains(packname)) {

								// return;
								sleep(2000);
								continue;
							}
							Log.i(TAG, "��Ҫ����" + packname);
							packageName = packname;
							// todo ������һ�������Ľ��� ���û���������
							lockappintent.putExtra("packname", packname);
							startActivity(lockappintent);

						} else {
							// todo ����ִ��
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
			// ���¸���lockapps�������������
			Log.i("change", "--------------���ݿ����ݱ仯��");
			lockapps = dao.getAllApps();
		}

	}

}
