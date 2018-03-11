package kr.puzi.puzi.biz.myservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by juhyun on 2018. 1. 14..
 */

@AllArgsConstructor
public enum OrderType {

    RECENTLY("최신순"),
    POPULAR("인기순"),
    MINE("나의 고민");

    @Getter
    private String comment;
}
