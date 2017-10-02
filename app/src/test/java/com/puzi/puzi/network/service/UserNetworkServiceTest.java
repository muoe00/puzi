package com.puzi.puzi.network.service;

import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by JangwonPark on 2017. 10. 2..
 */
public class UserNetworkServiceTest {

	UserNetworkService sut;

	void setup() {
		this.sut = RetrofitManager.create(UserNetworkService.class);
	}

	@Test
	public void myInfo() throws Exception {
		System.out.println("+++ START");
		setup();

		Call<ResponseVO<UserVO>> call = sut.myInfo("test123123123");
		Response<ResponseVO<UserVO>> response = call.execute();

		if(!response.isSuccessful()) {
			System.out.println("+++ ERROR : " + response.message());
			throw new Exception();
		}

		UserVO userVO = response.body().getValue("userInfoDTO");
		System.out.println("+++ userVO : " + userVO.toString());
	}
}