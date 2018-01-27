package kr.puzi.puzi.network.service;

import kr.puzi.puzi.biz.myservice.QuestionType;
import kr.puzi.puzi.biz.user.AgeType;
import kr.puzi.puzi.biz.user.GenderType;
import kr.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by muoe0 on 2018-01-07.
 */

public interface MyServiceNetworkService {

	@GET("/mytoday/question")
	Call<ResponseVO> getQuestion(@Header("token") String token);

	@FormUrlEncoded
	@POST("/mytoday/answer")
	Call<ResponseVO> setAnswer(@Header("token") String token,
		@Field("myTodayQuestionId") int myTodayQuestionId,
		@Field("answer") String answer,
		@Field("answerNumber") int answerNumber);

	@GET("/myworry/list")
	Call<ResponseVO> getWorryList(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex,
		@Query("mine") boolean mine,
		@Query("orderType") String orderType);

	@GET("/myworry/detail")
	Call<ResponseVO> getWorryDetail(@Header("token") String token,
		@Query("myWorryQuestionId") int myWorryQuestionId);

	@GET("/myworry/price/info")
	Call<ResponseVO> getWorryPrice(@Header("token") String token);

	@FormUrlEncoded
	@POST("/myworry/write")
	Call<ResponseVO> setWorryWrite(@Header("token") String token,
		@Field("question") String question,
		@Field("questionCount") int questionCount,
		@Field("answerOne") String answerOne,
		@Field("answerTwo") String answerTwo,
		@Field("answerThree") String answerThree,
		@Field("answerFour") String answerFour,
		@Field("questionType") QuestionType questionType,
		@Field("isShowTop") boolean isShowTop,
		@Field("isTargeting") boolean isTargeting,
		@Field("genderTypeList") List<GenderType> genderTypeList,
		@Field("ageTypeList") List<AgeType> ageTypeList);

	@FormUrlEncoded
	@POST("/myworry/answer")
	Call<ResponseVO> setWorryAnswer(@Header("token") String token,
									@Field("myWorryQuestionId") int myWorryQuestionId,
									@Field("answer") String answer,
									@Field("answerNumber") int answerNumber);

	@FormUrlEncoded
	@POST("/myworry/like")
	Call<ResponseVO> serWorryLike(@Header("token") String token,
								  @Field("myWorryQuestionId") int myWorryQuestionId);

	@FormUrlEncoded
	@POST("/myworry/notify")
	Call<ResponseVO> setWorryNotify(@Header("token") String token);

}
