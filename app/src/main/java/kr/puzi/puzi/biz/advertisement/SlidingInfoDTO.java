package kr.puzi.puzi.biz.advertisement;

import lombok.Data;

/**
 * Created by JangwonPark on 2018. 2. 17..
 */
@Data
public class SlidingInfoDTO {
	private long userSlidingId;
	private String slidingPreviewUrl;
	private String link;
	private int savePoint;
	private boolean saved;
	private boolean clicked;
}
