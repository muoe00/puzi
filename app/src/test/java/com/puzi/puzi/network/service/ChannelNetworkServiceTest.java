package com.puzi.puzi.network.service;

import com.puzi.puzi.biz.channel.ChannelCategoryType;
import com.puzi.puzi.biz.channel.ChannelEditorsPageVO;
import com.puzi.puzi.biz.channel.ChannelReplyVO;
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

		List<ChannelEditorsPageVO> channelEditorsPageList = responseVO.getList("channelEditorsPageDTOList", ChannelEditorsPageVO.class);
		System.out.println("+++ channelEditorsPageList : " + channelEditorsPageList.toString());

		for(ChannelEditorsPageVO channelEditorsPageVO : channelEditorsPageList) {
			System.out.println("+++ channelEditorsPageVO : " + channelEditorsPageVO.toString());
		}
	}

	@Test
	public void channelDetail() throws Exception {
		Call<ResponseVO> call = sut.channelDetail(TOKEN, 2);

		ResponseVO responseVO = NetworkTestValidation.call(call);

		ChannelVO channelVO = responseVO.getValue("channelDTO", ChannelVO.class);
		System.out.println("+++ channelVO : " + channelVO.toString());
	}

	@Test
	public void channelDetailEditorsPageList() throws Exception {
		Call<ResponseVO> call = sut.channelDetailEditorsPageList(TOKEN, 170);

		ResponseVO responseVO = NetworkTestValidation.call(call);

		List<ChannelEditorsPageVO> channelEditorsPageVOList = responseVO.getList("channelEditorsPageDTOList", ChannelEditorsPageVO.class);
		System.out.println("+++ channelEditorsPageVOList : " + channelEditorsPageVOList.toString());

		for(ChannelEditorsPageVO channelEditorsPageVO : channelEditorsPageVOList) {
			System.out.println("+++ channelEditorsPageVO : " + channelEditorsPageVO.toString());
		}
	}

	@Test
	public void replyList() throws Exception {
		Call<ResponseVO> call = sut.replyList(TOKEN, 1, 1);

		ResponseVO responseVO = NetworkTestValidation.call(call);

		List<ChannelReplyVO> channelReplyVOList = responseVO.getList("channelReplyDTOList", ChannelReplyVO.class);
		System.out.println("+++ channelReplyVOList : " + channelReplyVOList.toString());

		for(ChannelReplyVO channelReplyVO : channelReplyVOList) {
			System.out.println("+++ channelReplyVO : " + channelReplyVO.toString());
		}
	}

	@Test
	public void evaludate() throws Exception {
		Call<ResponseVO> call = sut.evaludate(TOKEN, 1, 5, "하하하하");

		ResponseVO responseVO = NetworkTestValidation.call(call);

		System.out.println("+++ resultCode : " + responseVO.getResultCode());
	}

	@Test
	public void replyWrite() throws Exception {
		Call<ResponseVO> call = sut.replyWrite(TOKEN, 1, "호호호");

		ResponseVO responseVO = NetworkTestValidation.call(call);

		System.out.println("+++ resultCode : " + responseVO.getResultCode());
	}

	@Test
	public void replyDelete() throws Exception {
		Call<ResponseVO> call = sut.replyDelete(TOKEN, 1, 10);

		ResponseVO responseVO = NetworkTestValidation.call(call);

		System.out.println("+++ resultCode : " + responseVO.getResultCode());
	}

	@Test
	public void replyRecommend() throws Exception {
		Call<ResponseVO> call = sut.replyRecommend(TOKEN, 1, 10, false);

		ResponseVO responseVO = NetworkTestValidation.call(call);

		System.out.println("+++ resultCode : " + responseVO.getResultCode());
	}

	@Test
	public void replyNotify() throws Exception {
		Call<ResponseVO> call = sut.replyNotify(TOKEN, 1, 10, "호호호");

		ResponseVO responseVO = NetworkTestValidation.call(call);

		System.out.println("+++ resultCode : " + responseVO.getResultCode());
	}
}