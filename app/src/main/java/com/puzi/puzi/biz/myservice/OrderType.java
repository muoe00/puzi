package com.puzi.puzi.biz.myservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    public static OrderType getRandomType() {

        List<OrderType> orderTypes = Arrays.asList(OrderType.values());

        Random random = new Random();
        int index = random.nextInt(3) + 1;

        return orderTypes.get(index);
    }
}
