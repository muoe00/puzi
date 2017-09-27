package com.puzi.puzi.network;

import android.util.Log;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class ResponseVO<T> {

	private int resultCode;
	private String resultMsg;
	private Map <String, T> params;

	public int getResultCode() {
		return resultCode;
	}

	public T getValue(String key) {
		Log.i("params", params.toString());
		return params.get(key);
	}

	public ResultType getResultType(){
		return ResultType.findBy(resultCode);
	}
}

