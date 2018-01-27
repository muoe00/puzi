package kr.puzi.puzi.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

	public static boolean checkUserId(String userId) {
		int length = userId.length();
		if(!checkNull(userId) || length < 6 || length > 15){
			return false;
		} else
			return true;
	}

	public static boolean checkEmail(String email)  {
		String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		if(!checkNull(email) || !Pattern.matches(regex, email.trim())){
			return false;
		} else
			return true;
	}

	public static boolean isValidPasswd(String pw) {
		String regex = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,15}$";
		if(!checkNull(pw) || !Pattern.matches(regex, pw.trim())){
			return false;
		} else
			return true;

	}

	public static boolean checkNull(Object object) {
		return object != null;
	}
}
