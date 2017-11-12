package com.puzi.puzi.biz.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by 170605 on 2017-10-27.
 */

@AllArgsConstructor
public enum CouponStatusType {

	NOT_USE("미사용상태"),
	USED("사용상태"),
	UNKNOWN("알수없음");

	@Getter
	private String comment;

	public boolean isNotUsed() {
		return this == NOT_USE;
		// return this == NOT_USE || this == UNKNOWN;
	}
}