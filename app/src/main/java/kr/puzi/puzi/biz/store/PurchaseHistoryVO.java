package kr.puzi.puzi.biz.store;

import lombok.Data;

/**
 * Created by 170605 on 2017-10-27.
 */

@Data
public class PurchaseHistoryVO {

	private String validEndDate;
	private String couponImageUrl;
	private Integer price;
	private Integer totalPrice;
	private Integer quantity;
	private CouponStatusType couponStatusType;
	private String createdAt;
	private StoreVO storeDTO;
	private StoreItemVO storeItemDTO;

}