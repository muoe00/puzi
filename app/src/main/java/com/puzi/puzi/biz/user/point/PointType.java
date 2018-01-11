package com.puzi.puzi.biz.user.point;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by JangwonPark on 2017. 9. 15..
 */
@AllArgsConstructor
public enum PointType {

	SIGNUP_POINT("회원가입"),
	ADVERTISEMENT_POINT("광고적립"),
	FRIEND_RECOMMEND_POINT("친구추천"),
	FIRST_PURCHASE_LIMIT_POINT("첫구매가능제한"),
	ITEM_PURCHASE("상품구매"),
	WITHDRAW("출금"),
	ADVERTISERMENT_VIEW_TIME("광고시청시간"),
	ITEM_CHALLENGE("푸지응모"),
	MY_TODAY_POINT("나의오늘은 적립금"),
	MY_WORRY_POINT("나의고민은 적립금"),
	MY_WORRY_REGISTER_POINT("나의고민은 등록"),
	SAVING("푸지적금");

	@Getter
	private String comment;

}
