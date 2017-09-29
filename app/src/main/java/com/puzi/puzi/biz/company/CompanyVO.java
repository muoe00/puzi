package com.puzi.puzi.biz.company;

import lombok.Data;

/**
 * Created by muoe0 on 2017-05-06.
 */

@Data
public class CompanyVO {

	private int companyId;				// 회사시퀀스
	private String companyAlias;		// 회사명
	private String pictureUrl;			// 사진주소
	private String comment;				// 설명

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyAlias() {
		return companyAlias;
	}

	public void setCompanyAlias(String companyAlias) {
		this.companyAlias = companyAlias;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
