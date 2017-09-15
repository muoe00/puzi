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

}
