package com.puzi.puzi.network;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ResponseVO {

	private int resultCode;
	private Gson gson = new Gson();

	public String getResultMsg() {
		return resultMsg;
	}

	private String resultMsg;
	private Map params;

	public int getResultCode() {
		return resultCode;
	}

	public int getInteger(String key) {
		return (int) params.get(key);
	}

	public <T> T getValue(String key, Class<T> classOfT) {
		LinkedTreeMap map = (LinkedTreeMap) params.get(key);
		return gson.fromJson(gson.toJson(map), classOfT);
	}

	public <T> List<T> getList(String key, Class<T> classOfT) {
		List<T> result = new ArrayList<T>();
		List<LinkedTreeMap> list = (List) params.get(key);
		for(LinkedTreeMap map : list) {
			result.add(gson.fromJson(gson.toJson(map), classOfT));
		}
		return result;
	}

	public ResultType getResultType(){
		return ResultType.findBy(resultCode);
	}
}

