package com.puzi.puzi.biz.channel;

import com.puzi.puzi.biz.company.CompanyVO;
import lombok.Data;

import java.util.List;

/**
 * Created by muoe0 on 2017-07-30.
 */

@Data
public class ChannelVO {
	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public WriteType getWriteType() {
		return writeType;
	}

	public void setWriteType(WriteType writeType) {
		this.writeType = writeType;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public int getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(int averageScore) {
		this.averageScore = averageScore;
	}

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public boolean isScored() {
		return scored;
	}

	public void setScored(boolean scored) {
		this.scored = scored;
	}

	public String getCreatedAtString() {
		return createdAtString;
	}

	public void setCreatedAtString(String createdAtString) {
		this.createdAtString = createdAtString;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public CompanyVO getCompany() {
		return company;
	}

	public void setCompany(CompanyVO company) {
		this.company = company;
	}

	public List<ChannelReplyVO> getChannelReplyVO() {
		return channelReplyVO;
	}

	public void setChannelReplyVO(List<ChannelReplyVO> channelReplyVO) {
		this.channelReplyVO = channelReplyVO;
	}

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
