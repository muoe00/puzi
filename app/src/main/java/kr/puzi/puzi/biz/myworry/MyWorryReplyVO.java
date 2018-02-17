package kr.puzi.puzi.biz.myworry;

import lombok.Data;

/**
 * Created by JangwonPark on 2018. 2. 17..
 */
@Data
public class MyWorryReplyVO {
	private int myWorryReplyId;
	private String comment;
	private String createdAt;
	private String writer;
}
