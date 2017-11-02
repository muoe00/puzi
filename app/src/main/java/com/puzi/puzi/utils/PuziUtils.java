package com.puzi.puzi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.ui.IntroActivity;

import java.util.UUID;

/**
 * Created by 170605 on 2017-09-26.
 */

public class PuziUtils {

	public static final String PUZI = "puzi";
	public static final String INFO = "INFO";
	public static final int VIEW_POINT = 0;
	public static final int VIEW_LEVEL = 1;

	public static void renewalToken(Activity activity) {
		Preference.removeProperty(activity, "token");
		activity.startActivity(new Intent(activity, IntroActivity.class));
		activity.finish();
	}

	public static String getDevicesUUID(Context mContext){
		final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();
		return deviceId;
	}

}
