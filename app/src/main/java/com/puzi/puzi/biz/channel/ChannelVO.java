package com.puzi.puzi.biz.channel;

import com.puzi.puzi.biz.company.CompanyVO;
import lombok.Data;

/**
 * Created by muoe0 on 2017-07-30.
 */

@Data
public class ChannelVO {
	private int channelId;
	private String link;
	private int score;
	private String title;
	private String comment;
	private String pictureUrl;
	private int averageScore;
	private int replyCount;
	private boolean scored;
	private String createdAt;
	private CompanyVO companyInfoDTO;
}
