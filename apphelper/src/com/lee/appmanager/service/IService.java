package com.lee.appmanager.service;

/**
 * @author liyayun
 * @date 2014-11-30 обнГ6:51:18  
 */
public interface IService {
	public void callAppProtectStart(String packname);
	public void callAppProtectStop(String packname);
	public void killTask();
	
}
