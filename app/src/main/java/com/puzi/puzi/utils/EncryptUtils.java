package com.puzi.puzi.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by muoe0 on 2017-04-28.
 */

public class EncryptUtils {

	private static String SALT_FOR_SHA256 = "cs0604";

	public static String sha256(String target){
		return encrypt("SHA-256", SALT_FOR_SHA256, target);
	}

	public static String md5(String target, String salt){
		return encrypt("MD5", salt, target);
	}

	private static String encrypt(String type, String salt, String target){
		String encrypted = "";
		MessageDigest sh = null;
		try{
			sh = MessageDigest.getInstance(type);
		}catch(NoSuchAlgorithmException e){
			Log.e("NoSuchAlgorithmExceptio", "n" + e);
			return null;
		}

		sh.update((target+salt).getBytes());
		byte byteData[] = sh.digest();
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < byteData.length ; i++){
			sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
		}
		encrypted = sb.toString();

		return encrypted;
	}
}
