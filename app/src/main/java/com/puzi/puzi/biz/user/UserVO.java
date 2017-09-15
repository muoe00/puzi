package com.puzi.puzi.biz.user;

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
	private String passwd;				// 비밀번호
	private RegisterType registerType = RegisterType.N;
	private String notifyId;			// 푸시아이디
	private GenderType genderType;
	private int age;				// 연령대 (1:10대, 2:20대, 3:30대, 4:40대이상)
	private AgeType ageType;					// 출생년도
	private List<FavoriteType> favoriteTypeList;			// 관심사(1:True, 0:False, 뷰티/쇼핑/게임/외식/여행/금융/문화)
	private String recommendId;
	private int rankRatio;				// 상위 %
	private int rewardRatio;
	private int accumulatedPoint;
	private int todayPoint;
	private int point;					// 포인트
	private int score;
	private PhoneType phoneType;
}