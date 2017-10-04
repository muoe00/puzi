package com.puzi.puzi.utils;

import com.puzi.puzi.biz.channel.ChannelCategoryType;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by JangwonPark on 2017. 10. 4..
 */

public class SerializeUtils {

	public static ArrayList<String> convertToString(List<ChannelCategoryType> typeList) {
		ArrayList<String> result = newArrayList();
		for(ChannelCategoryType type : typeList) {
			result.add(type.name());
		}
		return result;
	}

	public static List<ChannelCategoryType> convertToType(List<String> stringList) {
		List<ChannelCategoryType> result = newArrayList();
		for(String string : stringList) {
			result.add(ChannelCategoryType.valueOf(string));
		}
		return result;
	}
}
