package com.puzi.puzi.biz.store;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by JangwonPark on 2017. 10. 22..
 */

@Data
public class StoreItemVO implements Serializable {
	private int storeId;
	private int storeItemId;
	private String name;
	private String pictureUrl;
	private int price;
	private String comment;
	private int expiryDay;
}
