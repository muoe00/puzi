package com.puzi.puzi.biz.advertisement;

import lombok.Data;

/**
 * Created by muoe0 on 2017-04-28.
 */

@Data
public class ReceivedAdvertiseVO {

	private String channelId;				// 채널아이디
	private String companyId;				// 회사아이디
	private String userId;					// 사용자아이디
	private String cmpnId;					// 캠페인시퀀스
	private String sendComment;				// 광고코멘트
	private String link;					// 링크
	private String linkPreviewUrl;			// 광고이미지URL
	private String companyName;				// 회사명
	private String pictureUrl;				// 회사사진주소
	private int viewSecond;
	private String quiz;
	private String answerOne;
	private String answerTwo;
	private String receivedAtString;		// 표시할시간
}
