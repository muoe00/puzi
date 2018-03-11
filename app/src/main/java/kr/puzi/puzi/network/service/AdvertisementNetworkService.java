package kr.puzi.puzi.network.service;

import kr.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by muoe0 on 2017-04-28.
 */
public interface AdvertisementNetworkService {

	@GET("/v2/advertise/list")
	Call<ResponseVO> adList(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/advertise/point/save")
	Call<ResponseVO> pointSave(@Header("token") String token,
		@Field("receivedAdvertiseId") int receivedAdvertiseId,
		@Field("answer") String answer);

	@GET("/v2/advertise/point/history")
	Call<ResponseVO> pointHistory(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@GET("/advertise/view/params")
	Call<ResponseVO> advertiseParams(@Header("token") String token,
		@Query("companyId") int companyId,
		@Query("staySeconds") long staySeconds,
		@Query("clickCount") int clickCount,
		@Query("moveUrl") boolean moveUrl);
}
