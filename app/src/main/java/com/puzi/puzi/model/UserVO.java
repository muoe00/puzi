package com.puzi.puzi.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by muoe0 on 2017-05-06.
 */

@Data
public class UserVO implements Serializable {

	private String userId; 					// 아이디
	private String passwd;					// 비밀번호
	private String registerType;			// 가입 경로 (N:일반, K:카카오톡)
	private String email;					// 메일주소
	private String notifyId;				// 푸시아이디

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getRegisterType() {
		return registerType;
	}

	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}

	public String getGenderType() {
		return genderType;
	}

	public void setGenderType(String genderType) {
		this.genderType = genderType;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAgeType() {
		return ageType;
	}

	public void setAgeType(String ageType) {
		this.ageType = ageType;
	}

	public List<String> getFavoriteTypeList() {
		return favoriteTypeList;
	}

	public void setFavoriteTypeList(List<String> favoriteTypeList) {
		this.favoriteTypeList = favoriteTypeList;
	}

	public String getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(String recommendId) {
		this.recommendId = recommendId;
	}

	public int getRankRatio() {
		return rankRatio;
	}

	public void setRankRatio(int rankRatio) {
		this.rankRatio = rankRatio;
	}

	public int getRewardRatio() {
		return rewardRatio;
	}

	public void setRewardRatio(int rewardRatio) {
		this.rewardRatio = rewardRatio;
	}

	public int getAccumulatedPoint() {
		return accumulatedPoint;
	}

	public void setAccumulatedPoint(int accumulatedPoint) {
		this.accumulatedPoint = accumulatedPoint;
	}

	public int getTodayPoint() {
		return todayPoint;
	}

	public void setTodayPoint(int todayPoint) {
		this.todayPoint = todayPoint;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public Boolean getSpendFlag() {
		return spendFlag;
	}

	public void setSpendFlag(Boolean spendFlag) {
		this.spendFlag = spendFlag;
	}

	public String getPhoneKey() {
		return phoneKey;
	}

	public void setPhoneKey(String phoneKey) {
		this.phoneKey = phoneKey;
	}

	private String genderType;				// 성별 (MALE:남자, FEMALE:여자)
	private int age;						// 출생년도
	private String ageType;					// TEN : 10대, TWENTY : 20대, THIRTY : 30대, FOURTY : 40대 이상
	private List<String> favoriteTypeList;	// 관심사(BUEATY:뷰티, SHOPPING:쇼핑, GAME:게임, EAT:외식, TOUR:여행, FINANCE:금융, CULTURE:문화)
	private String recommendId;				// 추천인 아이디
	private int rankRatio;					// 상위 %
	private int rewardRatio;				// 보상비율
	private int accumulatedPoint;			// 누적포인트
	private int todayPoint;					// 오늘적립금액
	private int point;						// 포인트
	private int score;						// 내부점수
	private String phoneType;				// 폰종류(A)
	private Boolean spendFlag;				// 구매가능여부
	private String phoneKey;				// 핸드폰고유키

}