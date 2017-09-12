package com.puzi.puzi.network;

import lombok.Getter;

/**
 * Created by muoe0 on 2017-03-22.
 */

public class PuziNetworkException extends RuntimeException {

	@Getter
	private int code;

	public PuziNetworkException(Throwable t){
		super(t);
	}

	public PuziNetworkException(int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
