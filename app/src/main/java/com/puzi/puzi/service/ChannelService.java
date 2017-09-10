package com.puzi.puzi.service;

import com.puzi.puzi.model.ChannelReply;
import com.puzi.puzi.model.ChannelVO;
import com.puzi.puzi.model.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-09.
 */

public interface ChannelService {

	@GET("/channel/list")
	Call<ResponseVO<List<ChannelVO>>> channelList(@Header("token") String token,
		@Query("searchType") String searchType,
		@Query("pagingIndex") int pagingIndex);

	@GET("/channel/detail")
	Call<ResponseVO<ChannelVO>> channelDetail(@Header("token") String token,
		@Query("channelId") int ChannelId);

	@FormUrlEncoded
	@POST("/channel/evaludate")
	Call<ResponseVO> evaludate(@Header("token") String token,
		@Field("channelId") int channelId,
		@Field("score") int score,
		@Field("comment") String comment);

	@GET("/channel/reply/list")
	Call<ResponseVO<List<ChannelReply>>> replyList(@Header("token") String token,
		@Query("channelId") int channelId,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/channel/reply/write")
	Call<ResponseVO<Integer>> replyWrite(@Header("token") String token,
		@Field("channelId") int channelId,
		@Field("comment") String comment);

	@FormUrlEncoded
	@POST("/channel/reply/delete")
	Call<ResponseVO> replyDelete(@Header("token") String token,
		@Field("channelId") int channelId,
		@Field("channelReplyId") int channelReplyId);

	@FormUrlEncoded
	@POST("/channel/reply/recommend")
	Call<ResponseVO> replyRecommend(@Header("token") String token,
		@Field("channelId") int channelId,
		@Field("channelReplyId") int channelReplyId,
		@Field("recommend") boolean recommend);

}