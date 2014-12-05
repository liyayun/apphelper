package com.lee.appmanager.util;

import java.util.ArrayList;
import java.util.List;

import com.lee.appmanager.domain.AppInfo;

/**
 * @author liyayun
 * @date 2014-11-30 下午6:52:46  
 */
public class DiffOperation {
	
	/**
	 * @param apps
	 * @param packages
	 * @return 获得包含指定报名的list
	 */
	public static List<AppInfo> fetchLock(List<AppInfo> apps ,List<String> packages){
		List<AppInfo> list=new ArrayList<AppInfo>();
		for (String packname : packages) {
			for (int i = 0; i < apps.size(); i++) {
				AppInfo app=apps.get(i);
				if(packname.equals(app.getPackName())){
					list.add(app);
				}
			}
		}
		
		return list;
	}
	
	/**
	 * @param apps
	 * @param packages
	 * @return取得不包含packages集合中所包含的的包名的apps
	 */
	public static List<AppInfo> fetchUnlock(List<AppInfo> apps ,List<String> packages){
		for (String packname : packages) {
			for (int i = 0; i < apps.size(); i++) {
				AppInfo app=apps.get(i);
				if(packname.equals(app.getPackName())){
					apps.remove(i);
				}
			}
		}
		
		return apps;
	}
	
}
