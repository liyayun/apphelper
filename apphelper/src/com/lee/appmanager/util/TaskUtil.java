package com.lee.appmanager.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * @author liyayun
 * @date 2014-11-30 ����6:53:11  
 */
public class TaskUtil {

	public static void killAllProcess(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningapps = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : runningapps) {
			String packname = info.processName;
			am.killBackgroundProcesses(packname);
		}

	}
	public static int getProcessCount(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningapps = am.getRunningAppProcesses();
		return runningapps.size();

	}
	public static String getMemeorySize(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		return TextFormater.getDataSize( outInfo.availMem);

	}
}
