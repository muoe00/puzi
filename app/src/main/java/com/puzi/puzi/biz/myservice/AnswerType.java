package com.puzi.puzi.biz.myservice;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;

/**
 * Created by 170605 on 2018-01-19.
 */

@AllArgsConstructor
public enum AnswerType {

	RECENTLY("최신순"),
	OLD("오래된순"),
	LIKE("좋아요순");

	public String comment;
	public String getComment() {
		return this.comment;
	}

	public static List<AnswerType> getList() {

		List<AnswerType> answerTypeList = Arrays.asList(AnswerType.values());

		return answerTypeList;
	}
}
