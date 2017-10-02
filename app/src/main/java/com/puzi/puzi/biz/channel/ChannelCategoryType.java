package com.puzi.puzi.biz.channel;

import com.puzi.puzi.biz.user.AgeType;
import com.puzi.puzi.biz.user.FavoriteType;
import com.puzi.puzi.biz.user.GenderType;
import lombok.AllArgsConstructor;

/**
 * Created by JangwonPark on 2017. 10. 2..
 */
@AllArgsConstructor
public enum ChannelCategoryType {

	ALL(ChannelCategoryType.class, "전체"),
	BEAUTY(FavoriteType.class, FavoriteType.BEAUTY.getComment()),
	SHOPPING(FavoriteType.class, FavoriteType.SHOPPING.getComment()),
	GAME(FavoriteType.class, FavoriteType.GAME.getComment()),
	EAT(FavoriteType.class, FavoriteType.EAT.getComment()),
	TOUR(FavoriteType.class, FavoriteType.TOUR.getComment()),
	FINANCE(FavoriteType.class, FavoriteType.FINANCE.getComment()),
	CULTURE(FavoriteType.class, FavoriteType.CULTURE.getComment()),
	TEN(AgeType.class, AgeType.TEN.getComment()),
	TWENTY(AgeType.class, AgeType.TWENTY.getComment()),
	THIRTY(AgeType.class, AgeType.THIRTY.getComment()),
	FOURTY(AgeType.class, AgeType.FOURTY.getComment()),
	MALE(GenderType.class, GenderType.MALE.getComment()),
	FEMALE(GenderType.class, GenderType.FEMALE.getComment());

	private Class parentClass;
	private String comment;
}
