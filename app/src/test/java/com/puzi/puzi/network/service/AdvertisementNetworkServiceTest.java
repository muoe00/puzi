package com.puzi.puzi.network.service;

import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.notice.NoticeVO;
import com.puzi.puzi.biz.user.UserVO;
import com.puzi.puzi.network.ResponseVO;
import com.puzi.puzi.network.RetrofitManager;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;

import java.util.List;

/**
 * Created by muoe0 on 2017-10-02.
 */
public class AdvertisementNetworkServiceTest {

	AdvertisementNetworkService sut;

	@Before
	public void setUp() throws Exception {
		this.sut = RetrofitManager.create(AdvertisementNetworkService.class);
	}

	@Test
	public void main() throws Exception {
		System.out.println("+++ START");

		Call<ResponseVO> call = sut.main("test123123123");
		ResponseVO responseVO = NetworkTestValidation.call(call);

		UserVO userVO = responseVO.getValue("userInfoDTO", UserVO.class);
		List<ReceivedAdvertiseVO> receivedAdvertiseVOs = responseVO.getList("receivedAdvertiseDTOList", ReceivedAdvertiseVO.class);
		int raTotalCount = responseVO.getValue("raTotalCount", int.class);
		List<NoticeVO> noticeVOs = responseVO.getList("noticeDTOList", NoticeVO.class);
		int unTotalCount = responseVO.getValue("unTotalCount", int.class);
		String version = responseVO.getValue("version", String.class);

		System.out.println("+++ userVO : " + userVO.toString());
		System.out.println("+++ receivedAdvertiseVOs : " + receivedAdvertiseVOs.toString());
		System.out.println("+++ raTotalCount : " + raTotalCount);
		System.out.println("+++ noticeVOs : " + noticeVOs.toString());
		System.out.println("+++ unTotalCount : " + unTotalCount);
		System.out.println("+++ version : " + version);

		for(ReceivedAdvertiseVO receivedAdvertiseVO : receivedAdvertiseVOs) {
			System.out.println("+++ receivedAdvertiseVO : " + receivedAdvertiseVO.toString());
		}
	}

	@Test
	public void adList() throws Exception {
		System.out.println("+++ START");

		Call<ResponseVO> call = sut.adList("test123123123", 1);
		ResponseVO responseVO = NetworkTestValidation.call(call);
		List<ReceivedAdvertiseVO> receivedAdvertiseVOs = responseVO.getList("receivedAdvertiseDTOList", ReceivedAdvertiseVO.class);
		int totalCount = responseVO.getValue("totalCount", Integer.class);

		System.out.println("+++ receivedAdvertiseVOs : " + receivedAdvertiseVOs.toString());
		System.out.println("+++ totalCount : " + totalCount);

		for(ReceivedAdvertiseVO receivedAdvertiseVO : receivedAdvertiseVOs) {
			System.out.println("+++ receivedAdvertiseVO : " + receivedAdvertiseVO.toString());
		}
	}
}