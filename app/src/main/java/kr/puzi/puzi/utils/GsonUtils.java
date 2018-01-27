package kr.puzi.puzi.utils;

import com.google.gson.Gson;

/**
 * Created by JangwonPark on 2017. 10. 2..
 */

public class GsonUtils {

	private static Gson gson = new Gson();

	public static Gson getGson() {
		return gson;
	}
}
