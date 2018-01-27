package kr.puzi.puzi.biz.channel;

import kr.puzi.puzi.biz.user.AgeType;
import kr.puzi.puzi.biz.user.FavoriteType;
import kr.puzi.puzi.biz.user.GenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Created by JangwonPark on 2017. 10. 2..
 */
@AllArgsConstructor
public enum ChannelCategoryType implements Serializable {

	BEAUTY(FavoriteType.class, FavoriteType.BEAUTY.getComment(), kr.puzi.puzi.R.id.btn_channel_filter_beauty),
	SHOPPING(FavoriteType.class, FavoriteType.SHOPPING.getComment(), kr.puzi.puzi.R.id.btn_channel_filter_shopping),
	GAME(FavoriteType.class, FavoriteType.GAME.getComment(), kr.puzi.puzi.R.id.btn_channel_filter_eat),
	EAT(FavoriteType.class, FavoriteType.EAT.getComment(), kr.puzi.puzi.R.id.btn_channel_filter_tour),
	TOUR(FavoriteType.class, FavoriteType.TOUR.getComment(), kr.puzi.puzi.R.id.btn_channel_filter_culture),
	FINANCE(FavoriteType.class, FavoriteType.FINANCE.getComment(), kr.puzi.puzi.R.id.btn_channel_filter_game),
	CULTURE(FavoriteType.class, FavoriteType.CULTURE.getComment(), kr.puzi.puzi.R.id.btn_channel_filter_finance),
	TEN(AgeType.class, AgeType.TEN.getComment(), kr.puzi.puzi.R.id.ibtn_channel_filter_10),
	TWENTY(AgeType.class, AgeType.TWENTY.getComment(), kr.puzi.puzi.R.id.ibtn_channel_filter_20),
	THIRTY(AgeType.class, AgeType.THIRTY.getComment(), kr.puzi.puzi.R.id.ibtn_channel_filter_30),
	FOURTY(AgeType.class, AgeType.FOURTY.getComment(), kr.puzi.puzi.R.id.ibtn_channel_filter_40),
	MALE(GenderType.class, GenderType.MALE.getComment(), kr.puzi.puzi.R.id.ibtn_channel_filter_male),
	FEMALE(GenderType.class, GenderType.FEMALE.getComment(), kr.puzi.puzi.R.id.ibtn_channel_filter_female);

	@Getter
	private Class parentClass;
	@Getter
	private String comment;
	@Getter
	private int resource;

	/**
	 * FavoriteType만 버튼타입으로 UI생성됨.
	 * @return
	 */
	public boolean isButtonType() {
		return this.getParentClass().equals(FavoriteType.class);
	}
}
