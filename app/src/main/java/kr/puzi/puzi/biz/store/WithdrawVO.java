package kr.puzi.puzi.biz.store;

import lombok.Data;

/**
 * Created by JangwonPark on 2017. 11. 5..
 */
@Data
public class WithdrawVO {
	private int withdrawId;
	private int money;
	private String accountNumber;
	private String accountBankCode;
	private String accountBankName;
	private String accountName;
	private boolean withdrawn;
	private String reason;
	private String createdAt;
}
