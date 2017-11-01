package com.puzi.puzi.biz.channel;

import com.puzi.puzi.biz.company.CompanyVO;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by muoe0 on 2017-07-30.
 */

@Data
public class ChannelVO implements Serializable {
	private int channelId;
	private String link;
	private int score;
	private String title;
	private String comment;
	private String pictureUrl;
	private int averageScore;
	private int replyCount;
	private boolean scored;
	private int myScore;
	private String createdAt;
	private CompanyVO companyInfoDTO;
}
