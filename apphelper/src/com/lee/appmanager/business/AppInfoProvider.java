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
 * @date 2014-11-30 ����6:49:46  
 */
public class AppInfoProvider {
	private static final String TAG = "AppInfoProvider";
	private PackageManager packmanager;
	
	public AppInfoProvider(Context context) {
		packmanager = context.getPackageManager();
		Log.i(TAG, "AppInfoProvider������");
	}

	/**
	 * ���ص�ǰ�ֻ����氲װ�����еĳ�����Ϣ�ļ���
	 * @return Ӧ�ó���ļ���
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
//				 Log.i(TAG, appname+"���û�Ӧ�ó���");
			 }else{
				 myApp.setSystemApp(true);
//				 Log.i(TAG, appname+"��ϵͳӦ�ó���");
			 }
			appinfos.add(myApp);
		}
		return appinfos;
	}
	
	/**
	 * ���ص�ǰ�ֻ����氲װ�����е��û�������Ϣ�ļ���
	 * @return Ӧ�ó���ļ���
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
//				Log.i(TAG, appname+"���û�Ӧ�ó���");
				appinfos.add(myApp);
			}else{
				myApp.setSystemApp(true);
//				Log.i(TAG, appname+"��ϵͳӦ�ó���");
			}
		}
		return appinfos;
	}
	
	/**
	 * ���ص�ǰ�ֻ����氲װ�����е�ϵͳ������Ϣ�ļ���
	 * @return Ӧ�ó���ļ���
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
//				Log.i(TAG, appname+"���û�Ӧ�ó���");
			}else{
				myApp.setSystemApp(true);
				appinfos.add(myApp);
//				Log.i(TAG, appname+"��ϵͳӦ�ó���");
			}
		}
		return appinfos;
	}
	
	
	/**
	 * �ж�ĳ��Ӧ�ó����� ����������Ӧ�ó���
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
