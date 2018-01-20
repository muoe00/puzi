package com.puzi.puzi.biz.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public enum LevelType {

	WELCOME("Welcome"),
	SILVER("Silver"),
	GOLD("Gold"),
	VIP("VIP"),
	VVIP("VVIP");

	@Getter
	private String comment;

	public static LevelType findByComment(@NonNull String comment) {
		for(LevelType levelType : values()) {
			if(levelType.getComment().equals(comment)) {
				return levelType;
			}
		}
		return null;
	}
}