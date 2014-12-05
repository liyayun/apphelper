package com.lee.appmanager.business;

import java.util.ArrayList;
import java.util.List;

import com.lee.appmanager.domain.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * @author liyayun
 * @date 2014-11-30 下午6:49:46  
 */
public class AppInfoProvider {
	private static final String TAG = "AppInfoProvider";
	private PackageManager packmanager;
	
	public AppInfoProvider(Context context) {
		packmanager = context.getPackageManager();
		Log.i(TAG, "AppInfoProvider被创建");
	}

	/**
	 * 返回当前手机里面安装的所有的程序信息的集合
	 * @return 应用程序的集合
	 */
	public List<AppInfo> getAllApps(){
		List<AppInfo> appinfos = new ArrayList<AppInfo>();
		List<PackageInfo> packinfos = packmanager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(PackageInfo info :packinfos){
			AppInfo myApp = new AppInfo();
			String packname = info.packageName;
			myApp.setPackName(packname);
			ApplicationInfo appinfo = info.applicationInfo;
			Drawable icon = appinfo.loadIcon(packmanager);
			myApp.setIcon(icon);
			String appname = appinfo.loadLabel(packmanager).toString();
			myApp.setAppName(appname);
			 if(filterApp(appinfo)){
				 myApp.setSystemApp(false);
//				 Log.i(TAG, appname+"是用户应用程序");
			 }else{
				 myApp.setSystemApp(true);
//				 Log.i(TAG, appname+"是系统应用程序");
			 }
			appinfos.add(myApp);
		}
		return appinfos;
	}
	
	/**
	 * 返回当前手机里面安装的所有的用户程序信息的集合
	 * @return 应用程序的集合
	 */
	public List<AppInfo> getUserApps(){
		List<AppInfo> appinfos = new ArrayList<AppInfo>();
		List<PackageInfo> packinfos = packmanager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(PackageInfo info :packinfos){
			AppInfo myApp = new AppInfo();
			String packname = info.packageName;
			myApp.setPackName(packname);
			ApplicationInfo appinfo = info.applicationInfo;
			Drawable icon = appinfo.loadIcon(packmanager);
			myApp.setIcon(icon);
			String appname = appinfo.loadLabel(packmanager).toString();
			myApp.setAppName(appname);
			if(filterApp(appinfo)){
				myApp.setSystemApp(false);
//				Log.i(TAG, appname+"是用户应用程序");
				appinfos.add(myApp);
			}else{
				myApp.setSystemApp(true);
//				Log.i(TAG, appname+"是系统应用程序");
			}
		}
		return appinfos;
	}
	
	/**
	 * 返回当前手机里面安装的所有的系统程序信息的集合
	 * @return 应用程序的集合
	 */
	public List<AppInfo> getSystemApps(){
		List<AppInfo> appinfos = new ArrayList<AppInfo>();
		List<PackageInfo> packinfos = packmanager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(PackageInfo info :packinfos){
			AppInfo myApp = new AppInfo();
			String packname = info.packageName;
			myApp.setPackName(packname);
			ApplicationInfo appinfo = info.applicationInfo;
			Drawable icon = appinfo.loadIcon(packmanager);
			myApp.setIcon(icon);
			String appname = appinfo.loadLabel(packmanager).toString();
			myApp.setAppName(appname);
			if(filterApp(appinfo)){
				myApp.setSystemApp(false);
//				Log.i(TAG, appname+"是用户应用程序");
			}else{
				myApp.setSystemApp(true);
				appinfos.add(myApp);
//				Log.i(TAG, appname+"是系统应用程序");
			}
		}
		return appinfos;
	}
	
	
	/**
	 * 判断某个应用程序是 不是三方的应用程序
	 * @param info
	 * @return 
	 */
    public boolean filterApp(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }
}
