package kr.puzi.puzi.biz.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by 170605 on 2017-10-27.
 */

@AllArgsConstructor
public enum CouponStatusType {

	NOT_USE(000, "미사용"),
	USED(001, "사용"),
	CANCEL(100, "취소"),
	UNKNOWN(999, "알수없음");

	@Getter
	private int index;
	@Getter
	private String comment;
}