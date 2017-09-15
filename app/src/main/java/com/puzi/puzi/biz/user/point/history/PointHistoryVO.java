package com.puzi.puzi.biz.user.point.history;

import com.puzi.puzi.biz.user.point.PointType;
import lombok.Data;

/**
 * Created by muoe0 on 2017-04-28.
 */

@Data
public class PointHistoryVO {

	private String userId;
	private int pointHistoryId;
	private String answer;
	private boolean saved;
	private int point;
	private PointType pointType;
	private String createdAtString;
}
