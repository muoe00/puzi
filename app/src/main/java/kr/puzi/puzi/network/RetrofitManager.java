package kr.puzi.puzi.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by muoe0 on 2017-04-28.
 */

public class RetrofitManager {

	public static <T> T create(Class<T> c){

		return new Retrofit.Builder()
			.baseUrl(FixValues.baseURL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(c);
	}

}