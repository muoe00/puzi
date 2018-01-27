package kr.puzi.puzi.biz.myservice;

import lombok.AllArgsConstructor;

/**
 * Created by juhyun on 2018. 1. 24..
 */

@AllArgsConstructor
public enum PersonalType {

    OWNER("내가 작성한 고민"),
    DONE("끝난 고민"),
    ANSWERED("답변한 고민"),
    NOT_ANSWERED ("미답변 고민");

    private String comment;

    public boolean isMine() {
        return this == OWNER;
    }
}
