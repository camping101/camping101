package com.camping101.beta.web.domain.site.dto;

import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.db.entity.site.SiteCapacity;
import com.camping101.beta.db.entity.site.SiteType;
import com.camping101.beta.db.entity.site.SiteYn;
import java.time.LocalDate;
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
public class CreateSiteRq {

    private Long campId;

    private String name;
    private String rpImage; //대표 이미지
    private String introduction;
    private SiteType type;
    private SiteYn siteYn;
    private LocalDate checkIn; // 체크 인 시간
    private LocalDate checkOut;// 체크 아웃 시간
    private Integer leastScheduling; // 최소 일정

    private SiteCapacity siteCapacity;
    private String mapImage;
    private String policy;
    private Integer price;
    private LocalDate refundableDate;

    public static Site createSite(CreateSiteRq rq) {

        return Site.builder()
            .name(rq.getName())
            .rpImage(rq.getRpImage())
            .introduction(rq.getIntroduction())
            .type(rq.getType())
            .openYn(false)
            .siteYn(rq.getSiteYn())
            .checkIn(rq.getCheckIn())
            .checkOut(rq.getCheckOut())
            .leastScheduling(rq.getLeastScheduling())
            .siteCapacity(rq.getSiteCapacity())
            .mapImage(rq.getMapImage())
            .policy(rq.getPolicy())
            .price(rq.getPrice())
            .refundableDate(rq.getRefundableDate())
            .build();

    }


}
