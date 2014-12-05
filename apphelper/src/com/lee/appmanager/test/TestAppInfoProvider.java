package com.lee.appmanager.test;

import com.lee.appmanager.business.AppInfoProvider;

import android.test.AndroidTestCase;

/**
 * @author liyayun
 * @date 2014-11-30 обнГ6:51:41  
 */
public class TestAppInfoProvider extends AndroidTestCase{
	public void getApps() throws Exception{
		AppInfoProvider provider = new AppInfoProvider(getContext());
		provider.getAllApps();
	}
}
