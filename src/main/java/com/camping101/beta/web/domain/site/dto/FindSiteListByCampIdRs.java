package com.camping101.beta.web.domain.site.dto;

import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.db.entity.site.SiteCapacity;
import com.camping101.beta.db.entity.site.SiteType;
import com.camping101.beta.db.entity.site.SiteYn;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FindSiteListByCampIdRs {

    private Long siteId;
    private String name;
    private String rpImage; //대표 이미지
    private String introduction;
    private SiteType type;
    private boolean openYn;
    private SiteYn siteYn;
    private LocalDate checkIn; // 체크 인 시간
    private LocalDate checkOut;// 체크 아웃 시간
    private int leastScheduling; // 최소 일정
    private SiteCapacity siteCapacity;
    private String mapImage;
    private String policy;
    private int price;
    private LocalDate refundableDate;

    public static FindSiteListByCampIdRs createSiteListRs(Site site) {

        return FindSiteListByCampIdRs
            .builder()
            .siteId(site.getSiteId())
            .name(site.getName())
            .rpImage(site.getRpImage())
            .introduction(site.getIntroduction())
            .type(site.getType())
            .openYn(site.isOpenYn())
            .siteYn(site.getSiteYn())
            .checkIn(site.getCheckIn())
            .checkOut(site.getCheckOut())
            .leastScheduling(site.getLeastScheduling())
            .siteCapacity(site.getSiteCapacity())
            .mapImage(site.getMapImage())
            .policy(site.getPolicy())
            .price(site.getPrice())
            .refundableDate(site.getRefundableDate())
            .build();
    }
}
