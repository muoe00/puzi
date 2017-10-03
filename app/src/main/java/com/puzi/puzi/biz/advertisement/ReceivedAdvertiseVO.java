package com.puzi.puzi.biz.advertisement;

import com.puzi.puzi.biz.company.CompanyVO;
import lombok.Data;

/**
 * Created by muoe0 on 2017-04-28.
 */

@Data
public class ReceivedAdvertiseVO {

	private int receivedAdvertiseId;
	private int cmpnId;
	private int channelId;
	private String sendComment;
	private String link;
	private String linkPreviewUrl;
	private int viewSeconds;
	private String quiz;
	private String answerOne;
	private String answerTwo;
	private String receivedAt;
	private boolean isNew;
	private CompanyVO companyInfoDTO;

}
