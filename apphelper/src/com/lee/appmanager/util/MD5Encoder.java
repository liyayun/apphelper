package com.lee.appmanager.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author liyayun
 * @date 2014-11-30 обнГ6:53:06  
 */
public class MD5Encoder{
	
	public static String md5Encode(String str){
		try {
			MessageDigest digest=MessageDigest.getInstance("MD5");
			byte[] bytes=digest.digest(str.getBytes());
			StringBuffer sb=new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				String s=Integer.toHexString(0xff&bytes[i]);
				if(s.length()==1){
					sb.append("0"+s);
				}else{
					sb.append(s);
				}
			}
			
			return sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("MD5EncoderException");
		}
	}
}
