package kr.puzi.puzi.biz.myservice;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by juhyun on 2018. 1. 14..
 */

@Data
public class MyWorryQuestionDTO implements Serializable {

    private int myWorryQuestionId;		            // 시퀀스
    private String question;				        // 질문
    private int questionCount;				        // 질문개수
    private String answerOne;			            // 첫번째 답변
    private String answerTwo;			            // 두번째 답변
    private String answerThree;			            // 세번째 답변
    private String answerFour;			            // 네번째 답변
    private int answeredCount;			    	    // 답변된숫자
    private int totalAnswerCount;                   // 총답변숫자
    private int likedCount;                         // 좋아요숫자
    private boolean notifiedByMe;                   // 내가신고했는지
    private boolean likedByMe;                      // 내가좋아요했는지
    private List<CategoryType> categoryTypeList;    // UserInfoDTO 참조 (연령, 성별만)
    private boolean needToShowResult;
}
