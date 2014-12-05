package com.lee.appmanager;

import com.lee.appmanager.domain.TaskInfo;
import com.lee.appmanager.receiver.LockScreenReceiver;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author liyayun
 * @date 2014-11-30 обнГ6:48:51  
 */
public class MyApplication extends Application {
	public TaskInfo taskinfo;

	@Override
	public void onCreate() {
		super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.setPriority(1000);
        LockScreenReceiver recevier = new LockScreenReceiver();
        registerReceiver(recevier, filter);
	}
	
	
}
