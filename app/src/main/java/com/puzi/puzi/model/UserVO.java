package com.puzi.puzi.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by muoe0 on 2017-05-06.
 */

@Data
public class UserVO implements Serializable {

	private String userId; 				// 아이디
	private String email;				// 메일주소
	private String registerType;		// 가입 경로 (N:일반, K:카카오톡)
	private String passwd;				// 비밀번호
	private String notifyId;			// 푸시아이디
	// private String genderType;				// 성별 (M:남자, W:여자)
	private String ageType;					// 출생년도
	private List<String> favoriteTypeList;			// 관심사(1:True, 0:False, 뷰티/쇼핑/게임/외식/여행/금융/문화)

	private int age;				// 연령대 (1:10대, 2:20대, 3:30대, 4:40대이상)
	private int rankRatio;				// 상위 %
	private int rewardRatio;
	private int point;					// 포인트
	private int todayPoint;
	private String oreatedAt;			// 생성일자
	private String modifiedAt;			// 수정일자

}