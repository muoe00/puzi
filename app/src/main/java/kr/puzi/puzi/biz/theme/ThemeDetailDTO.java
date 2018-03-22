package kr.puzi.puzi.biz.theme;

/**
 * Created by juhyun on 2018. 3. 17..
 */

public class ThemeDetailDTO {

    private long themeInfoId;                           // 시퀀스
    private DegreeType degreeType;
    private String targetMin;                           // 최소 테마이름
    private String targetMinComment;                    // 최소 테마 설명
    private String targetMax;		                    // 최대 테마이름
    private String targetMaxComment;		            // 최대 테마 설명
    private int totalUserCount;		                    // 참여자수
    private double myAverageScore;		                // 나의 평균점수
    private double rate;		                        // 비율
    private double totalAverageScore;		            // 전체평균점수
    private double totalMinAverageScore;		        // 최소테마 평균점수
    private double totalMaxAverageScore;		        // 최대테마 평균점수
    private UserThemeDailyDTO userThemeDailies;		    // 데일리리스트


}


