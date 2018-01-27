package kr.puzi.puzi.biz.store;

import lombok.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by muoe0 on 2017-08-06.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class StoreVO {

	private int storeId;
	private String name;
	private String pictureUrl;
	private StoreType storeType;

	public static StoreVO createWithdraw() {
		StoreVO storeVO = new StoreVO();
		storeVO.setStoreId(0);
		storeVO.setName("출금하기");
		storeVO.setPictureUrl(null);
		storeVO.setStoreType(StoreType.WITHDRAW);
		return storeVO;
	}

	public static List<StoreVO> getPuziStoreList() {
		return newArrayList(getPuziChallenge(), getPuziSaving());
	}

	private static StoreVO getPuziItem() {
		return new StoreVO(0, "푸지아이템", "", StoreType.PUZI);
	}

	private static StoreVO getPuziChallenge() {
		return new StoreVO(0, "푸지응모", "", StoreType.PUZI);
	}

	private static StoreVO getPuziSaving() {
		return new StoreVO(0, "푸지적금", "", StoreType.PUZI);
	}
}
