package kr.puzi.puzi.network.service;

import kr.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by muoe0 on 2017-07-09.
 */
public interface MyWorryReplyNetworkService {

	@GET("/myworry/reply/list")
	Call<ResponseVO> getReplyList(@Header("token") String token,
		@Query("myWorryQuestionId") int myWorryQuestionId,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/myworry/reply/write")
	Call<ResponseVO> write(@Header("token") String token,
		@Field("myWorryQuestionId") int myWorryQuestionId,
		@Field("comment") String comment);

	@FormUrlEncoded
	@POST("/myworry/reply/notify")
	Call<ResponseVO> notify(@Header("token") String token,
		@Field("myWorryReplyId") int myWorryReplyId);

	@FormUrlEncoded
	@POST("/myworry/reply/delete")
	Call<ResponseVO> delete(@Header("token") String token,
		@Field("myWorryReplyId") int myWorryReplyId);

}