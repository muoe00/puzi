package com.puzi.puzi.network.service;

import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;

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

		Call<ResponseVO> call = sut.myInfo("test123123123");
		ResponseVO responseVO = NetworkTestValidation.call(call);

		UserVO userVO = responseVO.getValue("userInfoDTO", UserVO.class);
		System.out.println("+++ userVO : " + userVO.toString());
	}
}