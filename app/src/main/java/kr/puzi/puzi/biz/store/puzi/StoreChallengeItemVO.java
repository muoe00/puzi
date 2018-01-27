package kr.puzi.puzi.biz.store.puzi;

import kr.puzi.puzi.biz.store.StoreItemVO;
import kr.puzi.puzi.biz.store.StoreVO;
import lombok.Data;

/**
 * Created by JangwonPark on 2017. 12. 26..
 */
@Data
public class StoreChallengeItemVO {
	private long storeChallengeItemId;
	private String name;
	private int price;
	private int quantity;
	private int challengeCount;
	private int challengeSuccessCount;
	private StoreVO storeDTO;
	private StoreItemVO storeItemDTO;
}
