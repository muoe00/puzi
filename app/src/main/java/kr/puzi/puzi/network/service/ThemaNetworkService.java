package kr.puzi.puzi.network.service;

import kr.puzi.puzi.network.ResponseVO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by juhyun on 2018. 3. 17..
 */

public interface ThemaNetworkService {

    @GET("/theme/list")
    Call<ResponseVO> getList(@Header("token") String token);

    @GET("/theme/detail")
    Call<ResponseVO> getDetail(@Header("token") String token,
                               @Field("themeInfoId") int themeInfoId);

    @GET("/theme/reply/list")
    Call<ResponseVO> getReplyList(@Header("token") String token,
                                  @Field("themeInfoId") int themeInfoId);

    @FormUrlEncoded
    @POST("/theme/reply/write")
    Call<ResponseVO> write(@Header("token") String token,
                           @Field("themeInfoId") int themeInfoId,
                           @Field("comment") String comment);

    @FormUrlEncoded
    @POST("/theme/reply/notify")
    Call<ResponseVO> notify(@Header("token") String token,
                           @Field("themeInfoId") int themeInfoId,
                           @Field("themeReplyId") int themeReplyId);

    @FormUrlEncoded
    @POST("/theme/reply/delete")
    Call<ResponseVO> delete(@Header("token") String token,
                           @Field("themeInfoId") int themeInfoId,
                           @Field("themeReplyId") int themeReplyId);

}
