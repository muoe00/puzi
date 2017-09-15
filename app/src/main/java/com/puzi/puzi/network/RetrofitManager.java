package com.puzi.puzi.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by muoe0 on 2017-04-28.
 */

public class RetrofitManager {

	private static final String baseURL = "http://dev.cashspace.net";

	public static <T> T create(Class<T> c){

		return new Retrofit.Builder()
			.baseUrl(baseURL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(c);
	}

}