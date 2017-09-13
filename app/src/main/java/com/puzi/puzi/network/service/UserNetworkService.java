package com.puzi.puzi.network.service;

import com.puzi.puzi.model.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by muoe0 on 2017-04-28.
 */

public interface UserNetworkService {

	@GET("/guest/check")
	Call<ResponseVO> check(@Query("userId") String userId);

	@FormUrlEncoded
	@POST("/guest/signup")
	Call<ResponseVO> signup(@Header("userId") String userId,
		@Header("passwd") String passwd,
		@Field("registerType") String registerType, // 가입경로는 일반이면 N, 카카오톡이면 K
		@Field("email") String email,
		@Field("notifyId") String notifyId, // 푸시아이디는 없을경우 'NoRegister' 로 전송
		@Field("gender") String gender, // 성별은 남자는 M, 여자는 W
		@Field("age") String age,
		@Field("favorites") String favorites, // 관심사는 1(True) 또는 0(false)으로 표기하여 순서대로전송 (순서:뷰티/쇼핑/게임/외식/여행/금융/문화)
		@Field("recommendId") String recommendId,
		@Field("phoneType") String phoneType);

	@GET("/guest/search/id")
	Call<ResponseVO> searchid(@Query("email") String email);

	@GET("/guest/search/passwd")
	Call<ResponseVO> searchpasswd(@Query("userId") String userId,
		@Query("email") String email);

	@FormUrlEncoded
	@POST("/guest/login")
	Call<ResponseVO<String>> login(@Header("userId") String userId,
		@Header("passwd") String passwd,
		@Field("notifyId") String notifyId, // 푸시아이디는 없을경우 'NoRegister' 로 전송
		@Field("phoneType") String phoneType);
}