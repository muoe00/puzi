package kr.puzi.puzi.network.service;

import kr.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by JangwonPark on 2017. 10. 2..
 */

public class NetworkTestValidation {

	public static ResponseVO call(Call<ResponseVO> call) throws Exception {
		Response<ResponseVO> response = call.execute();

		if(!response.isSuccessful()) {
			System.out.println("+++ ERROR : " + response.errorBody().string());
			throw new Exception();
		}

		return response.body();
	}
}
