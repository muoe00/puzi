package com.puzi.puzi.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.puzi.puzi.cache.Preference;
import com.puzi.puzi.network.service.UserNetworkService;
import com.puzi.puzi.ui.IntroActivity;
import com.puzi.puzi.ui.ProgressDialog;
import com.puzi.puzi.ui.base.BaseFragmentActivity;
import okhttp3.Request;
import retrofit2.Call;

import java.util.HashSet;
import java.util.Set;

import static com.puzi.puzi.network.ResultType.LOGIN_FAIL;

/**
 * Created by muoe0 on 2017-04-28.
 */

public abstract class CustomCallback extends LazyCallback {

	private static Set<CustomCallback> retrySet = new HashSet<>();
	private static AlertDialog dialog = null;
	private static boolean logining = false;

	public CustomCallback(Activity activity) {
		super(activity);
	}

	public abstract void onSuccess(ResponseVO responseVO);

	public void onFail(ResponseVO responseVO) {
		// @Override if use
		Toast.makeText(savedActivity, responseVO.getResultMsg(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResponse(final Call<ResponseVO> call, retrofit2.Response<ResponseVO> response) {
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

			if(response.body().getResultType().isNoAuth()) {
				saveRetry();
				if(!logining){
					tryLogin();
				}
			} else if(response.body().getResultType().isSuccess()) {
				onSuccess(response.body());
			} else {
				if(response.body().getResultCode() == LOGIN_FAIL.getResultCode()) {
					Preference.addProperty(getSavedActivity(), "id", null);
					Preference.addProperty(getSavedActivity(), "passwd", null);
					tryLogin();
				}
				onFail(response.body());
			}
		} else {
			Log.d("errorbody : ", "" + response.errorBody());
			retryProcess("통신 실패", "통신에 실패하였습니다\n잠시 후 다시 시도해주시기 바랍니다.");
		}
	}

	private void tryLogin() {
		logining = true;
		ProgressDialog.show(savedActivity);

		final String id = Preference.getProperty(savedActivity, "id");
		final String pw = Preference.getProperty(savedActivity, "passwd");
		final String notifyId = Preference.getProperty(savedActivity, "tokenFCM");

		UserNetworkService userNetworkService = RetrofitManager.create(UserNetworkService.class);

		Call<ResponseVO> call = userNetworkService.login(id, pw, notifyId, "A", "");
		call.enqueue(new CustomCallback(savedActivity) {
			@Override
			public void onSuccess(ResponseVO responseVO) {

				ProgressDialog.dismiss();
				ResultType resultCode = responseVO.getResultType();

				switch(resultCode){
					case SUCCESS:
						String token = responseVO.getString("token");
						if(token != null) {
							Preference.addProperty(savedActivity, "token", token);
						}
						retry();
						break;
					case LOGIN_FAIL:
						BaseFragmentActivity.finishAll();
						savedActivity.startActivity(new Intent(savedActivity, IntroActivity.class));
						break;
					default:
						break;
				}
				logining = false;
			}
		});
	}

	@Override
	public void onFailure(final Call<ResponseVO> call, Throwable t) {
		// 통신 오류
		Log.e("TAG", "통신 오류", t);
		retryProcess("통신 실패", "통신에 실패하였습니다. 네트워크 상태를 다시 한번 확인해주시기 바랍니다.");
	}

	private void retryProcess(String title, String comment) {
		saveRetry();
		retryAlert(title, comment);
	}

	private void saveRetry() {
		retrySet.add(this);
	}

	private void retryAlert(String title, String comment) {
		if(dialog != null && dialog.isShowing()) {
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(savedActivity);
		builder.setTitle(title);
		builder.setMessage(comment);
		builder.setNegativeButton("닫기", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ProgressDialog.dismiss();
			}
		});
		builder.setPositiveButton("재시도", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				retry();
			}
		});

		if(!savedActivity.isFinishing()) {
			dialog = builder.show();
		}
	}

	private void retry(){
		ProgressDialog.show(savedActivity);

		for(CustomCallback retry : retrySet) {
			if(retry.savedActivity.equals(savedActivity)) {
				if(!savedActivity.isFinishing()) {
					retry.getService().enqueue(retry);
				}
			}
		}

		retrySet.clear();
	}
}