package com.puzi.puzi.network;

import android.app.Activity;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Callback;

/**
 * Created by JangwonPark on 2017. 11. 10..
 */
@Getter
public abstract class LazyCallback implements Callback<ResponseVO> {

	protected Activity savedActivity;
	@Setter
	private LazyRequestService service;

	public LazyCallback(Activity activity) {
		this.savedActivity = activity;
	}

}
