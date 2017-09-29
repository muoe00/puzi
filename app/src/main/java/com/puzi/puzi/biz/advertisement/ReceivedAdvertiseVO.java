package com.puzi.puzi.biz.advertisement;

import com.puzi.puzi.biz.company.CompanyVO;
import lombok.Data;

/**
 * Created by muoe0 on 2017-04-28.
 */

@Data
public class ReceivedAdvertiseVO {

	private int receivedAdvertiseId;		// 수신광고 시퀀스
	private String userId;					// 사용자 아이디
	private String cmpnId;					// 캠페인 시퀀스
	private String channelId;				// 채널 아이디
	private String sendComment;				// 광고 코멘트
	private String link;					// 링크
	private String linkPreviewUrl;			// 링크 미리보기 URL
	private int viewSeconds;				// 의무 시청 시간
	private String quiz;					// 질문
	private String answerOne;				// 답변 1
	private String answerTwo;				// 답변 2
	private String receivedAt;				// 수신 일자
	private CompanyVO companyInfoDTO;

	public int getReceivedAdvertiseId() {
		return receivedAdvertiseId;
	}

	public void setReceivedAdvertiseId(int receivedAdvertiseId) {
		this.receivedAdvertiseId = receivedAdvertiseId;
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

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getSendComment() {
		return sendComment;
	}

	public void setSendComment(String sendComment) {
		this.sendComment = sendComment;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLinkPreviewUrl() {
		return linkPreviewUrl;
	}

	public void setLinkPreviewUrl(String linkPreviewUrl) {
		this.linkPreviewUrl = linkPreviewUrl;
	}

	public int getViewSeconds() {
		return viewSeconds;
	}

	public void setViewSeconds(int viewSeconds) {
		this.viewSeconds = viewSeconds;
	}

	public String getQuiz() {
		return quiz;
	}

	public void setQuiz(String quiz) {
		this.quiz = quiz;
	}

	public String getAnswerOne() {
		return answerOne;
	}

	public void setAnswerOne(String answerOne) {
		this.answerOne = answerOne;
	}

	public String getAnswerTwo() {
		return answerTwo;
	}

	public void setAnswerTwo(String answerTwo) {
		this.answerTwo = answerTwo;
	}

	public String getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(String receivedAt) {
		this.receivedAt = receivedAt;
	}

	public CompanyVO getCompanyInfoDTO() {
		return companyInfoDTO;
	}

	public void setCompanyInfoDTO(CompanyVO companyInfoDTO) {
		this.companyInfoDTO = companyInfoDTO;
	}
}
