package com.puzi.puzi.network.service;

import com.puzi.puzi.biz.channel.ChannelVO;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

/**
 * Created by JangwonPark on 2017. 10. 2..
 */
public class ChannelNetworkServiceTest {

	ChannelNetworkService sut;
	String TOKEN = "test123123123";

	@Before
	public void setUp() throws Exception {
		this.sut = RetrofitManager.create(ChannelNetworkService.class);
	}

	@Test
	public void channelList() throws Exception {
		Call<ResponseVO> call = sut.channelList(TOKEN, "GAME", "POPULARITY", 1);

		ResponseVO responseVO = NetworkTestValidation.call(call);

		List<ChannelVO> channelList = responseVO.getValue("channelList", List.class);
		System.out.println("+++ channelList : " + channelList.toString());
	}

	@Test
	public void channelDetail() throws Exception {
		Call<ResponseVO> call = sut.channelDetail(TOKEN, 2);
		Response<ResponseVO> response = call.execute();
	}

}