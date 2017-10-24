package com.puzi.puzi.network.service;

import com.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by muoe0 on 2017-07-09.
 */

public interface SettingNetworkService {

	@GET("/v2/setting/notice/list")
	Call<ResponseVO> list(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/setting/update/favorites")
	Call<ResponseVO> updateFavorites(@Header("token") String token,
		@Field("favoriteTypeList") String favoriteTypeList);

	@FormUrlEncoded
	@POST("/setting/update/account")
	Call<ResponseVO> updateAccount(@Header("token") String token,
		@Header("passwd") String passwd,
		@Field("email") String email);

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
	@POST("/v2/setting/reject/company/list")
	Call<ResponseVO> companyList(@Header("token") String token,
		@Field("pagingIndex") int pagingIndex);

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
