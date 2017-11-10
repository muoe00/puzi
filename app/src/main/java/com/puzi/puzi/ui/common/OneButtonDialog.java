package com.puzi.puzi.ui.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

/**
 * Created by JangwonPark on 2017. 9. 13..
 */

public class OneButtonDialog {

	private static AlertDialog dialog = null;

	public static void show(Activity activity, String title, String message, String buttonName, final DialogButtonCallback callback) {
		if(dialog != null && dialog.isShowing()){
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNegativeButton("취소", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setPositiveButton(buttonName, new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onClick();
			}
		});
		dialog = builder.show();
	}

	public static void show(Activity activity, String title, String message, String buttonName, final DialogButtonCallback callback,
		final DialogButtonCallback failCallback) {
		if(dialog != null && dialog.isShowing()){
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNegativeButton("취소", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				failCallback.onClick();
			}
		});
		builder.setPositiveButton(buttonName, new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.onClick();
			}
		});
		dialog = builder.show();
	}
}
