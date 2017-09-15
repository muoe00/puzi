package com.puzi.puzi.biz.notice;

import lombok.Data;

/**
 * Created by muoe0 on 2017-06-04.
 */

@Data
public class NoticeVO {

	private String title;			// 제목
	private int userNoticeId;		// 사용자공지시퀀스
	private String comment;			// 내용
	private boolean confirm;		// 확인여부
	private Object createdAt;		// 생성일자
	private Object createdBy;		// 수정일자
}
