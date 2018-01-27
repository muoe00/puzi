package kr.puzi.puzi.biz.event;

import lombok.AllArgsConstructor;

/**
 * Created by JangwonPark on 2018. 1. 17..
 */
@AllArgsConstructor
public enum EventStatusType {
	ING("진행중"),
	COMPLETED("마감완료"),
	END("종료");

	private String comment;

	public boolean isShowEvent() {
		return this == ING || this == COMPLETED;
	}
}
