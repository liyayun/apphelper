package com.lee.appmanager.domain;

import android.graphics.drawable.Drawable;

/**
 * @author liyayun
 * @date 2014-11-30 ÏÂÎç6:50:28  
 */
public class AppInfo {
	private String appName;
	private String packName;
	private Drawable icon;
	private boolean isSystemApp;
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public boolean isSystemApp() {
		return isSystemApp;
	}
	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}
	
	
	
}
