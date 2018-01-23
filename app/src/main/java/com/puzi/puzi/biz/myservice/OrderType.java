package com.puzi.puzi.biz.myservice;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by juhyun on 2018. 1. 14..
 */

@AllArgsConstructor
public enum OrderType {

    RECENTLY("최신순"),
    OLD("오래된순"),
    POPULAR("인기순");

    @Getter
    private String comment;

    public static List<OrderType> getList() {

        List<OrderType> orderTypes = Arrays.asList(OrderType.values());

        return orderTypes;
    }
}
