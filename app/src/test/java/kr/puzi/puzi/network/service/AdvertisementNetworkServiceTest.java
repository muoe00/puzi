package kr.puzi.puzi.network.service;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import kr.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import kr.puzi.puzi.biz.user.point.history.PointHistoryVO;
import kr.puzi.puzi.network.ResponseVO;
import kr.puzi.puzi.network.RetrofitManager;
import retrofit2.Call;

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
	public void adList() throws Exception {
		System.out.println("+++ START");

		Call<ResponseVO> call = sut.adList("test123123123", 1);
		ResponseVO responseVO = NetworkTestValidation.call(call);
		List<ReceivedAdvertiseVO> receivedAdvertiseVOs = responseVO.getList("receivedAdvertiseDTOList", ReceivedAdvertiseVO.class);
		int totalCount = responseVO.getInteger("totalCount");

		System.out.println("+++ receivedAdvertiseVOs : " + receivedAdvertiseVOs.toString());
		System.out.println("+++ totalCount : " + totalCount);

		for(ReceivedAdvertiseVO receivedAdvertiseVO : receivedAdvertiseVOs) {
			System.out.println("+++ receivedAdvertiseVO : " + receivedAdvertiseVO.toString());
		}
	}

	@Test
	public void pointSave() throws Exception {
		System.out.println("+++ START");

		Call<ResponseVO> call = sut.pointSave("test123123123", 2, "a");
		ResponseVO responseVO = NetworkTestValidation.call(call);
	}

	@Test
	public void pointHistory() throws Exception {
		System.out.println("+++ START");

		Call<ResponseVO> call = sut.pointHistory("test123123123", 1);
		ResponseVO responseVO = NetworkTestValidation.call(call);

		List<PointHistoryVO> pointHistoryVOs = responseVO.getList("pointHistoryList", PointHistoryVO.class);
		int totalCount = responseVO.getInteger("totalCount");

		System.out.println("pointHistoryVOs : " + pointHistoryVOs.toString());
		System.out.println("totalCount : " + totalCount);
	}

	@Test
	public void advertiseParams() throws Exception {
		System.out.println("+++ START");

		/*Call<ResponseVO> call = sut.advertiseParams("test123123123", 1, 1, 1, true);
		ResponseVO responseVO = NetworkTestValidation.call(call);*/
	}
}