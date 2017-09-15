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
	private String storeType;
	private String createdAt;
	private String modifiedAt;
}
