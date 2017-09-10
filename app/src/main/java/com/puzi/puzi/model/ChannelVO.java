package com.puzi.puzi.model;

import lombok.Data;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-30.
 */

@Data
public class ChannelVO extends Object {

	//private String viewType;
	//private int channelId;
	//private CompanyVO company;
	//private String comment;
	/*private String writeType;	 //
	private String writeId;
	private String link;	 //
	private String title;	 //
	private String pictureUrl;	 //
	private int score;	 //
	private int replyCount;	 //
	// private String createdAt;	 //
	private String createdAtString;	 //
	private String createdBy;	 //
	private String modifiedAt;	 //
	private String modifiedBy;	 //*/

	private int channelId;
	private WriteType writeType;
	private String link;
	private int score;
	private String title;
	private String comment;
	private String pictureUrl;
	private int averageScore;
	private boolean scored;
	//private LocalDateTime createdAt;
	private String createdAtString;
	private String createdBy;
	//private LocalDateTime modifiedAt;
	private String modifiedBy;
	private int replyCount;
	private List<ChannelReply> channelReply;
	/* 작성자정보(회사) */
	private CompanyVO company;

}
