package com.puzi.puzi.biz.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by JangwonPark on 2017. 9. 15..
 */
@AllArgsConstructor
public enum FavoriteType {

	BEAUTY("뷰티"),
	SHOPPING("쇼핑"),
	GAME("게임"),
	EAT("외식업"),
	TOUR("여행"),
	FINANCE("금융"),
	CULTURE("문화");

	@Getter
	private String comment;
}
