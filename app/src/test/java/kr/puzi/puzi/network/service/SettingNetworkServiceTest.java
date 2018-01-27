package kr.puzi.puzi.network.service;

import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.RetrofitManager;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 170605 on 2017-11-01.
 */

public class SettingNetworkServiceTest {

	SettingNetworkService sut;

	@Before
	public void setUp() throws Exception {
		this.sut = RetrofitManager.create(SettingNetworkService.class);
	}

	@Test
	public void list() throws Exception {
		System.out.println("+++ START");

		Call<ResponseVO> call = sut.list("test123123123", 1);
		ResponseVO responseVO = NetworkTestValidation.call(call);

	}

	@Test
	public void updateFavorites() throws Exception {
		System.out.println("+++ START");

		List<String> favoriteTypeList = new ArrayList<>();
		favoriteTypeList.add("BEAUTY");

		Call<ResponseVO> call = sut.updateFavorites("test123123123", favoriteTypeList);
		ResponseVO responseVO = NetworkTestValidation.call(call);

	}

	@Test
	public void updateAccount() throws Exception {
		System.out.println("+++ START");

		Call<ResponseVO> call = sut.updateAccount("test123123123", "a", "bbb@gmail.com");
		ResponseVO responseVO = NetworkTestValidation.call(call);
	}

	@Test
	public void company() throws Exception {
		System.out.println("+++ START");

		Call<ResponseVO> call = sut.blockCompany("test123123123", true, 1);
		ResponseVO responseVO = NetworkTestValidation.call(call);

	}

	@Test
	public void ask() throws Exception {
		System.out.println("+++ START");

		Call<ResponseVO> call = sut.ask("test123123123", "a", "b", "ccc@gmail.com", "d", "e");
		ResponseVO responseVO = NetworkTestValidation.call(call);

	}

	@Test
	public void out() throws Exception {
		System.out.println("+++ START");

		Call<ResponseVO> call = sut.out("test123123123");
		ResponseVO responseVO = NetworkTestValidation.call(call);

	}
}
