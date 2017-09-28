package com.puzi.puzi.biz.channel;

import lombok.Data;

/**
 * Created by muoe0 on 2017-08-12.
 */

@Data
public class ChannelReplyVO {
	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getChannelReplyId() {
		return channelReplyId;
	}

	public void setChannelReplyId(int channelReplyId) {
		this.channelReplyId = channelReplyId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public int getReverse() {
		return reverse;
	}

	public void setReverse(int reverse) {
		this.reverse = reverse;
	}

	public int getWarning() {
		return warning;
	}

	public void setWarning(int warning) {
		this.warning = warning;
	}

	public boolean isScored() {
		return scored;
	}

	public void setScored(boolean scored) {
		this.scored = scored;
	}

	private int channelId;
	private int channelReplyId;
	private String comment;
	private int recommend;
	private int reverse;
	private int warning;
	private boolean scored;
}