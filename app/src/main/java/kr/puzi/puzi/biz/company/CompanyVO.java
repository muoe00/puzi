package kr.puzi.puzi.biz.company;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by muoe0 on 2017-05-06.
 */

@Data
public class CompanyVO implements Serializable {
	private int companyId;
	private String companyAlias;
	private String pictureUrl;
	private String comment;
	private boolean liked;
	private boolean blocked;

	public boolean getBlocked() {
		return this.blocked;
	}
}
