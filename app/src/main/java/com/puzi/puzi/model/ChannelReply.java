package com.puzi.puzi.model;

import lombok.Data;

/**
 * Created by muoe0 on 2017-08-12.
 */

@Data
public class ChannelReply {

	private int channelId;
	private int channelReplyId;
	private String comment;
	private int recommend;
	private int reverse;
	private int warning;
	private boolean scored;

	/*public ChannelReply(String userId, int channelId, String comment) {
		setCreatedBy(userId);
		this.channelId = channelId;
		this.comment = comment;
	}*/
}