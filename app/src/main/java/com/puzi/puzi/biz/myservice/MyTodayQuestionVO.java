package com.puzi.puzi.biz.myservice;

import android.support.annotation.Nullable;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by muoe0 on 2018-01-07.
 */

@Data
public class MyTodayQuestionVO implements Serializable {

	private int myTodayQuestionId;		// 나의오늘은질문시퀀스
	private String question;				// 질문
	private int answerCount;				// 답변개수
	private String answerOne;			// 첫번째 답변
	private String answerTwo;			// 두번째 답변
	@Nullable
	private String answerThree;			// 세번째 답변
	@Nullable
	private String answerFour;			// 네번째 답변
	private int savePoint;				// 적립예정포인트

}


