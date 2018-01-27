package kr.puzi.puzi.cache;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.utils.PuziUtils;

/**
 * Created by muoe0 on 2017-04-28.
 */

public class Preference {

	private static final String MY_INFO = "MY_INFO";
	private static Gson gson = new Gson();

	public static void addProperty(Activity activity, String key, String value) {
		SharedPreferences.Editor editor = getEditor(activity);
		editor.putString(key, value);
		editor.commit();
	}

	public static void removeProperty(Activity activity, String key) {
		SharedPreferences.Editor editor = getEditor(activity);
		editor.remove(key);
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

	public static void saveMyInfo(Activity activity, UserVO myInfo) {
		removeProperty(activity, MY_INFO);
		addProperty(activity, MY_INFO, gson.toJson(myInfo));
	}
	
	public static UserVO getMyInfo(Activity activity) {
		String json = getProperty(activity, MY_INFO);
		if(json == null){
			return null;
		}
		return gson.fromJson(json, UserVO.class);
	}

	public static void updateMyInfoPlusPoint(Activity activity, int plusPoint) {
		UserVO myInfo = Preference.getMyInfo(activity);
		myInfo.setPoint(myInfo.getPoint() + plusPoint);
		Preference.saveMyInfo(activity, myInfo);
	}

	public static void updateMyInfoMinusPoint(Activity activity, int minusPoint) {
		UserVO myInfo = Preference.getMyInfo(activity);
		myInfo.setPoint(myInfo.getPoint() - minusPoint);
		Preference.saveMyInfo(activity, myInfo);
	}

}
