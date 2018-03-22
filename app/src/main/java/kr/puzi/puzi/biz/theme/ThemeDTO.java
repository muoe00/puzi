package kr.puzi.puzi.biz.theme;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by juhyun on 2018. 3. 17..
 */

@Data
public class ThemeDTO implements Serializable {

    private int themeInfoId;       // 시퀀스
    private DegreeType degreeType;
    private double rate;            // 비율
    private String targetMin;       // 최소 테마이름
    private String targetMax;       // 최대 테마이름
    private int totalUserCount;     // 참여자수
    private String themeBackground; // 백그라운드이미지

}