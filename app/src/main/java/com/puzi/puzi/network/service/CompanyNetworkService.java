package com.puzi.puzi.network.service;

import com.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by muoe0 on 2017-10-05.
 */

public interface CompanyNetworkService {

	@GET("/v2/company/profile")
	Call<ResponseVO> profile(@Header("token") String token,
		@Query("companyId") int companyId);

	@GET("/v2/company/channel/list")
	Call<ResponseVO> channelList(@Header("token") String token,
		@Query("companyId") int companyId,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/v2/company/like")
	Call<ResponseVO> like(@Header("token") String token,
		@Field("add") boolean add,
		@Field("companyId") int companyId);

}
