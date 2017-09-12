package com.puzi.puzi.model;

import android.util.Log;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class ResponseVO<T> {
	private int resultCode;
	private String resultMsg;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public Map<String, T> getParams() {
		return params;
	}

	public void setParams(Map<String, T> params) {
		this.params = params;
	}

	private Map <String, T> params;

	public T getValue(String key) {
		Log.i("params", params.toString());
//		return new Gson().fromJson(, );
		return params.get(key);
	}

//	public List getList(String key, Type type) {
//		List sub = (List)params.get(key);
//		Log.e("sub:", sub.toString());
//		return new Gson().fromJson(sub.toString(), type);
//	}

//	public List test(String key, Type type) {
//		List sub = new Gson().fromJson(params.get(key), List.class);
//		String json = new Gson().toJson(sub, type);
//		return new Gson().fromJson(json, type);
//	}
}

