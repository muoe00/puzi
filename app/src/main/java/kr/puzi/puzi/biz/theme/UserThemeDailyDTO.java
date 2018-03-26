package kr.puzi.puzi.biz.theme;

import lombok.Data;

/**
 * Created by juhyun on 2018. 3. 17..
 */

@Data
public class UserThemeDailyDTO {

    private String targetDate;      // 날짜 (x)
    private double score;           // 점수 (y)

}
