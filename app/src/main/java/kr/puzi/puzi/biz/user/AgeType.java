package kr.puzi.puzi.biz.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by JangwonPark on 2017. 9. 15..
 */
@AllArgsConstructor
public enum AgeType {

	TEN("10대"),
	TWENTY("20대"),
	THIRTY("30대"),
	FOURTY("40대이상");

	@Getter
	private String comment;
}
