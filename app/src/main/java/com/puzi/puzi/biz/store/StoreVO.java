package com.puzi.puzi.biz.store;

import lombok.Data;

/**
 * Created by muoe0 on 2017-08-06.
 */

@Data
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

}
