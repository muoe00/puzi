package com.puzi.puzi.network.service;

import com.puzi.puzi.biz.channel.ChannelCategoryType;
import com.puzi.puzi.biz.channel.ChannelVO;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;

import java.util.Arrays;
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
		Call<ResponseVO> call = sut.channelList(TOKEN, Arrays.asList(ChannelCategoryType.BEAUTY, ChannelCategoryType.GAME), null, 1);

		ResponseVO responseVO = NetworkTestValidation.call(call);

		List<ChannelVO> channelList = responseVO.getList("channelDTOList", ChannelVO.class);
		System.out.println("+++ channelList : " + channelList.toString());

		for(ChannelVO channelVO : channelList) {
			System.out.println("+++ channelVO : " + channelVO.toString());
		}
	}

	@Test
	public void channelEditorsPageList() throws Exception {
		Call<ResponseVO> call = sut.channelEditorsPageList(TOKEN, Arrays.asList(ChannelCategoryType.BEAUTY, ChannelCategoryType.GAME), 1);

		ResponseVO responseVO = NetworkTestValidation.call(call);

//		ChannelVO channelVO = responseVO.getValue("channelEditorsPageDTOList", ChannelEditors.class);
//		System.out.println("+++ channelVO : " + channelVO.toString());
	}

	@Test
	public void channelDetail() throws Exception {
		Call<ResponseVO> call = sut.channelDetail(TOKEN, 2);

		ResponseVO responseVO = NetworkTestValidation.call(call);

		ChannelVO channelVO = responseVO.getValue("channelDTO", ChannelVO.class);
		System.out.println("+++ channelVO : " + channelVO.toString());
	}

}