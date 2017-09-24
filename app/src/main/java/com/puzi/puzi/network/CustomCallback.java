package com.puzi.puzi.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import com.puzi.puzi.ui.ProgressDialog;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by muoe0 on 2017-04-28.
 */

public abstract class CustomCallback<T> implements Callback<T> {

	private static AlertDialog dialog = null;

	private Activity activity;

	public CustomCallback(Activity activity) {
		this.activity = activity;
	}

	public abstract void onSuccess(T t);

	@Override
	public void onResponse(final Call<T> call, retrofit2.Response<T> response) {
		Log.d("retrofit2Response", "" + response.body());

		Request request = call.request();
		Log.d("CustomCallback", "####### REQUEST #######");
		//HEADER
		for (String name : request.headers().names()) {
			Log.d("REQUEST", "## Header - " + name + " : " + request.header(name));
		}
		//BODY
		Log.d("REQUEST", "## " + request.toString());

		ProgressDialog.dismiss();

		if(response.isSuccessful()){
			Log.d("CustomCallback", "####### RESPONSE ######");
			Log.d("CustomCallback", response.body().toString());
			onSuccess(response.body());
		} else {
			Log.d("errorbody : ", "" + response.errorBody());
			retryAlert("통신 실패", "통신에 실패하였습니다["+response.code()+"]\n잠시 후 다시 시도해주시기 바랍니다.", call);
		}
	}

	@Override
	public void onFailure(final Call<T> call, Throwable t) {
		// 통신 오류
		Log.e("TAG", "통신 오류", t);
		retryAlert("통신 실패", "통신에 실패하였습니다. 네트워크 상태를 다시 한번 확인해주시기 바랍니다.", call);
	}

	private void retryAlert(String title, String comment, final Call<T> call) {
		if(dialog != null && dialog.isShowing()) {
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(comment);
		builder.setNegativeButton("닫기", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setPositiveButton("재시도", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				retry(call);
			}
		});
		dialog = builder.show();
	}

	private void retry(Call<T> call){
		ProgressDialog.show(activity);
		call.clone().enqueue(this);
	}
}