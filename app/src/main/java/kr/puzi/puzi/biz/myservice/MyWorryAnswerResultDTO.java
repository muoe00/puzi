package kr.puzi.puzi.biz.myservice;

import lombok.Data;

/**
 * Created by juhyun on 2018. 1. 21..
 */

@Data
public class MyWorryAnswerResultDTO {

    private int answerOneCount;             // 답변1 카운트
    private int answerTwoCount;             // 답변2 카운트
    private int answerThreeCount;           // 답변3 카운트
    private int answerFourCount;            // 답변4 카운트
    private int answerCount;                // 답변 개수
    private int answerLimitCount;           // 답변최대개수
}