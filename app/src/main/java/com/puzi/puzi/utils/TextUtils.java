package com.puzi.puzi.utils;

import java.text.NumberFormat;

/**
 * Created by JangwonPark on 2017. 11. 4..
 */

public class TextUtils {

	private static NumberFormat numberFormat = NumberFormat.getInstance();

	public static String addComma(int number) {
		return numberFormat.format(number);
	}
}
