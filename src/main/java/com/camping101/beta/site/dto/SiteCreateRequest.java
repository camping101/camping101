package com.camping101.beta.site.dto;

import com.camping101.beta.site.entity.SiteCapacity;
import com.camping101.beta.site.entity.SiteType;
import com.camping101.beta.site.entity.SiteYn;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteCreateRequest {

    private Long campId;

    private String name;
    private String rpImage; //대표 이미지
    private String introduction;
    private SiteType type;
    private SiteYn siteYn;


    private LocalDateTime checkIn; // 체크 인 시간
    private LocalDateTime checkOut;// 체크 아웃 시간
    private Integer leastScheduling; // 최소 일정

    private SiteCapacity siteCapacity;
    private String mapImage;
    private String policy;
    private Integer price;
    private LocalDateTime refundableDate;


}
