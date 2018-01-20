package com.puzi.puzi.biz.myservice;

import com.puzi.puzi.biz.user.GenderType;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by juhyun on 2018. 1. 14..
 */

@AllArgsConstructor
public enum CategoryType {

    MALE("남성"),
    FEMALE("여성"),
    TEN("10대"),
    TWENTY("20대"),
    THIRTY("30대"),
    FOURTY("40대이상");

    @Getter
    private String comment;
}
