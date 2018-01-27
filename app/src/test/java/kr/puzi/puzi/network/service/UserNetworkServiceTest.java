package kr.puzi.puzi.network.service;

import kr.puzi.puzi.biz.user.UserVO;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.RetrofitManager;
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

	@Test
	public void login() throws Exception {
		System.out.println("+++ START");

		Call<ResponseVO> call = sut.login("muoe01", "00558a039edccbe7912a9e6f5eaafb1a8764f37ccc6e768e62b70a44162331b3", "b", "A", "d");
		ResponseVO responseVO = NetworkTestValidation.call(call);

		String token = responseVO.getString("token");

		System.out.println("token : " + token);
	}
}