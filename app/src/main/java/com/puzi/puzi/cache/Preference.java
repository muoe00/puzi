package com.puzi.puzi.cache;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.puzi.puzi.utils.PuziUtils;

/**
 * Created by muoe0 on 2017-04-28.
 */

public class Preference {

	public static void addProperty(Activity activity, String key, String value) {
		SharedPreferences.Editor editor = getEditor(activity);
		editor.putString(key, value);
		editor.commit();
	}

	public static String getProperty(Activity activity, String key) {
		SharedPreferences sharedPreference = getSharedPreferences(activity);
		return sharedPreference.getString(key, null);
	}

	public static SharedPreferences.Editor getEditor(Activity activity) {
		SharedPreferences sharedPreference = getSharedPreferences(activity);
		return sharedPreference.edit();
	}

	public static SharedPreferences getSharedPreferences(Activity activity) {
		return activity.getSharedPreferences(PuziUtils.PUZI, Context.MODE_PRIVATE);
	}
}
