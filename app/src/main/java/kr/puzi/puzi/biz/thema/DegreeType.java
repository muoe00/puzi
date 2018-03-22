package kr.puzi.puzi.biz.thema;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by juhyun on 2018. 3. 17..
 */

@AllArgsConstructor
public enum DegreeType {

    MAX("최대"),
    MIN("최소"),
    AVERAGE("일반인"),
    LACK("데이터부족");

    @Getter
    private String comment;
}
