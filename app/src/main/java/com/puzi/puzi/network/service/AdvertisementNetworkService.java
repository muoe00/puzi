package com.puzi.puzi.network.service;

import com.puzi.puzi.model.ReceivedAdvertiseVO;
import com.puzi.puzi.model.ResponseVO;
import com.puzi.puzi.model.UserVO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by muoe0 on 2017-04-28.
 */

public interface AdvertisementNetworkService {
	@GET("/advertise/main")
	Call<ResponseVO<UserVO>> main(@Header("token") String token);

	@GET("/advertise/list")
	Call<ResponseVO<List<ReceivedAdvertiseVO>>> adList(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/advertise/point/save")
	Call<ResponseVO> pointSave(@Header("token") String token,
		@Field("cmpnId") int cmpnId);

	@GET("/advertise/point/history")
	Call<ResponseVO> pointHistory(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@GET("/advertise/company/profile")
	Call<ResponseVO> companyProfile(@Header("token") String token,
		@Query("companyId") int companyId);
}
