package kr.puzi.puzi.biz.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by JangwonPark on 2017. 9. 15..
 */
@AllArgsConstructor
public enum GenderType {

	MALE("남성"),
	FEMALE("여성");

	@Getter
	private String comment;
}
