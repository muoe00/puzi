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

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCmpnId() {
		return cmpnId;
	}

	public void setCmpnId(String cmpnId) {
		this.cmpnId = cmpnId;
	}

	public String getSendComment() {
		return sendComment;
	}

	public void setSendComment(String sendComment) {
		this.sendComment = sendComment;
	}

	public String getLinkPreviewUrl() {
		return linkPreviewUrl;
	}

	public void setLinkPreviewUrl(String linkPreviewUrl) {
		this.linkPreviewUrl = linkPreviewUrl;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getReceivedAtString() {
		return receivedAtString;
	}

	public void setReceivedAtString(String receivedAtString) {
		this.receivedAtString = receivedAtString;
	}

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
