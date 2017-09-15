package com.puzi.puzi.biz.user;

/**
 * Created by JangwonPark on 2017. 9. 15..
 */

public enum RegisterType {

	N("일반가입"),
	K("카카오가입");

	private String comment;

	RegisterType(String comment){
		this.comment = comment;
	}
}
