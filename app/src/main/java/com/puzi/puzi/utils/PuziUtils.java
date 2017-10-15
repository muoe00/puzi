package com.puzi.puzi.utils;

import android.app.Activity;
import android.content.Intent;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.ui.IntroActivity;

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

}
