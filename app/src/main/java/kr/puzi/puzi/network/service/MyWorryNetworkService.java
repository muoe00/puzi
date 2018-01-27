package kr.puzi.puzi.network.service;

import kr.puzi.puzi.biz.myworry.QuestionType;
import kr.puzi.puzi.biz.user.AgeType;
import kr.puzi.puzi.biz.user.GenderType;
import kr.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-09.
 */
public interface MyWorryNetworkService {

	@GET("/myworry/price/info")
	Call<ResponseVO> getPriceInfo(@Header("token") String token);

	@FormUrlEncoded
	@POST("/myworry/write")
	Call<ResponseVO> write(@Header("token") String token,
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

}