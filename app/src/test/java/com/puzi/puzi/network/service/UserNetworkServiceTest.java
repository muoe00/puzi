package com.puzi.puzi.network.service;

import com.puzi.puzi.network.RetrofitManager;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by JangwonPark on 2017. 10. 2..
 */
public class UserNetworkServiceTest {

	UserNetworkService sut;

	@Before
	public void setUp() {
		this.sut = RetrofitManager.create(UserNetworkService.class);
	}

	@Test
	public void myInfo() throws Exception {
		System.out.println("+++ START");
/*
		Call<ResponseVO<UserVO>> call = sut.myInfo("test123123123");
		Response<ResponseVO<UserVO>> response = call.execute();

		if(!response.isSuccessful()) {
			System.out.println("+++ ERROR : " + response.message());
			throw new Exception();
		}

		UserVO userVO = response.body().getValue("userInfoDTO");
		System.out.println("+++ userVO : " + userVO.toString());*/
	}
}