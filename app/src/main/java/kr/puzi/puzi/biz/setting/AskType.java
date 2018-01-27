package kr.puzi.puzi.biz.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by muoe0 on 2017-10-28.
 */

@AllArgsConstructor
public enum AskType {

	USE("사용문의"),
	ADVERTISEMENT("광고문의"),
	TIEUP("제휴문의"),
	ETC("기타문의");

	@Getter
	private String comment;

	public static AskType findByComment(@NonNull String comment) {
		for(AskType askType : values()) {
			if(askType.getComment().equals(comment)) {
				return askType;
			}
		}
		return null;
	}
}
