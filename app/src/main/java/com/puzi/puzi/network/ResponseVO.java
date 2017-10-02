package com.puzi.puzi.network;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class ResponseVO {

	private int resultCode;

	public String getResultMsg() {
		return resultMsg;
	}

	private String resultMsg;
	private Map params;

	public int getResultCode() {
		return resultCode;
	}

	public <R> R getValue(String key, Class<R> clazz) {
		return (R) params.get(key);
	}

	public ResultType getResultType(){
		return ResultType.findBy(resultCode);
	}
}

