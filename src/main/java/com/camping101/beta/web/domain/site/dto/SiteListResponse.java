package com.camping101.beta.web.domain.site.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SiteListResponse {

    // 대표이미지 / 사이트명 / 가격 / 기본일정 / 체크인 / 별점

    private Long siteId;
    private Long campId;

    private String name; // 사이트 명
    private String rpImage; //대표 이미지
    private LocalDateTime checkIn; // 체크 인 시간
    private LocalDateTime checkOut;// 체크 아웃 시간
    private int leastScheduling; // 최소 일정

    private int price; // 가격


}
