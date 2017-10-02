package com.puzi.puzi.network.service;

import com.puzi.puzi.network.RetrofitManager;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by muoe0 on 2017-10-02.
 */
public class AdvertisementNetworkServiceTest {

	AdvertisementNetworkService sut;

	@Before
	public void setUp() throws Exception {
		this.sut = RetrofitManager.create(AdvertisementNetworkService.class);
	}

	@Test
	public void adList() throws Exception {
		System.out.println("+++ START");

		/*Call<ResponseVO> call = sut.adList("test123123123", 1);
		Response<ResponseVO> response = call.execute();

		if(!response.isSuccessful()) {
			System.out.println("+++ ERROR : " + response.message());
			throw new Exception();
		}

		List<ReceivedAdvertiseVO> receivedAdvertiseVOs = response.body().getValue("receivedAdvertiseDTOList");

		System.out.println("response.body() : " + response.body());

		for(ReceivedAdvertiseVO receivedAdvertiseVO : receivedAdvertiseVOs) {
			System.out.println("+++ receivedAdvertiseVO : " + receivedAdvertiseVO.toString());
		}*/
	}
}