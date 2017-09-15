package com.puzi.puzi.biz.channel;

import com.puzi.puzi.biz.company.CompanyVO;
import lombok.Data;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-30.
 */

@Data
public class ChannelVO {

	private int channelId;
	private WriteType writeType;
	private String link;
	private int score;
	private String title;
	private String comment;
	private String pictureUrl;
	private int averageScore;
	private int replyCount;
	private boolean scored;
	private String createdAtString;
	private String createdBy;
	private String modifiedBy;

	private CompanyVO company;
	private List<ChannelReplyVO> channelReplyVO;
}
