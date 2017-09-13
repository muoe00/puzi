package com.puzi.puzi.network.service;

import com.puzi.puzi.model.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by muoe0 on 2017-07-09.
 */

public interface SettingNetworkService {

	@GET("/setting/notice/list")
	Call<ResponseVO> list(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/setting/update")
	Call<ResponseVO> update(@Header("token") String token,
		@Field("email") String email,
		@Field("favorites") String favorites);

	@FormUrlEncoded
	@POST("/setting/update/passwd")
	Call<ResponseVO> updatePasswd(@Header("token") String token,
		@Header("passwd") String passwd);

	@FormUrlEncoded
	@POST("/setting/reject/alarm")
	Call<ResponseVO> alarm(@Header("token") String token,
		@Field("add") boolean add,
		@Field("startTime") String startTime,
		@Field("endTime") String endTime);

	@FormUrlEncoded
	@POST("/setting/reject/company")
	Call<ResponseVO> company(@Header("token") String token,
		@Field("add") boolean add,
		@Field("companyId") int companyId);

	@FormUrlEncoded
	@POST("/setting/center/ask")
	Call<ResponseVO> ask(@Header("token") String token,
		@Field("userType") String userType,
		@Field("askType") String askType,
		@Field("title") String title,
		@Field("comment") String comment);

	@FormUrlEncoded
	@POST("/setting/center/out")
	Call<ResponseVO> out(@Header("token") String token);

}
