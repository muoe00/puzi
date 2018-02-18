package kr.puzi.puzi.biz.user.point;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by JangwonPark on 2017. 9. 15..
 */
@AllArgsConstructor
public enum PointType {

	SIGNUP_POINT("회원가입포인트"),
	ADVERTISEMENT_POINT("푸시광고 적립"),
	FRIEND_RECOMMEND_POINT("친구추천"),
	ITEM_PURCHASE("상품구매"),
	WITHDRAW("출금"),
	SAVING("푸지적금"),
	MY_TODAY_POINT("나의오늘은 적립"),
	MY_WORRY_POINT("나의고민은 적립"),
	ITEM_CHALLENGE("푸지응모 아이템 구매"),
	MY_WORRY_REGISTER_POINT("나의고민은 작성"),
	SLIDING("슬라이딩광고 적립");

	@Getter
	private String comment;

}
