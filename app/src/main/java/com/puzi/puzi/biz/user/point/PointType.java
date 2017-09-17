package com.puzi.puzi.biz.user.point;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by JangwonPark on 2017. 9. 15..
 */
@AllArgsConstructor
public enum PointType {

	SIGNUP("회원가입"),
	ADVERTISEMENT("광고적립"),
	FRIEND_RECOMMEND("친구추천"),
	FIRST_PURCHASE("첫구매가능한포인트"),
	ITEM_PURCHASE("상품구매"),
	WITHDRAW("출금");

	@Getter
	private String comment;

}
