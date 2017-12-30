package com.puzi.puzi.biz.store.puzi;

import com.puzi.puzi.biz.store.StoreItemVO;
import com.puzi.puzi.biz.store.StoreVO;
import lombok.Data;

/**
 * Created by JangwonPark on 2017. 12. 30..
 */
@Data
public class StoreSavingItemVO {
	private long storeSavingItemId;
	private int quantity;
	private String name;
	private int discountRate;
	private int targetPoint;
	private int targetMyToday;
	private StoreVO storeDTO;
	private StoreItemVO storeItemDTO;
}
