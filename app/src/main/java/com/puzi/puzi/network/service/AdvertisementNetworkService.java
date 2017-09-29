package com.puzi.puzi.network.service;

import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.biz.user.point.history.PointHistoryVO;
import com.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by muoe0 on 2017-04-28.
 */

public interface AdvertisementNetworkService {
	@GET("/v2/advertise/main")
	Call<ResponseVO<UserVO>> main(@Header("token") String token);

	@GET("/v2/advertise/list")
	Call<ResponseVO<List<ReceivedAdvertiseVO>>> adList(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/advertise/point/save")
	Call<ResponseVO> pointSave(@Header("token") String token,
		@Field("cmpnId") int cmpnId,
		@Field("answer") String answer);

	@GET("/v2/advertise/point/history")
	Call<ResponseVO<List<PointHistoryVO>>> pointHistory(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@GET("/advertise/view/params")
	Call<ResponseVO> advertiseParams(@Header("token") String token,
		@Query("companyId") int companyId,
		@Query("staySeconds") long staySeconds,
		@Query("clickCount") int clickCount,
		@Query("moveUrl") boolean moveUrl);
}
