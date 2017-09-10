package com.puzi.puzi.network;

import android.util.Log;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by muoe0 on 2017-04-28.
 */

public abstract class CustomCallback<T> implements Callback<T> {

	public abstract void onResponse(T t);

	public abstract void onFailure(PuziNetworkException e);

	@Override
	public void onResponse(Call<T> call, retrofit2.Response<T> response) {
		Log.d("retrofit2Response", response.body().toString());

		//TODO:log
		Request request = call.request();
		Log.d("CustomCallback", "####### REQUEST #######");
		//HEADER
		for (String name : request.headers().names()) {
			Log.d("REQUEST", "## Header - " + name + " : " + request.header(name));
		}
		//BODY
		Log.d("REQUEST", "## " + request.toString());

		if(response.isSuccessful()){
			Log.d("CustomCallback", "####### RESPONSE ######");
			Log.d("CustomCallback", response.body().toString());
			onResponse(response.body());
		} else {
			Log.d("errorbody : ", "" + response.errorBody());
			onFailure(new PuziNetworkException(response.code()));
		}
	}

	@Override
	public void onFailure(Call<T> call, Throwable t) {
		// 통신 오류
		Log.e("TAG", "통신 오류", t);
		onFailure(new PuziNetworkException(t));
	}
}