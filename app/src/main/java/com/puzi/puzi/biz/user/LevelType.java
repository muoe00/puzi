package com.puzi.puzi.biz.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LevelType {

	BRONZE("브론즈"),
	SILVER("실버"),
	GOLD("골드"),
	PLATINUM("플레티넘"),
	DIAMOND("다이아몬드");

	@Getter
	private String comment;
}