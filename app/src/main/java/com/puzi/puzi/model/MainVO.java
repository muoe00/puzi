package com.puzi.puzi.model;

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

}
