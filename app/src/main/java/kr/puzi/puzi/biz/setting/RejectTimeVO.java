package kr.puzi.puzi.biz.setting;

import lombok.Data;

/**
 * Created by JangwonPark on 2017. 11. 4..
 */
@Data
public class RejectTimeVO {
	private int rejectTimeId;
	private String startTime;
	private String endTime;
	private String createdAt;
}
