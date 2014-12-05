package com.lee.appmanager.util;

import java.text.DecimalFormat;

/**
 * @author liyayun
 * @date 2014-11-30 ����6:53:15  
 */
public class TextFormater {

	
	/**
	 * ����byte�����ݴ�С��Ӧ���ı�
	 * @param size
	 * @return
	 */
	public static String getDataSize(long size){
		DecimalFormat formater = new DecimalFormat("####.00");
		if(size<1024){
			return size+"bytes";
		}else if(size<1024*1024){
			float kbsize = size/1024f;  
			return formater.format(kbsize)+"KB";
		}else if(size<1024*1024*1024){
			float mbsize = size/1024f/1024f;  
			return formater.format(mbsize)+"MB";
		}else{
			float gbsize = size/1024f/1024f/1024f;  
			return formater.format(gbsize)+"GB";
		}
		
		
		
	}
	
	/**
	 * ����kb�����ݴ�С��Ӧ���ı�
	 * @param size
	 * @return
	 */
	public static String getKBDataSize(long size){
	
		
		return  getDataSize(size*1024);
		
	}
}
