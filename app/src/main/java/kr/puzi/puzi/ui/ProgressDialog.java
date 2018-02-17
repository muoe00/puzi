package kr.puzi.puzi.ui;

import android.app.Activity;

/**
 * Created by muoe0 on 2017-04-28.
 */

public class ProgressDialog {

	private static android.app.ProgressDialog dialog = null;

	public static void show(Activity activity){
		if(dialog == null || !dialog.isShowing()) {
			if(!activity.isFinishing()) {
				dialog = android.app.ProgressDialog.show(activity, "", "연결중입니다...", true);
			}
		}
	}

	public static void dismiss(){
		if(dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
