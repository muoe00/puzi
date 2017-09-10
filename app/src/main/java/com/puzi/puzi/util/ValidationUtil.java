package com.puzi.puzi.util;

import com.puzi.puzi.model.UserVO;

import java.util.List;
import java.util.regex.Pattern;

public class ValidationUtil {
	public static void checkUser(UserVO userVO)  {
		checkUserId(userVO.getUserId());
		checkEmail(userVO.getEmail());
		checkUserFavorites(userVO.getFavoriteTypeList());
	}

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

	public static boolean checkUserFavorites(List<String> favoriteList) {
		/*if(!checkNull(favoriteList) || favoriteList.length() != 7){
			return false;
		} else*/
			return true;
	}

	public static boolean checkToken(String token){
		return checkNull(token) && token.length() > 10;
	}

	public static boolean checkNull(Object object) {
		return object != null;
	}
}
