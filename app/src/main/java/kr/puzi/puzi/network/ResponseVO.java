package kr.puzi.puzi.network;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import kr.puzi.puzi.utils.GsonUtils;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ResponseVO {

	private int resultCode;
	private String resultMsg;
	private Map params;

	public int getInteger(String key) {
		return ((Double) params.get(key)).intValue();
	}

	public boolean getBoolean(String key) {
		return ((boolean) params.get(key));
	}

	public String getString(String key) {
		return (String) params.get(key);
	}

	public <T> T getValue(String key, Class<T> classOfT) {
		Gson gson = GsonUtils.getGson();
		LinkedTreeMap map = (LinkedTreeMap) params.get(key);
		return gson.fromJson(gson.toJson(map), classOfT);
	}

	public <T> List<T> getList(String key, Class<T> classOfT) {
		Gson gson = GsonUtils.getGson();
		List<T> result = new ArrayList<T>();
		List<LinkedTreeMap> list = (List) params.get(key);
		if(list == null) {
			return new ArrayList<>();
		}
		for(LinkedTreeMap map : list) {
			result.add(gson.fromJson(gson.toJson(map), classOfT));
		}
		return result;
	}

	public ResultType getResultType(){
		return ResultType.findBy(resultCode);
	}

	public Map<String, Integer> getIntegerMap(String key) {
		Type type = new TypeToken<Map<String, Integer>>(){}.getType();
		Gson gson = GsonUtils.getGson();
		return gson.fromJson(String.valueOf(params.get(key)), type);
	}
}


