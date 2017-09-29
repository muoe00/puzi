package com.puzi.puzi.network;

import lombok.Getter;

/**
 * Created by muoe0 on 2017-04-28.
 */

public enum ResultType {

	SUCCESS(1000, "성공"),
	NO_AUTH(2000, "권한이 없습니다."),
	LOGIN_FAIL(2001, "로그인에 실패하였습니다."),
	DUPLICATED_ID(2002, "중복된 아이디입니다."),
	NOT_FOUND_USER(2003, "등록되지 않은 사용자입니다."),
	OVERDUE_SAVE(2004, "적립기한이 지났습니다."),
	ALREADY_SAVED(2005, "이미 적립되었습니다."),
	NOT_FOUNT_RECOMMEND(2006, "등록되지 않은 추천인입니다."),
	ALREADY_WROTE(2007, "이미 평가를 작성하였습니다."),
	WRONG_SCORE(2008, "점수 범위가 벗어났습니다."),
	ALREADY_INCLUDED(2009, "이미 특정시간이 포함되어 있습니다."),
	NO_AUTH_DELETE(2010, "삭제권한이 없습니다."),
	NO_FIRST_PURCHASE(2011, "첫구매포인트가 부족합니다."),
	LACK_POINT(2012, "포인트가 부족합니다."),
	OVER_QUANTITY(2013, "환불 수량이 더 많습니다."),
	NO_CHANNEL(2014, "존재하지 않는 채널입니다."),
	ALREADY_SIGNUP_WITH_PHONEKEY(2015, "이미 해당 핸드폰으로 가입하셨습니다."),
	WRONG_PARAMS(3000, "잘못된 파라미터 요청(#arg0#)"),
	ERROR_STORE(4000, "일시적으로 상품구매 연동 에러가 발생하였습니다. 잠시 후 다시 요청해 주시기 바랍니다.");

	@Getter
	private int resultCode;
	private String resultMsg;

	ResultType(int resultCode, String resultMsg) {
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	public static ResultType findBy(int resultCode) {
		for(ResultType resultType : ResultType.values()) {
			if(resultType.getResultCode() == resultCode) {
				return resultType;
			}
		}
		return null;
	}

	public boolean isSuccess() {
		return this == SUCCESS;
	}

	public int getResultCode() {
		return resultCode;
	}
}
