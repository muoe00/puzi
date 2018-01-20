package com.puzi.puzi.network.service;

import com.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by muoe0 on 2017-04-28.
 */

public interface UserNetworkService {

	@GET("/guest/check")
	Call<ResponseVO> check(@Query("userId") String userId);

	@GET("/guest/check/kakao")
	Call<ResponseVO> checkKakao(@Query("uuid") String uuid);

	@FormUrlEncoded
	@POST("/guest/signup")
	Call<ResponseVO> signup(@Header("userId") String userId,
		@Header("passwd") String passwd,
		@Field("registerType") String registerType,
		@Field("email") String email,
		@Field("notifyId") String notifyId,
		@Field("genderType") String genderType,
		@Field("age") int age,
		@Field("favoriteTypeList") List<String> favoriteTypeList,
		@Field("recommendId") String recommendId,
		@Field("phoneType") String phoneType,
		@Field("phoneKey") String phoneKey,
		@Field("region") String region,
		@Field("cityc") String city);

	@GET("/guest/search/id")
	Call<ResponseVO> searchid(@Query("email") String email);

	@GET("/guest/search/passwd")
	Call<ResponseVO> searchpasswd(@Query("userId") String userId,
		@Query("email") String email);

	@FormUrlEncoded
	@POST("/guest/login")
	Call<ResponseVO> login(@Header("userId") String userId,
		@Header("passwd") String passwd,
		@Field("notifyId") String notifyId, // 푸시아이디는 없을경우 'NoRegister' 로 전송
		@Field("phoneType") String phoneType,
		@Field("phoneKey") String phoneKey);

	@GET("/v2/user/myInfo")
	Call<ResponseVO> myInfo(@Header("token") String token);

	@GET("/v2/event/emoticon")
	Call<ResponseVO> eventEmoticon(@Header("token") String token);
}