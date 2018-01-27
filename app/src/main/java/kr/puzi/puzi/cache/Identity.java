package kr.puzi.puzi.cache;

/**
 * Created by JangwonPark on 2017. 9. 15..
 */

public class Identity {

	private static String token = null;

	public static String getToken() {
		return token;
	}

	public static void saveToken(String token) {
		Identity.token = token;
	}

	public static boolean isValid() {
		return token != null;
	}
}
