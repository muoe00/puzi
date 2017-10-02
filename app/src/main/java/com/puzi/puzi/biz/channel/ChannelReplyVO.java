package com.puzi.puzi.biz.channel;

import lombok.Data;

/**
 * Created by muoe0 on 2017-08-12.
 */

@Data
public class ChannelReplyVO {
	private int channelId;
	private int channelReplyId;
	private String comment;
	private int recommend = 0;
	private int reverse = 0;
	private String createdAt;
}