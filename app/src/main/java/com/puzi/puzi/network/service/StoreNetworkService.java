package com.puzi.puzi.network.service;

import com.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by muoe0 on 2017-07-09.
 */

public interface StoreNetworkService {

	@GET("/store/brand/list")
	Call<ResponseVO> brandList(@Header("token") String token);

	@GET("/store/item/list")
	Call<ResponseVO> itemList(@Header("token") String token,
		@Query("storeId") int storeId,
		@Query("pagingIndex") int pagingIndex);

	@GET("/store/item/detail")
	Call<ResponseVO> itemDetail(@Header("token") String token,
		@Query("storeId") int storeId,
		@Query("storeItemId") int storeItemId);

	@FormUrlEncoded
	@POST("/store/purchase")
	Call<ResponseVO> purchase(@Header("token") String token,
		@Field("storeId") int storeId,
		@Field("storeItemId") int storeItemId,
		@Field("quantity") int quantity);

	@GET("/store/purchase/history")
	Call<ResponseVO> purchaseHistory(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/store/refund")
	Call<ResponseVO> refund(@Header("token") String token,
		@Field("purchaseHistoryId") int purchaseHistoryId,
		@Field("quantity") int quantity);

	@GET("/store/withdraw/result")
	Call<ResponseVO> withdrawResult(@Header("token") String token,
		@Query("pagingIndex") int pagingIndex);

	@FormUrlEncoded
	@POST("/store/withdraw")
	Call<ResponseVO> withdraw(@Header("token") String token,
		@Header("accountNumber") String accountNumber,
		@Header("accountBankCode") String accountBankCode,
		@Field("accountName") String accountName,
		@Field("money") int money);

}