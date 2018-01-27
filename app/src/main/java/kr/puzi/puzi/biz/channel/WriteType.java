package kr.puzi.puzi.biz.channel;

/**
 * Created by muoe0 on 2017-08-12.
 */

public enum WriteType {
	ADVERTISER("광고주"),
	EDITOR("에디터");

	private String comment;

	WriteType(String comment){
		this.comment = comment;
	}
}
