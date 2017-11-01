package com.puzi.puzi.biz.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by 170605 on 2017-11-01.
 */

@AllArgsConstructor
public enum StoreType {

	MOVIE("영화"),
	CONVENIENCE_STORE("편의점"),
	BAKERY("베이커리"),
	GIFT_CARD("상품권"),
	EAT_OUT("외식"),
	CAR_REFUEL("주유권"),
	CAFE("카페"),
	COSMETICS("화장품"),
	WITHDRAW("출금");

	@Getter
	private String comment;

	public boolean isWithdraw() {
		return this == WITHDRAW;
	}
}