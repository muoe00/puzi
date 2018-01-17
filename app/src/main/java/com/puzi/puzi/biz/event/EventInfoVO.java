package com.puzi.puzi.biz.event;

import lombok.Data;

/**
 * Created by JangwonPark on 2018. 1. 17..
 */
@Data
public class EventInfoVO {
	private EventType eventType;
	private EventStatusType eventStatusType;
	private EventResultType resultType;
	private String url;
}
