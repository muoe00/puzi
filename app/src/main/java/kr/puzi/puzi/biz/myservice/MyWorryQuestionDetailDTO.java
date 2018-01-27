package kr.puzi.puzi.biz.myservice;

import lombok.Data;

/**
 * Created by juhyun on 2018. 1. 21..
 */

@Data
public class MyWorryQuestionDetailDTO {
    private MyWorryQuestionDTO myWorryQuestionDTO;
    private PersonalType personalType;
    private MyWorryAnswerDTO myWorryAnswerDTO;
    private MyWorryAnswerResultDTO myWorryAnswerResultDTO;
}