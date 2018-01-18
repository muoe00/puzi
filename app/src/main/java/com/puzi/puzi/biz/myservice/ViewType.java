package com.puzi.puzi.biz.myservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Created by juhyun on 2018. 1. 18..
 */

@AllArgsConstructor
public enum ViewType {

    INIT(1),
    BONUS(2),
    REMAIN(3),
    END(4);

    @Getter
    public int index;

}
