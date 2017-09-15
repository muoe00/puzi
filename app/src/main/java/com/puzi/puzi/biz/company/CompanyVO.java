package com.puzi.puzi.biz.company;

import lombok.Data;

/**
 * Created by muoe0 on 2017-05-06.
 */

@Data
public class CompanyVO {

	private String viewType;
	private int companyId;				// 회사시퀀스
	private String companyName;			// 회사명
	private String pictureUrl;			// 사진주소
	private String comment;				// 설명
	private String createdAt;			// 생성일자
	private String createdBy;			// 생성자
	private String modifiedAt;			// 수정일자
	private String modifiedBy;			// 수정자

}
