package kr.puzi.puzi.biz.myworry;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by JangwonPark on 2018. 1. 13..
 */
@AllArgsConstructor
public enum QuestionType {
	LOW_PRICE("초저가형"),
	STANDARD("일반형"),
	LUXURY("고급형"),
	PREMIUM("프리미엄");

	@Getter
	private String comment;
}
