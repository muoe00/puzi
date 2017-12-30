package com.puzi.puzi.biz.store.puzi;

import lombok.Data;

/**
 * Created by JangwonPark on 2017. 12. 26..
 */
@Data
public class StoreSavingVO {
	private String userId;
	private long storeSavingItemId;
	private int savedPoint;
	private int savedMyToday;
	private int dailyPoint;
	private boolean modifiedDailyPoint;

}
