package com.puzi.puzi.ui;

import android.app.Activity;

/**
 * Created by muoe0 on 2017-04-28.
 */

public class ProgressDialog {

	private static android.app.ProgressDialog dialog = null;

	private static int showCount = 0;

	public static void show(Activity activity){
		if(showCount == 0){
			++showCount;
			dialog = android.app.ProgressDialog.show(activity, "", "연결중입니다...", true);
		} else {
			++showCount;
		}
	}

	public static void dismiss(){
		if(showCount >= 2){
			--showCount;
		} else {
			showCount = 0;
			if(dialog != null){
				dialog.dismiss();
			}
		}

	}
}
