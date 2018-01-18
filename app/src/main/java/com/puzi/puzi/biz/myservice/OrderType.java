package com.puzi.puzi.biz.myservice;

import lombok.AllArgsConstructor;

/**
 * Created by juhyun on 2018. 1. 14..
 */

@AllArgsConstructor
public enum OrderType {

    RECENTLY("최신순"),
    OLD("오래된순"),
    POPULAR("인기순");

    private String comment;
}
