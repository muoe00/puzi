package com.puzi.puzi.biz.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LevelType {

	WELCOME("Welcome"),
	SILVER("Silver"),
	GOLD("Gold"),
	VIP("VIP"),
	VVIP("VVIP");

	@Getter
	private String comment;
}