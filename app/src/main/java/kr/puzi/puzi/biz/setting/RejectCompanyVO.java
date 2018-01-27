package kr.puzi.puzi.biz.setting;

import kr.puzi.puzi.biz.company.CompanyVO;
import lombok.Data;

/**
 * Created by JangwonPark on 2017. 11. 4..
 */
@Data
public class RejectCompanyVO {
	private int rejectCompanyId;
	private CompanyVO companyInfoDTO;
	private String createdAt;
}
