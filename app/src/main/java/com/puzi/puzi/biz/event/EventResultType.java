package com.puzi.puzi.biz.event;

import lombok.AllArgsConstructor;

/**
 * Created by JangwonPark on 2018. 1. 17..
 */
@AllArgsConstructor
public enum EventResultType {
	NONE("요청전"),
	REQUEST("요청완료"),
	COMPLETED("처리완료");

	private String comment;
}
