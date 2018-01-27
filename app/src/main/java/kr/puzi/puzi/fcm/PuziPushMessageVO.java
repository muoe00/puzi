package kr.puzi.puzi.fcm;

import kr.puzi.puzi.biz.advertisement.ReceivedAdvertiseVO;
import kr.puzi.puzi.biz.setting.NoticeVO;
import lombok.Data;

/**
 * Created by JangwonPark on 2018. 1. 17..
 */
@Data
public class PuziPushMessageVO {
	private PuziPushType type;
	private ReceivedAdvertiseVO receivedAdvertiseDTO;
	private NoticeVO noticeDTO;
}
