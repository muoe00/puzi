package kr.puzi.puzi.network.service;

import kr.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by JangwonPark on 2017. 12. 26..
 */
public interface StorePuziNetworkService {

	@GET("/store/puzi/saving/list")
	Call<ResponseVO> getSavingList(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/store/puzi/saving/register")
	Call<ResponseVO> registerSaving(@Header("token") String token,
		@Field("storeSavingItemId") long storeSavingItemId,
		@Field("dailyPoint") int dailyPoint);

	@FormUrlEncoded
	@POST("/store/puzi/saving/edit")
	Call<ResponseVO> editSaving(@Header("token") String token,
		@Field("storeSavingItemId") long storeSavingItemId,
		@Field("dailyPoint") int dailyPoint);

	@FormUrlEncoded
	@POST("/store/puzi/saving/terminate")
	Call<ResponseVO> terminateSaving(@Header("token") String token,
		@Field("storeSavingItemId") long storeSavingItemId);

	@GET("/store/puzi/challenge/list")
	Call<ResponseVO> getChallengeList(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/store/puzi/challenge/purchase")
	Call<ResponseVO> purchaseChallenge(@Header("token") String token,
		@Field("storeChallengeItemId") long storeChallengeItemId,
		@Field("useItem") boolean useItem);

	@GET("/store/puzi/item/list")
	Call<ResponseVO> getPuziItemList(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/store/puzi/item/purchase")
	Call<ResponseVO> purchasePuziItem(@Header("token") String token,
		@Field("storePuziItemId") int storePuziItemId,
		@Field("quantity") int quantity);

}
