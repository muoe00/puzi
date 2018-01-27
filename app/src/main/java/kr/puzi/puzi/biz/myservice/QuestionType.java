package kr.puzi.puzi.biz.myservice;

import lombok.AllArgsConstructor;

/**
 * Created by muoe0 on 2018-01-07.
 */

@AllArgsConstructor
public enum QuestionType {

	LOW_PRICE("초저가형"),
	STANDARD("일반형"),
	LUXURY("고급형"),
	PREMIUM("프리미엄");

	private String comment;

}
