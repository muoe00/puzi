package com.puzi.puzi.ui.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

/**
 * Created by JangwonPark on 2017. 9. 13..
 */

public class BasicDialog {

	private static AlertDialog dialog = null;

	public static void show(Activity activity, String title, String message) {
		if(dialog != null && dialog.isShowing()){
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNeutralButton("닫기", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog = builder.show();
	}

}
