package com.puzi.puzi.biz.store;

import lombok.Data;

/**
 * Created by JangwonPark on 2017. 10. 22..
 */

@Data
public class StoreItemVO {

	private int storeId;
	private int storeItemId;
	private String name;
	private String pictureUrl;
	private int price;
	private String comment;
	private int expiryDay;
}
