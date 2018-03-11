package kr.puzi.puzi.network.service;

import java.util.List;

import kr.puzi.puzi.biz.user.FavoriteType;
import kr.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
		@Field("favoriteTypeList") List<FavoriteType> favoriteTypeList);

	@FormUrlEncoded
	@POST("/setting/update/account")
	Call<ResponseVO> updateAccount(@Header("token") String token,
		@Header("passwd") String passwd,
		@Field("email") String email);

	@FormUrlEncoded
	@POST("/setting/reject/alarm")
	Call<ResponseVO> blockTime(@Header("token") String token,
		@Field("add") boolean add,
		@Field("startTime") String startTime,
		@Field("endTime") String endTime);

	@GET("/v2/setting/reject/alarm/list")
	Call<ResponseVO> blockedTimeList(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/setting/reject/company")
	Call<ResponseVO> blockCompany(@Header("token") String token,
		@Field("add") boolean add,
		@Field("companyId") int companyId);

	@GET("/v2/setting/reject/company/list")
	Call<ResponseVO> blockedCompanyList(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/setting/center/ask")
	Call<ResponseVO> ask(@Header("token") String token,
		@Field("userType") String userType,
		@Field("askType") String askType,
		@Field("email") String email,
		@Field("title") String title,
		@Field("comment") String comment);

	@POST("/setting/center/out")
	Call<ResponseVO> out(@Header("token") String token);

	@GET("/v2/setting/version")
	Call<ResponseVO> version(@Header("token") String token);
}
