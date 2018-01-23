package com.puzi.puzi.biz.advertisement;

import com.puzi.puzi.biz.company.CompanyVO;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by muoe0 on 2017-04-28.
 */

@Data
public class ReceivedAdvertiseVO implements Serializable {

	private int receivedAdvertiseId;
	private int cmpnId;
	private int channelId;
	private String sendComment;
	private String link;
	private String linkPreviewUrl;
	private int viewSeconds;
	private String quiz;
	private String answerOne;
	private String answerTwo;
	private String receivedAt;
	private boolean test;
	private boolean today;
	private boolean saved;
	private String companyName;
	private String companyPictureUrl;
	private String companyComment;
	private CompanyVO companyInfoDTO;

	public void transferComponyInfo() {
		if(companyInfoDTO == null) {
			companyInfoDTO = new CompanyVO();
		}
		companyInfoDTO.setCompanyAlias(companyName);
		companyInfoDTO.setPictureUrl(companyPictureUrl);
		companyInfoDTO.setComment(companyComment);
	}
}
