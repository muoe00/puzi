package kr.puzi.puzi.network.service;

import kr.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by muoe0 on 2017-04-28.
 */
public interface SlidingNetworkService {
	@GET("/advertise/sliding/list")
	Call<ResponseVO> slidingList(@Header("token") String token);

	@FormUrlEncoded
	@POST("/advertise/sliding/save")
	Call<ResponseVO> save(@Header("token") String token,
		@Field("userSlidingId") int userSlidingId);

	@FormUrlEncoded
	@POST("/advertise/sliding/save/cpc")
	Call<ResponseVO> saveCpc(@Header("token") String token,
		@Field("userSlidingId") int userSlidingId);

}
