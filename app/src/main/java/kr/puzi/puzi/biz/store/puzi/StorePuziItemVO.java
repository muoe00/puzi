package kr.puzi.puzi.biz.store.puzi;

import lombok.Data;

/**
 * Created by JangwonPark on 2017. 12. 26..
 */
@Data
public class StorePuziItemVO {
	private int storePuziItemId;
	private String name;
	private String pictureUrl;
	private int price;
	private String comment;
}
