package com.puzi.puzi.biz;

import com.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import com.puzi.puzi.biz.notice.NoticeVO;
import com.puzi.puzi.biz.user.UserVO;
import lombok.Data;

import java.util.List;

/**
 * Created by muoe0 on 2017-05-28.
 */
@Data
public class MainVO {

	private UserVO userVO;
	private List<ReceivedAdvertiseVO> receivedAdvertiseList;
	private List<NoticeVO> noticeList;
	private String version;

	public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO user) {
		this.userVO = user;
	}

	public List<ReceivedAdvertiseVO> getReceivedAdvertiseList() {
		return receivedAdvertiseList;
	}

	public void setReceivedAdvertiseList(List<ReceivedAdvertiseVO> receivedAdvertiseList) {
		this.receivedAdvertiseList = receivedAdvertiseList;
	}

	public List<NoticeVO> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<NoticeVO> noticeList) {
		this.noticeList = noticeList;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
