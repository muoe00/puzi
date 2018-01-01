package com.puzi.puzi.biz.store.puzi;

import com.puzi.puzi.biz.store.StoreItemVO;
import lombok.Data;

/**
 * Created by JangwonPark on 2017. 12. 26..
 */
@Data
public class UserSavingVO {
	private String userId;
	private long storeSavingItemId;
	private int savedPoint;
	private int savedMyToday;
	private int dailyPoint;
	private boolean modifiedDailyPoint;
	private StoreSavingItemVO storeSavingItemDTO;
	private StoreItemVO storeItemDTO;
}
