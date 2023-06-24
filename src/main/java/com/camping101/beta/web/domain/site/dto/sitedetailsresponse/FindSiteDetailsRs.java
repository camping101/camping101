package com.camping101.beta.web.domain.site.dto.sitedetailsresponse;

import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.db.entity.site.SiteCapacity;
import com.camping101.beta.db.entity.site.SiteType;
import com.camping101.beta.db.entity.site.SiteYn;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FindSiteDetailsRs {

    private Long siteId;
    private Long campId;
    private String name;
    private String rpImage; // 대표이미지
    private String introduction;
    private SiteType type;
    private boolean openYn; // 이거를 활성화, 비활성화 , 탈퇴상태로 바꿀까?
    private SiteYn siteYn;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int leastScheduling;
    private SiteCapacity siteCapacity;
    private String mapImage;
    private String policy;
    private int price;
    private LocalDate refundableDate;
//    private List<ReservationDto> reservationDtoList;
//    private List<CampLogDto> campLogDtoList;


    public static FindSiteDetailsRs createFindSiteDetailsRs(Site site) {
        return FindSiteDetailsRs
            .builder()
            .siteId(site.getSiteId())
            .campId(site.getCamp().getCampId())
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
