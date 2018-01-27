package kr.puzi.puzi.network;

import android.app.Activity;
import kr.puzi.puzi.cache.Preference;
import retrofit2.Call;

/**
 * Created by JangwonPark on 2017. 11. 10..
 */

public class LazyRequestService<T> {

	private T t;
	private Activity activity;
	private RequestMothod<T> method;

	public LazyRequestService(Activity activity, Class<T> clazz) {
		this.activity = activity;
		this.t = RetrofitManager.create(clazz);
	}

	public LazyRequestService method(RequestMothod<T> method) {

		this.method = method;
		return this;
	}

	public void enqueue(CustomCallback callback) {
		Call<ResponseVO> call = method.execute(t, Preference.getProperty(activity, "token"));
		callback.setService(this);
		call.enqueue(callback);
	}

	public interface RequestMothod<T> {
		Call<ResponseVO> execute(T t, String token);
	}

}
